package com.groomiz.billage.reservation.repository;

import static com.groomiz.billage.building.entity.QBuilding.*;
import static com.groomiz.billage.classroom.entity.QClassroom.*;
import static com.groomiz.billage.member.entity.QMember.*;
import static com.groomiz.billage.reservation.entity.QReservation.*;
import static com.groomiz.billage.reservation.entity.QReservationStatus.*;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.reservation.dto.ReservationSearchCond;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse.ReservationInfo;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ReservationInfo> searchPendingReservationPageByBuilding(ReservationSearchCond reservationSearchCond) {
		return fetchReservationPage(
			reservationSearchCond.toPageable(),
			true,
			statusEq(ReservationStatusType.PENDING),
			buildingIn(reservationSearchCond.getBuildingIds())
		);
	}

	@Override
	public Page<ReservationInfo> searchApprovedReservationPageByAdmin(ReservationSearchCond reservationSearchCond) {
		return fetchReservationPage(
			reservationSearchCond.toPageable(),
			false,
			statusEq(ReservationStatusType.APPROVED),
			adminEq(reservationSearchCond.getAdmin())
		);
	}

	@Override
	public Page<ReservationInfo> searchRejectedAndCanceledReservationPageByAdmin(ReservationSearchCond reservationSearchCond) {
		return fetchReservationPage(
			reservationSearchCond.toPageable(),
			false,
			statusIn(List.of(ReservationStatusType.STUDENT_CANCELED, ReservationStatusType.ADMIN_CANCELED, ReservationStatusType.REJECTED)),
			adminEq(reservationSearchCond.getAdmin())
		);
	}

	private Page<ReservationInfo> fetchReservationPage(Pageable pageable, boolean includeBuildingJoin, BooleanExpression... conditions) {
		List<ReservationInfo> content = queryFactory
			.select(
				Projections.fields(ReservationInfo.class,
					reservation.id.as("reservationId"),
					reservation.applyDate.as("date"),
					reservation.startTime,
					reservation.endTime,
					reservation.headcount,
					building.name.as("buildingName"),
					classroom.floor,
					classroom.name.as("classroomName"),
					member.username.as("studentName"),
					member.studentNumber
				)
			)
			.from(reservation)
			.join(reservation.reservationStatus, reservationStatus)
			.join(reservation.classroom, classroom)
			.join(classroom.building, building)
			.join(reservationStatus.requester, member)
			.where(conditions)
			.orderBy(reservation.applyDate.desc(), reservation.startTime.desc(), reservation.endTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long totalCount = null;
		if (includeBuildingJoin) {
			totalCount = queryFactory
				.select(reservation.count())
				.from(reservation)
				.join(reservation.reservationStatus, reservationStatus)
				.join(reservation.classroom, classroom)
				.join(classroom.building, building)
				.where(conditions)
				.fetchOne();
		} else {
			totalCount = queryFactory
				.select(reservation.count())
				.from(reservation)
				.join(reservation.reservationStatus, reservationStatus)
				.where(conditions)
				.fetchOne();
		}

		return PageableExecutionUtils.getPage(content, pageable, totalCount::intValue);
	}

	private static BooleanExpression buildingIn(Collection<Long> buildingIds) {
		return buildingIds != null && !buildingIds.isEmpty() ? building.id.in(buildingIds) : null;
	}

	private BooleanExpression adminEq(Member admin) {
		return admin != null ? reservationStatus.admin.eq(admin) : null;
	}

	private BooleanExpression statusEq(ReservationStatusType status) {
		return status != null ? reservationStatus.status.eq(status) : null;
	}

	private BooleanExpression statusIn(Collection<ReservationStatusType> statuses) {
		return statuses != null && !statuses.isEmpty() ? reservationStatus.status.in(statuses) : null;
	}
}

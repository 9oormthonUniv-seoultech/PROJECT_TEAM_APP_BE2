package com.groomiz.billage.reservation.repository;

import static com.groomiz.billage.building.entity.QBuilding.*;
import static com.groomiz.billage.classroom.entity.QClassroom.*;
import static com.groomiz.billage.member.entity.QMember.*;
import static com.groomiz.billage.reservation.entity.QReservation.*;
import static com.groomiz.billage.reservation.entity.QReservationStatus.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.reservation.dto.AdminReservationSearchCond;
import com.groomiz.billage.reservation.dto.ReservationSearchCond;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse.ReservationInfo;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<AdminReservationStatusListResponse.ReservationInfo> searchPendingReservationPageByBuilding(
		AdminReservationSearchCond adminReservationSearchCond) {
		return fetchAdminReservationPage(
			adminReservationSearchCond.toPageable(),
			statusEq(ReservationStatusType.PENDING),
			buildingIdIn(adminReservationSearchCond.getBuildings())
		);
	}

	@Override
	public Page<AdminReservationStatusListResponse.ReservationInfo> searchApprovedReservationPageByAdmin(
		AdminReservationSearchCond adminReservationSearchCond) {
		return fetchAdminReservationPage(
			adminReservationSearchCond.toPageable(),
			statusEq(ReservationStatusType.APPROVED),
			adminEq(adminReservationSearchCond.getAdmin())
		);
	}

	@Override
	public Page<AdminReservationStatusListResponse.ReservationInfo> searchRejectedAndCanceledReservationPageByAdmin(
		AdminReservationSearchCond adminReservationSearchCond) {
		return fetchAdminReservationPage(
			adminReservationSearchCond.toPageable(),
			statusIn(List.of(ReservationStatusType.STUDENT_CANCELED, ReservationStatusType.ADMIN_CANCELED, ReservationStatusType.REJECTED)),
			adminEq(adminReservationSearchCond.getAdmin())
		);
	}

	@Override
	public Page<ReservationStatusListResponse.ReservationInfo> searchUpcomingReservationPageByRequester(
		ReservationSearchCond reservationSearchCond) {
		LocalDate today = LocalDate.now();
		return fetchReservationPage(
			reservationSearchCond.toPageable(),
			requesterEq(reservationSearchCond.getRequester()),
			applyDateGoe(today)
		);
	}

	@Override
	public Page<ReservationStatusListResponse.ReservationInfo> searchPastReservationPageByRequester(
		ReservationSearchCond reservationSearchCond) {
		LocalDate today = LocalDate.now();
		return fetchReservationPage(
			reservationSearchCond.toPageable(),
			requesterEq(reservationSearchCond.getRequester()),
			applyDateLt(today)
		);
	}

	private Page<ReservationInfo> fetchAdminReservationPage(Pageable pageable, BooleanExpression... conditions) {
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

		JPAQuery<Long> countQuery = queryFactory
				.select(reservation.count())
				.from(reservation)
				.join(reservation.reservationStatus, reservationStatus)
				.join(reservation.classroom, classroom)
				.join(classroom.building, building)
				.join(reservationStatus.requester, member)
				.where(conditions);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private Page<ReservationStatusListResponse.ReservationInfo> fetchReservationPage(Pageable pageable, BooleanExpression... conditions) {
		List<ReservationStatusListResponse.ReservationInfo> content = queryFactory
			.select(
				Projections.fields(ReservationStatusListResponse.ReservationInfo.class,
					reservation.id.as("reservationId"),
					reservation.applyDate,
					reservation.startTime,
					reservation.endTime,
					reservation.headcount,
					classroom.name.as("classroomName"),
					classroom.number.as("classroomNumber"),
					reservationStatus.status.as("reservationStatus"),
					reservationStatus.rejectionReason,
					reservationStatus.requester.phoneNumber.as("adminPhoneNumber")
				)
			)
			.from(reservation)
			.join(reservation.reservationStatus, reservationStatus)
			.join(reservation.classroom, classroom)
			.where(conditions)
			.orderBy(reservation.applyDate.desc(), reservation.startTime.desc(), reservation.endTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		 JPAQuery<Long> countQuery = queryFactory
			.select(reservation.count())
			.from(reservation)
			.where(conditions);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

	}

	private BooleanExpression buildingIdIn(Collection<Building> buildings) {
		return buildings != null && !buildings.isEmpty() ? building.in(buildings) : null;
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

	private BooleanExpression requesterEq(Member requester) {
		return requester != null ? reservationStatus.requester.eq(requester) : null;
	}

	private BooleanExpression applyDateGoe(LocalDate today) {
		return today != null ? reservation.applyDate.goe(today) : null;
	}

	private BooleanExpression applyDateLt(LocalDate today) {
		return today != null ? reservation.applyDate.lt(today) : null;
	}
}

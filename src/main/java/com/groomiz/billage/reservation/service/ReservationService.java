package com.groomiz.billage.reservation.service;

import static com.groomiz.billage.common.util.DateUtills.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.dto.ReservationSearchCond;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.ReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.entity.ReservationStatus;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.repository.ReservationRepository;
import com.groomiz.billage.reservation.repository.ReservationStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final ClassroomRepository classroomRepository;
	private final ReservationStatusRepository reservationStatusRepository;

	// 예약 생성
	public Long reserveClassroom(ClassroomReservationRequest request, String studentNumber) {

		// 기타 목적 예외
		if (request.getPurpose().equals(ReservationPurpose.OTHERS)
			&& (request.getContents() == null || request.getContents().trim().isEmpty())) {
			throw new ReservationException(ReservationErrorCode.NO_PURPOSE_IN_ETC);
		}

		// 예약 시작 시간과 종료 시간 비교 예외
		if (request.getStartTime().isAfter(request.getEndTime())) {
			throw new ReservationException(ReservationErrorCode.START_TIME_AFTER_END_TIME);
		}
		
		// 중복 예약 예외
		List<ReservationTime> reservations = reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(
			request.getApplyDate(), request.getClassroomId());

		boolean hasConflict = reservations.stream().anyMatch(
			reservationTime ->
				isTimeOverlapping(request.getStartTime(), request.getEndTime(), reservationTime.getStartTime(),
					reservationTime.getEndTime()));

		if (hasConflict) {
			throw new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
		}

		Member requester = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		Classroom classroom = classroomRepository.findById(request.getClassroomId())
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		// 예약 현황 생성
		ReservationStatus status = new ReservationStatus(requester);

		// 예약 생성
		Reservation reservation = Reservation.builder()
			.applyDate(request.getApplyDate())
			.headcount(request.getHeadcount())
			.startTime(request.getStartTime())
			.endTime(request.getEndTime())
			.purpose(request.getPurpose())
			.phoneNumber(request.getPhoneNumber())
			.contents(request.getContents())
			.classroom(classroom)
			.reservationStatus(status)
			.build();

		// 예약, 예약 현황 저장
		reservationStatusRepository.save(status);
		Reservation savedReservation = reservationRepository.save(reservation);

		return savedReservation.getId();
	}

	// 학생 예약 취소
	public void cancelReservation(Long id, String studentNumber) {

		Reservation reservation = reservationRepository.findById(id)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		if (reservation.getReservationStatus().isApproved()) {
			throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_APPROVED);
		} else if (reservation.getReservationStatus().isRejected()) {
			throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);
		} else if (reservation.getReservationStatus().isCanceledByStudent()) {
			throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_DELETED);
		} else if (reservation.getReservationStatus().isCanceledByAdmin()) {
			throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);
		}

		// 예약 기간 지난 경우 예외
		if (reservation.getApplyDate().isBefore(LocalDate.now())) {
			throw new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);
		}

		// 유저와 예약자가 다른 경우 예외
		String requesterNumber = reservation.getReservationStatus().getRequester().getStudentNumber();

		if (!requesterNumber.equals(studentNumber)) {
			throw new ReservationException(ReservationErrorCode.USER_RESERVATION_MISMATCH);
		}

		reservation.getReservationStatus().updateStatus(ReservationStatusType.STUDENT_CANCELED);
	}

	@Transactional(readOnly = true)
	public ReservationStatusListResponse getAllReservationStatus(boolean isPast, int page, String studentNumber) {

		Member requester = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		ReservationSearchCond reservationSearchCond = new ReservationSearchCond(requester);
		reservationSearchCond.setPage(page);

		Page<ReservationStatusListResponse.ReservationInfo> reservationPage;

		if (isPast) {
			reservationPage = reservationRepository.searchPastReservationPageByRequester(
				reservationSearchCond);
		} else {
			reservationPage = reservationRepository.searchUpcomingReservationPageByRequester(
				reservationSearchCond);
		}

		return ReservationStatusListResponse.builder()
			.totalReservations(reservationPage.getTotalElements())
			.totalPages(reservationPage.getTotalPages())
			.reservations(reservationPage.getContent())
			.build();
	}
}

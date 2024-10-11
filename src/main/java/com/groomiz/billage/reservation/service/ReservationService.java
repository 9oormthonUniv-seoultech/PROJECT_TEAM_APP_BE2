package com.groomiz.billage.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.entity.ReservationStatus;
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

	public Long reserveClassroom(ClassroomReservationRequest request, String studentNumber) {

		// 예약 인원 음수 예외
		if (request.getHeadcount() <= 0) {
			throw new ReservationException(ReservationErrorCode.NEGATIVE_PARTICIPANTS);
		}

		// 최대 인원 초과 예외
		if (request.getHeadcount() > 100) {
			throw new ReservationException(ReservationErrorCode.EXCEED_MAX_PARTICIPANTS);
		}

		// 예약 날짜 과거 예외
		if (request.getApplyDate().isBefore(LocalDate.now())) {
			throw new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);
		}

		// 예약 날짜 한달 이후 예외
		if (request.getApplyDate().isAfter(LocalDate.now().plusMonths(1))) {
			throw new ReservationException(ReservationErrorCode.FUTURE_DATE_LIMIT_EXCEEDED);
		}

		// 기타 목적 예외
		if (request.getPurpose().equals(ReservationPurpose.OTHERS)
			&& ( request.getContents().trim().isEmpty() || request.getContents() == null)) {
			throw new ReservationException(ReservationErrorCode.NO_PURPOSE_IN_ETC);
		}

		// 예약 시작 시간과 종료 시간 비교 예외
		if (request.getStartTime().isAfter(request.getEndTime())) {
			throw new ReservationException(ReservationErrorCode.START_TIME_AFTER_END_TIME);
		}
		
		// 중복 예약 예외
		reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(request.getApplyDate(), request.getClassroomId())
			.forEach(reservationTime -> {
				if (isTimeOverlapping(request.getStartTime(), request.getEndTime(), reservationTime.getStartTime(), reservationTime.getEndTime())) {
					throw new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
				}
			});

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

	public boolean isTimeOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2, LocalTime endTime2) {
		return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
	}
}

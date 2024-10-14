package com.groomiz.billage.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.auth.dto.CustomUserDetails;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.dto.request.AdminRejectionRequest;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationStatus;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.repository.ReservationRepository;
import com.groomiz.billage.reservation.repository.ReservationStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminReservationService {

	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;

	public void approveReservation(Long reservationId, String studentNumber) {

		Member admin = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		// 예약 승인
		ReservationStatusType status = reservation.getReservationStatus().getStatus();
		switch (status) {
			case PENDING:
				reservation.getReservationStatus().approve(admin);
				break;
			case APPROVED:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_APPROVED);
			case REJECTED:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);
			default:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_CANCELED);
		}
	}

	public void rejectReservation(Long reservationId, String rejectionReason, String studentNumber) {

		Member admin = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		// 예약 거절
		ReservationStatusType status = reservation.getReservationStatus().getStatus();
		switch (status) {
			case PENDING:
				reservation.getReservationStatus().reject(admin, rejectionReason);
				break;
			case APPROVED:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_APPROVED);
			case REJECTED:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);
			default:
				throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_CANCELED);
		}
	}
}

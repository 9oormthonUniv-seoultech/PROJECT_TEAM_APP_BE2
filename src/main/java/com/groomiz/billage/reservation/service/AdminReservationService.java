package com.groomiz.billage.reservation.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.groomiz.billage.auth.service.RedisService;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminReservationService {

	private final MemberRepository memberRepository;
	private final ReservationRepository reservationRepository;
	private final RedisService redisService;

	public void approveReservation(Long reservationId, String studentNumber) throws FirebaseMessagingException {

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


		// 예약자 푸시 알림
		Member requester = reservation.getReservationStatus().getRequester();

		String title = "예약 승인 완료";
		String body = reservation.getApplyDate().toString() + " "
			+ reservation.getStartTime().toString() + "-" + reservation.getEndTime().toString()
			+ " 예약이 승인되었습니다.";

		sendMessage(requester.getStudentNumber(), title, body);
	}

	public void rejectReservation(Long reservationId, String rejectionReason, String studentNumber) throws
		FirebaseMessagingException {

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

		// 예약자 푸시 알림
		Member requester = reservation.getReservationStatus().getRequester();

		String title = "예약 거절";
		String body = reservation.getApplyDate().toString() + " "
			+ reservation.getStartTime().toString() + "-" + reservation.getEndTime().toString()
			+ " 예약이 거절되었습니다.";

		sendMessage(requester.getStudentNumber(), title, body);
	}

	private void sendMessage(String studentNumber, String title, String body) throws FirebaseMessagingException {
		String key = "FCM_" + studentNumber;
		String token = redisService.getValues(key);

		if (Objects.equals(token, "false")) {
			throw new MemberException(MemberErrorCode.FCM_TOKEN_NOT_FOUND);
		}

		Message message = Message.builder()
			.setNotification(Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build())
			.setToken(token)
			.build();

		FirebaseMessaging.getInstance().send(message);
	}
}

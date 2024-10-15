package com.groomiz.billage.reservation.service;

import static com.groomiz.billage.common.util.DateUtills.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.groomiz.billage.auth.service.RedisService;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.dto.request.AdminReservationRequest;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationGroup;
import com.groomiz.billage.reservation.entity.ReservationStatus;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.repository.ReservationGroupRepository;
import com.groomiz.billage.reservation.repository.ReservationRepository;
import com.groomiz.billage.reservation.repository.ReservationStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminReservationService {

	private final MemberRepository memberRepository;

	private final ReservationRepository reservationRepository;
	private final ReservationStatusRepository reservationStatusRepository;
	private final ReservationGroupRepository reservationGroupRepository;

	private final ClassroomRepository classroomRepository;

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

	public void reserveClassroom(AdminReservationRequest request, String studentNumber) {

		Member requester = memberRepository.findByStudentNumber(studentNumber)
			.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

		Classroom classroom = classroomRepository.findById(request.getClassroomId())
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		// 예약 날짜 과거 예외
		if (request.getStartDate().isBefore(LocalDate.now())) {
			throw new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);
		}

		// 예약 시작 시간과 종료 시간 비교 예외
		if (request.getStartTime().isAfter(request.getEndTime())) {
			throw new ReservationException(ReservationErrorCode.START_TIME_AFTER_END_TIME);
		}

		// 타입별 예약 생성
		switch (request.getType()) {
			case SINGLE:
				createSingleReservation(request, requester, classroom);
				break;
			case PERIOD:
				createPeriodReservation(request, requester, classroom);
				break;
			case RECURRING:
				createRecurringReservation(request, requester, classroom);
				break;
			default:
		}
	}

	private void createSingleReservation(AdminReservationRequest request, Member requester, Classroom classroom) {

		// 중복 예약 예외
		reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(request.getStartDate(), request.getClassroomId())
			.forEach(reservationTime -> {
				if (isTimeOverlapping(request.getStartTime(), request.getEndTime(), reservationTime.getStartTime(), reservationTime.getEndTime())) {
					throw new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
				}
			});

		ReservationStatus status = ReservationStatus.builder()
			.requester(requester)
			.status(ReservationStatusType.APPROVED)
			.build();

		Reservation reservation = Reservation.builder()
			.classroom(classroom)
			.applyDate(request.getStartDate())
			.startTime(request.getStartTime())
			.endTime(request.getEndTime())
			.reservationStatus(status)
			.build();

		reservationStatusRepository.save(status);
		reservationRepository.save(reservation);
	}

	private void createPeriodReservation(AdminReservationRequest request, Member requester, Classroom classroom) {

		int period = (int)ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());

		ReservationGroup group = new ReservationGroup();
		List<ReservationStatus> statuses = new ArrayList<>(period);

		for (int i = 0; i <= period; i++) {
			LocalDate date = request.getStartDate().plusDays(i);

			// 중복 예약 예외
			reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(date, request.getClassroomId())
				.forEach(reservationTime -> {
					if (isTimeOverlapping(request.getStartTime(), request.getEndTime(), reservationTime.getStartTime(), reservationTime.getEndTime())) {
						throw new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
					}
				});

			ReservationStatus status = ReservationStatus.builder()
				.requester(requester)
				.status(ReservationStatusType.APPROVED)
				.build();

			Reservation reservation = Reservation.builder()
				.classroom(classroom)
				.applyDate(date)
				.startTime(request.getStartTime())
				.endTime(request.getEndTime())
				.reservationStatus(status)
				.group(group)
				.build();

			group.getReservations().add(reservation);
			statuses.add(status);
		}

		reservationStatusRepository.saveAll(statuses);
		reservationGroupRepository.save(group);
	}


	private void createRecurringReservation(AdminReservationRequest request, Member requester, Classroom classroom) {

		ReservationGroup group = new ReservationGroup();
		List<ReservationStatus> statuses = new ArrayList<>();

		// 요일 정렬
		List<DayOfWeek> days = request.getDays().stream().sorted().toList();

		LocalDate date = request.getStartDate();

		boolean st = true;

		while (st) {

			for (DayOfWeek day : request.getDays()) {
				// 요일별 날짜
				LocalDate with = date.with(day);

				// 중복 예약 예외
				reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(date, request.getClassroomId())
					.forEach(reservationTime -> {
						if (isTimeOverlapping(request.getStartTime(), request.getEndTime(), reservationTime.getStartTime(), reservationTime.getEndTime())) {
							throw new ReservationException(ReservationErrorCode.DUPLICATE_RESERVATION);
						}
					});

				if (with.isBefore(request.getStartDate())) {
					continue;
				}
				else if (with.isAfter(request.getEndDate())) {
					st = false;
					break;
				} else {
					ReservationStatus status = ReservationStatus.builder()
						.requester(requester)
						.status(ReservationStatusType.APPROVED)
						.build();

					Reservation reservation = Reservation.builder()
						.classroom(classroom)
						.applyDate(with)
						.startTime(request.getStartTime())
						.endTime(request.getEndTime())
						.reservationStatus(status)
						.group(group)
						.build();

					group.getReservations().add(reservation);
					statuses.add(status);
				}
			}
			date = date.plusWeeks(1);
		}

		reservationStatusRepository.saveAll(statuses);
		reservationGroupRepository.save(group);
	}

}

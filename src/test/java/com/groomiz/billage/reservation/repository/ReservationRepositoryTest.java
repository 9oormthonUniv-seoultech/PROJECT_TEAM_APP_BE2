package com.groomiz.billage.reservation.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.member.entity.College;
import com.groomiz.billage.member.entity.Major;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.service.ReservationService;

@SpringBootTest
@Transactional
class ReservationRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private ClassroomRepository classroomRepository;

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ReservationRepository reservationRepository;

	@Test
	public void 해당하는_날짜에_승인_미승인_예약_시작시간_종료시간_조회_성공() throws Exception{
		//given
		Member student = saveStudent();
		Building building = saveBuilding("미래관");
		Building building2 = saveBuilding("상상관");
		Classroom classroom = saveClassroom(building);
		Classroom classroom2 = saveClassroom(building2);

		LocalDate applyDate = LocalDate.now();
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);
		LocalTime startTime2 = LocalTime.of(14, 0);
		LocalTime endTime2 = LocalTime.of(15, 0);
		LocalTime startTime3 = LocalTime.of(15, 0);
		LocalTime endTime3 = LocalTime.of(16, 0);
		LocalTime startTime4 = LocalTime.of(13, 0);
		LocalTime endTime4 = LocalTime.of(16, 0);

		Long reservationId = reserveClassroom(classroom, student, applyDate, startTime, endTime);
		Long reservationId2 = reserveClassroom(classroom, student, applyDate, startTime2, endTime2);
		Long reservationId3 = reserveClassroom(classroom, student, applyDate, startTime3, endTime3);
		Long reservationId4 = reserveClassroom(classroom2, student, applyDate, startTime4, endTime4);

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
		Reservation reservation2 = reservationRepository.findById(reservationId2)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
		reservation.getReservationStatus().updateStatus(ReservationStatusType.APPROVED);
		reservation2.getReservationStatus().updateStatus(ReservationStatusType.REJECTED);

		//when
		List<ReservationTime> result = reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(
			applyDate, classroom.getId());

		//then
		Assertions.assertAll(
			() -> {
				assertThat(result.size()).isEqualTo(2);
			}
		);
	}

	private Long reserveClassroom(Classroom classroom, Member student, LocalDate applyDate, LocalTime startTime, LocalTime endTime) throws
		FirebaseMessagingException {

		String phoneNumber = "010-1234-5678";

		ClassroomReservationRequest request = ClassroomReservationRequest.builder()
			.classroomId(classroom.getId())
			.phoneNumber(phoneNumber)
			.applyDate(applyDate)
			.headcount(30)
			.startTime(startTime)
			.endTime(endTime)
			.purpose(ReservationPurpose.CLUB_EVENT)
			.build();

		return reservationService.reserveClassroom(request, student.getStudentNumber());
	}

	private Member saveStudent() {
		Member member = Member.builder()
			.username("홍길동")
			.password("password1234!")
			.phoneNumber("010-1234-5678")
			.role(Role.STUDENT)
			.isAdmin(false)
			.isValid(true)
			.agreedToTerms(true)
			.studentNumber("21000000")
			.college(College.ICE)
			.major(Major.CS)
			.build();

		return memberRepository.save(member);
	}

	private Building saveBuilding(String name) {
		Building building = Building.builder()
			.name(name)
			.number("60")
			.build();

		return buildingRepository.save(building);
	}

	private Classroom saveClassroom(Building building) {
		Classroom classroom = Classroom.builder()
			.number("101")
			.floor(1L)
			.capacity(30)
			.building(building)
			.build();

		return classroomRepository.save(classroom);
	}
}
package com.groomiz.billage.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groomiz.billage.auth.dto.RegisterRequest;
import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.entity.BuildingAdmin;
import com.groomiz.billage.building.repository.BuildingAdminRepository;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.member.entity.Member;
import com.groomiz.billage.member.entity.Role;
import com.groomiz.billage.member.repository.MemberRepository;
import com.groomiz.billage.member.service.MemberService;
import com.groomiz.billage.reservation.dto.request.ClassroomReservationRequest;
import com.groomiz.billage.reservation.dto.response.AdminReservationStatusListResponse;
import com.groomiz.billage.reservation.entity.ReservationPurpose;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
class AdminReservationServiceTest {

	@Autowired
	AdminReservationService adminReservationService;

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberService memberService;

	@Autowired
	BuildingRepository buildingRepository;

	@Autowired
	ClassroomRepository classroomRepository;

	@Autowired
	ReservationService reservationService;
	@Autowired
	ReservationRepository reservationRepository;
	@Autowired
	private BuildingAdminRepository buildingAdminRepository;

	@BeforeEach
	public void setUp() throws FirebaseMessagingException {
		register("admin", Role.ADMIN, "1");
		register("student", Role.STUDENT, "2");

		Member admin = memberRepository.findByStudentNumber("1").get();
		Member student = memberRepository.findByStudentNumber("2").get();


		Building building = saveBuilding("미래관");
		Building building2 = saveBuilding("상상관");
		Classroom classroom = saveClassroom(building);
		Classroom classroom2 = saveClassroom(building2);

		LocalDate applyDate = LocalDate.now();
		LocalDate applyDate2 = LocalDate.now().plusDays(3);
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);
		LocalTime startTime2 = LocalTime.of(15, 0);
		LocalTime endTime2 = LocalTime.of(16, 0);
		LocalTime startTime3 = LocalTime.of(17, 0);
		LocalTime endTime3 = LocalTime.of(20, 0);
		LocalTime startTime4 = LocalTime.of(13, 0);
		LocalTime endTime4 = LocalTime.of(16, 0);

		Long rid = reserveClassroom(classroom, student, applyDate, startTime, endTime);
		Long rid2 = reserveClassroom(classroom, student, applyDate, startTime2, endTime2);
		Long rid3 = reserveClassroom(classroom, student, applyDate2, startTime3, endTime3);
		Long rid4 = reserveClassroom(classroom2, admin, applyDate, startTime4, endTime4);
		Long rid5 = reserveClassroom(classroom2, admin, applyDate2, startTime2, endTime2);
		Long rid6 = reserveClassroom(classroom2, student, applyDate2, startTime3, endTime3);

		// 대기 2, 승인 1, 거절 1, 관리자 취소 1, 학생 취소 1
		reservationRepository.findById(rid).get().getReservationStatus().approve(admin);
		reservationRepository.findById(rid2).get().getReservationStatus().reject(admin, "null");
		reservationRepository.findById(rid5).get().getReservationStatus().cancelByAdmin(admin);
		reservationRepository.findById(rid6).get().getReservationStatus().approve(admin);
		reservationRepository.findById(rid6).get().getReservationStatus().cancelByStudent();

		BuildingAdmin buildingAdmin = new BuildingAdmin(admin, building);
		BuildingAdmin buildingAdmin2 = new BuildingAdmin(admin, building2);
		buildingAdminRepository.save(buildingAdmin);
		buildingAdminRepository.save(buildingAdmin2);
	}

	@Test
	@Rollback(value = false)
	public void 관리자_대기_예약_조회_성공() throws Exception{
	    //given
		String adminStudentNumber = "1";

	    //when
		AdminReservationStatusListResponse pending = adminReservationService.getReservationByStatus(
			ReservationStatusType.PENDING, 1, adminStudentNumber);
		AdminReservationStatusListResponse approved = adminReservationService.getReservationByStatus(
			ReservationStatusType.APPROVED, 1, adminStudentNumber);
		AdminReservationStatusListResponse rejectedAndCanceled = adminReservationService.getReservationByStatus(
			ReservationStatusType.REJECTED, 1, adminStudentNumber);

	    //then
		Assertions.assertAll(
			() -> {
				assertThat(pending.getReservations().size()).isEqualTo(2);
				assertThat(approved.getReservations().size()).isEqualTo(1);
				assertThat(rejectedAndCanceled.getReservations().size()).isEqualTo(3);
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

	private Member register(String username, Role role, String studentNumber) {
		RegisterRequest registerRequest = new RegisterRequest(username, studentNumber, "password1234!", "010-1234-5678",
			"정보통신대학", "컴퓨터공학과", true, "asdf1234@gmail.com");

		memberService.register(registerRequest);
		return memberRepository.findByStudentNumber(studentNumber).get();
	}

	private Building saveBuilding(String name) {
		Building building = Building.builder()
			.name(name)
			.startFloor(1L)
			.endFloor(3L)
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
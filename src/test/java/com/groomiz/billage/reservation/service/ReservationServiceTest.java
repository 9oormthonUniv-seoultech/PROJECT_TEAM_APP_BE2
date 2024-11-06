package com.groomiz.billage.reservation.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.repository.BuildingRepository;
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
import com.groomiz.billage.reservation.entity.ReservationStatus;
import com.groomiz.billage.reservation.entity.ReservationStatusType;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;
import com.groomiz.billage.reservation.repository.ReservationRepository;

@SpringBootTest
@Transactional
class ReservationServiceTest {

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
	public void 학생_강의실_예약_성공() throws Exception{
	    //given
		Member student = saveStudent();
		Building building = saveBuilding("미래관");
		Classroom classroom = saveClassroom(building);


		LocalDate applyDate = LocalDate.now();
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);

		Long reservationId = reserveClassroom(classroom, student, applyDate, startTime, endTime);

		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		//then
		Assertions.assertAll(
			() -> {
				assert reservation != null;
				ReservationStatus reservationStatus = reservation.getReservationStatus();
				assert reservationStatus != null;
				assertThat(reservationStatus.getRequester()).isEqualTo(student);
			}
		);
	}

	@Test
	public void 강의실_시간_중복_예약_시_예외() throws Exception{
		//given
		Member student = saveStudent();
		Building building = saveBuilding("미래관");
		Building building2 = saveBuilding("상상관");
		Classroom classroom = saveClassroom(building);
		Classroom classroom2 = saveClassroom(building2);

		LocalDate applyDate = LocalDate.now();
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);
		LocalTime startTime2 = LocalTime.of(15, 0);
		LocalTime endTime2 = LocalTime.of(16, 0);
		LocalTime startTime3 = LocalTime.of(14, 0);
		LocalTime endTime3 = LocalTime.of(20, 0);
		LocalTime startTime4 = LocalTime.of(13, 0);
		LocalTime endTime4 = LocalTime.of(16, 0);

		reserveClassroom(classroom, student, applyDate, startTime, endTime);
		reserveClassroom(classroom, student, applyDate, startTime2, endTime2);
		reserveClassroom(classroom2, student, applyDate, startTime4, endTime4);

		//when & then
		assertThatThrownBy(() -> reserveClassroom(classroom, student, applyDate, startTime3, endTime3))
			.isInstanceOf(ReservationException.class);
	}
	
	@Test
	public void 학생_예약_취소_성공() throws Exception{
		//given
		Member student = saveStudent();
		Building building = saveBuilding("미래관");
		Classroom classroom = saveClassroom(building);

		LocalDate applyDate = LocalDate.now();
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);

		Long reservationId = reserveClassroom(classroom, student, applyDate, startTime, endTime);

		//when
		reservationService.cancelReservation(reservationId, student.getStudentNumber());

		//then
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		assertThat(reservation.getReservationStatus().isCanceledByStudent()).isTrue();
	}


	@Test
	public void 학생_예약_실패_이미_거절() throws Exception{
		//given
		Member student = saveStudent();
		Building building = saveBuilding("미래관");
		Classroom classroom = saveClassroom(building);

		LocalDate applyDate = LocalDate.now();
		LocalTime startTime = LocalTime.of(13, 0);
		LocalTime endTime = LocalTime.of(14, 0);

		Long reservationId = reserveClassroom(classroom, student, applyDate, startTime, endTime);

		//when
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
		reservation.getReservationStatus().updateStatus(ReservationStatusType.REJECTED);

		//then
		assertThatThrownBy(() -> reservationService.cancelReservation(reservationId, student.getStudentNumber()))
			.isInstanceOf(ReservationException.class);
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
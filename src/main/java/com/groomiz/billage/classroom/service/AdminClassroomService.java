package com.groomiz.billage.classroom.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.dto.request.AdminClassroomStatusRequest;
import com.groomiz.billage.classroom.dto.response.AdminClassroomDetailResponse;
import com.groomiz.billage.classroom.dto.response.AdminClassroomStatusResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminClassroomService {

	private final ClassroomRepository classroomRepository;
	private final BuildingRepository buildingRepository;
	private final ReservationRepository reservationRepository;

	@Transactional(readOnly = true)
	public AdminClassroomDetailResponse findClassroomByIdAndDate(Long classroomId, LocalDate date) {

		Classroom classroom = classroomRepository.findClassroomByIdAndDate(classroomId, date)
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		return AdminClassroomDetailResponse.from(classroom, date);
	}

	@Transactional(readOnly = true)
	public AdminClassroomStatusResponse findClassroomsByFilter(AdminClassroomStatusRequest request) {

		// 건물 조회
		List<Long> buildingIds = (request.getBuildings() == null || request.getBuildings().isEmpty())
			? null : request.getBuildings();
		List<Long> floors = (request.getFloors() == null || request.getFloors().isEmpty())
			? null : request.getFloors();

		List<Building> buildings = buildingRepository.findBuildingsWithClassroomByIdsAndFloors(
		buildingIds, floors);

		// 건물에 속한 강의실 ID 추출
		List<Long> classroomIds = buildings.stream()
			.flatMap(building -> building.getClassrooms().stream())
			.map(Classroom::getId)
			.collect(Collectors.toList());

		// 특정 날짜 강의실 예약 조회
		List<Reservation> reservations = reservationRepository.findReservationsByClassroomIdsAndDate(
			classroomIds, request.getDate());

		return AdminClassroomStatusResponse.of(request.getDate(), buildings, reservations);
	}
}

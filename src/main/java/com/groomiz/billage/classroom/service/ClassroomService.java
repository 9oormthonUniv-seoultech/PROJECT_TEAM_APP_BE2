package com.groomiz.billage.classroom.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;
import com.groomiz.billage.building.repository.BuildingRepository;
import com.groomiz.billage.classroom.dto.response.ClassroomDetailResponse;
import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
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
public class ClassroomService {

	private final ClassroomRepository classroomRepository;
	private final BuildingRepository buildingRepository;
	private final ReservationRepository reservationRepository;

	@Transactional(readOnly = true)
	public ClassroomDetailResponse findClassroomByIdAndDate(Long classroomId, LocalDate date) {

		Classroom classroom = classroomRepository.findClassroomById(classroomId)
			.orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

		List<Reservation> reservationsByClassroomIdsAndDate = reservationRepository.findReservationsByClassroomIdsAndDate(
			List.of(classroomId), date);

		return ClassroomDetailResponse.from(classroom, reservationsByClassroomIdsAndDate);
	}

	@Transactional(readOnly = true)
	public List<ClassroomListResponse> findAllClassroom(ClassroomListRequest request) {

		// 건물 정보 조회
		Building building = buildingRepository.findById(request.getBuildingId())
			.orElseThrow(() -> new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND));

		// 요청된 층이 건물의 층 범위를 벗어나는 경우 예외 발생
		if (request.getFloor() < building.getStartFloor() || request.getFloor() > building.getEndFloor()) {
			throw new BuildingException(BuildingErrorCode.FLOOR_NOT_FOUND);
		}

		List<Classroom> classrooms = classroomRepository.findClassroomByBuildingIdAndFloorAndCapacityGreaterThanEqual(
			request.getBuildingId(), request.getFloor(), request.getHeadcount()
		);

		return classrooms.stream().map(classroom -> {
			// 각 강의실의 예약 정보를 조회
			List<ReservationTime> reservationTimes = reservationRepository.findPendingOrApprovedReservationsByDateAndClassroom(request.getDate(), classroom.getId());

			// ClassroomListResponse 객체 생성
			return ClassroomListResponse.builder()
				.classroomId(classroom.getId())
				.classroomName(classroom.getName())
				.classroomNumber(classroom.getNumber())
				.capacity(classroom.getCapacity())
				.reservationTimes(reservationTimes)
				.build();
		}).collect(Collectors.toList());
	}
}

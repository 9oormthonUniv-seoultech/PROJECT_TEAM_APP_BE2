package com.groomiz.billage.classroom.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.dto.request.ClassroomListRequest;
import com.groomiz.billage.classroom.dto.response.ClassroomListResponse;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.classroom.repository.ClassroomRepository;
import com.groomiz.billage.reservation.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClassroomService {

	private final ClassroomRepository classroomRepository;
	private final ReservationRepository reservationRepository;

	public List<ClassroomListResponse> findAllClassroom(ClassroomListRequest request) {

		List<Classroom> classrooms = classroomRepository.findByBuildingIdAndFloorAndCapacityGreaterThanEqual(
			request.getBuildingId(), request.getFloor(), request.getHeadcount()
		).orElseThrow(() -> new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND));

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

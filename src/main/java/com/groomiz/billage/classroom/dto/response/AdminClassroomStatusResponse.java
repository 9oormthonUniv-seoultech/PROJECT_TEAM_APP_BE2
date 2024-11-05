package com.groomiz.billage.classroom.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.groomiz.billage.building.entity.Building;
import com.groomiz.billage.classroom.dto.ReservationTime;
import com.groomiz.billage.classroom.entity.Classroom;
import com.groomiz.billage.reservation.entity.Reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "강의실 현황 필터링 응답 DTO")
public class AdminClassroomStatusResponse {

	@Schema(description = "날짜", example = "2024-09-04")
	private LocalDate date;

	@Schema(description = "필터링 건물 리스트")
	private List<BuildingResponse> buildings;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@Schema(description = "필터링 건물 정보")
	static class BuildingResponse {

		@Schema(description = "건물 이름", example = "미래관")
		private String buildingName;

		@Schema(description = "필터링 강의실 리스트")
		private List<ClassroomResponse> classrooms;

		@Builder
		public BuildingResponse(String buildingName, List<ClassroomResponse> classrooms) {
			this.buildingName = buildingName;
			this.classrooms = classrooms;
		}

		public static BuildingResponse from(Building building, Map<Long, List<Reservation>> reservationsByClassroom) {
			List<ClassroomResponse> classrooms = building.getClassrooms().stream()
				.map(classroom -> ClassroomResponse.from(classroom, reservationsByClassroom.getOrDefault(classroom.getId(), List.of())))
				.collect(Collectors.toList());

			return BuildingResponse.builder()
				.buildingName(building.getName())
				.classrooms(classrooms)
				.build();
		}

		@Getter
		@NoArgsConstructor(access = AccessLevel.PROTECTED)
		@Schema(description = "필터링 강의실 정보")
		static class ClassroomResponse {

			@Schema(description = "층", example = "1")
			private Long floor;

			@Schema(description = "강의실 번호", example = "101")
			private String classroomNumber;

			@Schema(description = "시간 정보 리스트")
			private List<ReservationTime> time;

			@Schema(description = "인원 수")
			private Integer headcount;

			@Builder
			public ClassroomResponse(Long floor, String classroomNumber, List<ReservationTime> time,
				Integer headcount) {
				this.floor = floor;
				this.classroomNumber = classroomNumber;
				this.time = time;
				this.headcount = headcount;
			}

			public static ClassroomResponse from(Classroom classroom, List<Reservation> reservations) {
				return ClassroomResponse.builder()
					.floor(classroom.getFloor())
					.classroomNumber(classroom.getNumber())
					.time(reservations.stream()
						.map(ReservationTime::from)
						.collect(Collectors.toList()))
					.headcount(classroom.getCapacity())
					.build();
			}
		}

	}

	@Builder
	public AdminClassroomStatusResponse(LocalDate date, List<BuildingResponse> buildings) {
		this.date = date;
		this.buildings = buildings;
	}

	public static AdminClassroomStatusResponse of(LocalDate date, List<Building> buildings, List<Reservation> reservations) {
		// 강의실 단위로 예약 그룹화
		Map<Long, List<Reservation>> reservationsByClassroom = reservations.stream()
			.collect(Collectors.groupingBy(r -> r.getClassroom().getId()));

		// 각 강의실에 해당하는 예약을 전달
		List<BuildingResponse> buildingResponses = buildings.stream()
			.map(building -> BuildingResponse.from(building, reservationsByClassroom))
			.collect(Collectors.toList());

		return AdminClassroomStatusResponse.builder()
			.date(date)
			.buildings(buildingResponses)
			.build();
	}
}
package com.groomiz.billage.classroom.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.groomiz.billage.classroom.dto.ReservationTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의실 현황 필터링 응답 DTO")
public class AdminClassroomStatusResponse {
	@Schema(description = "날짜", example = "2024-09-04")
	private LocalDate date;

	@Schema(description = "건물 리스트")
	private List<BuildingResponse> buildings;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "건물 정보")
class BuildingResponse {

	@Schema(description = "건물 이름", example = "미래관")
	private String buildingName;

	@Schema(description = "강의실 리스트")
	private List<ClassroomResponse> classrooms;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의실 정보")
class ClassroomResponse {

	@Schema(description = "층", example = "1")
	private Long floor;

	@Schema(description = "강의실 번호", example = "101")
	private String classroomNumber;

	@Schema(description = "시간 정보 리스트")
	private List<ReservationTime> time;

	@Schema(description = "인원 수")
	private Integer headcount;
}


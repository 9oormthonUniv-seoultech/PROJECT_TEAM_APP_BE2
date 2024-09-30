package com.groomiz.billage.classroom.dto.request;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의실 현황 필터링 요청 DTO")
public class AdminClassroomStatusRequest {
	@Schema(description = "날짜", example = "2024-09-04")
	private LocalDate date;

	@Schema(description = "건물 ID 리스트", example = "[1, 2]")
	private List<Long> buildings;

	@Schema(description = "층 리스트", example = "[1, 2, 3]")
	private List<Integer> floors;
}

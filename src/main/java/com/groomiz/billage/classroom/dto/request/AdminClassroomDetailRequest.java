package com.groomiz.billage.classroom.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의실 상세 조회 요청 DTO")
public class AdminClassroomDetailRequest {

	@Schema(description = "조회할 날짜", example = "2024-09-03")
	private LocalDate date;

	@Schema(description = "건물 ID", example = "1")
	private Long building;

	@Schema(description = "층", example = "2")
	private Integer floor;

	@Schema(description = "강의실 ID", example = "2")
	private Long classroom;
}

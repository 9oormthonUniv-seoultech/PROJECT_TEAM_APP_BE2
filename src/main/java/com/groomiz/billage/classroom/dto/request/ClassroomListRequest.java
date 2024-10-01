package com.groomiz.billage.classroom.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "강의실 목록 조회 요청 DTO")
public class ClassroomListRequest {

	@Schema(description = "건물 ID", example = "1")
	@NotNull
	private Long buildingId;
	@Schema(description = "층", example = "1")
	@NotNull
	private Long floor;
	@Schema(description = "날짜", example = "2024-08-01")
	@NotNull
	private LocalDate date;
	@Schema(description = "인원", example = "30")
	@NotNull
	private Integer headcount;
}

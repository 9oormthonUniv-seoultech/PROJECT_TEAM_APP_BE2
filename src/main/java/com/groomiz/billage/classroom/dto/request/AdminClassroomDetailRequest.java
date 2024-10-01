package com.groomiz.billage.classroom.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

	@NotNull
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd여야 합니다.")
	@Schema(description = "조회할 날짜", example = "2024-09-03")
	private LocalDate date;

	@NotNull
	@Schema(description = "건물 ID", example = "1")
	private Long building;

	@Schema(description = "층", example = "2")
	private Long floor;

	@Schema(description = "강의실 ID", example = "2")
	private Long classroom;
}

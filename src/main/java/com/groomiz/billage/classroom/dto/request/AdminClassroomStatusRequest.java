package com.groomiz.billage.classroom.dto.request;

import java.time.LocalDate;
import java.util.List;

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
@Schema(description = "강의실 현황 필터링 요청 DTO")
public class AdminClassroomStatusRequest {

	@NotNull
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd여야 합니다.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Schema(description = "날짜", example = "2024-09-04")
	private LocalDate date;

	@Schema(description = "건물 ID 리스트", example = "[1, 2]")
	private List<Long> buildings;

	@Schema(description = "층 리스트", example = "[1, 2, 3]")
	private List<Long> floors;
}

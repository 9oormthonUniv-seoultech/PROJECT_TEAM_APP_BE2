package com.groomiz.billage.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.reservation.entity.ReservationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "예약 요청 DTO")
public class AdminReservationRequest {

	@NotNull
	@Schema(description = "예약 타입", example = "single")
	private ReservationType type;

	@NotNull
	@Schema(description = "건물 ID", example = "1")
	private Long buildingId;

	@NotNull
	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;

	@NotNull
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd여야 합니다.")
	@Schema(description = "시작 날짜", example = "2024-09-03")
	private LocalDate startDate;

	@NotNull
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식은 yyyy-MM-dd여야 합니다.")
	@Schema(description = "종료 날짜", example = "2024-09-05")
	private LocalDate endDate;

	@Schema(description = "요일 리스트", example = "[\"Monday\", \"Wednesday\", \"Friday\"]")
	private List<String> days;

	@NotNull
	@Pattern(regexp = "\\d{2}-\\d{2}", message = "시간 형식은 HH:mm이어야 합니다.")
	@Schema(description = "시작 시간", example = "9:00")
	private LocalTime startTime;

	@NotNull
	@Pattern(regexp = "\\d{2}-\\d{2}", message = "시간 형식은 HH:mm이어야 합니다.")
	@Schema(description = "종료 시간", example = "10:00")
	private LocalTime endTime;
}

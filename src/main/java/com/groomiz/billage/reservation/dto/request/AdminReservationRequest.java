package com.groomiz.billage.reservation.dto.request;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.reservation.entity.ReservationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "예약 요청 DTO")
public class AdminReservationRequest {

	@NotNull
	@Schema(description = "예약 타입", example = "일반")
	private ReservationType type;

	@NotNull
	@Schema(description = "건물 ID", example = "1")
	private Long buildingId;

	@NotNull
	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;

	@NotNull
	@Schema(description = "시작 날짜", example = "2024-09-03")
	private LocalDate startDate;

	@NotNull
	@Schema(description = "종료 날짜", example = "2024-09-05")
	private LocalDate endDate;

	@Schema(description = "요일 리스트", example = "[\"MONDAY\", \"TUESDAY\", \"FRIDAY\"]")
	private List<DayOfWeek> days;

	@NotNull
	@Schema(description = "시작 시간", example = "9:00", type = "string")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime startTime;

	@NotNull
	@Schema(description = "종료 시간", example = "10:00", type = "string")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime endTime;
}

package com.groomiz.billage.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.groomiz.billage.reservation.entity.ReservationType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "예약 요청 DTO")
public class AdminReservationRequest {

	@Schema(description = "예약 타입", example = "single")
	private ReservationType type;

	@Schema(description = "건물 ID", example = "1")
	private Long buildingId;

	@Schema(description = "강의실 ID", example = "1")
	private Long classroomId;

	@Schema(description = "시작 날짜", example = "2024-09-03")
	private LocalDate startDate;

	@Schema(description = "종료 날짜", example = "2024-09-05")
	private LocalDate endDate;

	@Schema(description = "요일 리스트", example = "[\"Monday\", \"Wednesday\", \"Friday\"]")
	private List<String> days;

	@Schema(description = "시작 시간", example = "9")
	private LocalTime startTime;

	@Schema(description = "종료 시간", example = "10")
	private LocalTime endTime;
}

package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "예약 상태별 조회 응답 DTO")
public class AdminReservationStatusListResponse {
	@Schema(description = "예약 상태", example = "pending")
	private String status;

	@Schema(description = "예약 목록")
	private List<ReservationInfo> reservations;

	@Data
	@Schema(description = "예약 정보 DTO")
	public static class ReservationInfo {
		@Schema(description = "예약 ID", example = "1")
		private Long reservationId;

		@Schema(description = "예약 날짜", example = "2024-09-04")
		private LocalDate date;

		@Schema(description = "예약 시작 시간", example = "9:00")
		private LocalTime startTime;

		@Schema(description = "예약 종료 시간", example = "10:00")
		private LocalTime endTime;

		@Schema(description = "예약 인원", example = "10")
		private Integer headcount;

		@Schema(description = "건물 이름", example = "어의관")
		private String buildingName;

		@Schema(description = "층", example = "10")
		private Long floor;

		@Schema(description = "강의실 이름", example = "실험실")
		private String classroomName;

		@Schema(description = "학생 이름", example = "홍길동")
		private String memberName;

		@Schema(description = "학생 ID", example = "20201234")
		private String studentId;
	}
}

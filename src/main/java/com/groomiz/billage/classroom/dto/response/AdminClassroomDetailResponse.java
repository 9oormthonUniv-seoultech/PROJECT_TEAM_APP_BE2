package com.groomiz.billage.classroom.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "강의실 상세 조회 응답 DTO")
public class AdminClassroomDetailResponse {

	@Schema(description = "조회 날짜", example = "2024-09-04")
	private String date;

	@Schema(description = "건물 이름", example = "어의관")
	private String buildingName;

	@Schema(description = "층", example = "3")
	private int floor;

	@Schema(description = "강의실 이름", example = "실험실")
	private String classroomName;

	@Schema(description = "예약 정보 목록")
	private List<ReservationDetail> reservations;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@Schema(description = "예약 상세 정보")
	public static class ReservationDetail {

		@Schema(description = "예약 ID", example = "1")
		private Long reservationId;

		@Schema(description = "예약 날짜", example = "2024-09-04")
		private LocalDate date;

		@Schema(description = "시작 시간", example = "09:00")
		private LocalTime startTime;

		@Schema(description = "종료 시간", example = "10:00")
		private LocalTime endTime;

		@Schema(description = "인원 수", example = "10")
		private Integer headcount;

		@Schema(description = "예약자 이름", example = "홍길동")
		private String memberName;

		@Schema(description = "예약자 학번", example = "20201234")
		private String studentId;
	}
}

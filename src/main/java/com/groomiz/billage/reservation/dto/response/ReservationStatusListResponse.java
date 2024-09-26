package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.groomiz.billage.reservation.entity.ReservationStatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "예약 현황 목록 조회 응답 DTO")
public class ReservationStatusListResponse {
	@Schema(description = "전체 예약 건수", example = "10")
	private Integer totalReservationCount;
	@Schema(description = "예약 정보 목록")
	private List<ReservationInfo> reservations;

	@Data
	@Schema(description = "예약 정보")
	static class ReservationInfo {
		@Schema(description = "예약 ID", example = "1")
		private Long reservationId;
		@Schema(description = "예약 날짜", example = "2024-08-01")
		private LocalDate applyDate;
		@Schema(description = "예약 시작 시간", example = "9")
		private Integer startTime;
		@Schema(description = "예약 종료 시간", example = "10")
		private Integer endTime;
		@Schema(description = "예약 인원", example = "30")
		private Integer headcount;
		@Schema(description = "강의실 이름", example = "실험실")
		private String classroomName;
		@Schema(description = "강의실 번호", example = "101")
		private String classroomNumber;
		@Schema(description = "예약 상태", example = "예약 거절")
		private ReservationStatusType reservationStatus;
		@Schema(description = "거절 사유", example = "인원 초과")
		private String rejectionReason;
		@Schema(description = "관리자 전화번호", example = "010-1234-5678")
		private String adminPhoneNumber;
	}
}

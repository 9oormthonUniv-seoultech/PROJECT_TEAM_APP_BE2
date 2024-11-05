package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.reservation.entity.Reservation;
import com.groomiz.billage.reservation.entity.ReservationStatus;
import com.groomiz.billage.reservation.entity.ReservationStatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "예약 현황 목록 조회 응답 DTO")
public class ReservationStatusListResponse {
	@Schema(description = "전체 예약 건수", example = "10")
	private Long totalReservations;
	@Schema(description = "전체 페이지 수", example = "3")
	private int totalPages;
	@Schema(description = "예약 정보 목록")
	private List<ReservationInfo> reservations;

	@Getter
	@NoArgsConstructor
	@Schema(description = "예약 정보")
	public static class ReservationInfo {
		@Schema(description = "예약 ID", example = "1")
		private Long reservationId;
		@Schema(description = "예약 날짜", example = "2024-08-01")
		private LocalDate applyDate;
		@Schema(description = "예약 시작 시간", example = "09:00", type = "string")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private LocalTime startTime;
		@Schema(description = "예약 종료 시간", example = "10:00", type = "string")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
		private LocalTime endTime;
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

		public ReservationInfo(ReservationStatus reservationStatus) {
			Reservation reservation = reservationStatus.getReservation();

			this.reservationId = reservation.getId();
			this.applyDate = reservation.getApplyDate();
			this.startTime = reservation.getStartTime();
			this.endTime = reservation.getEndTime();
			this.headcount = reservation.getHeadcount();
			this.classroomName = reservation.getClassroom().getName();
			this.classroomNumber = reservation.getClassroom().getNumber();
			this.reservationStatus = reservationStatus.getStatus();
			this.rejectionReason = reservationStatus.getRejectionReason();
			if (reservationStatus.getAdmin() != null) {
				this.adminPhoneNumber = reservationStatus.getAdmin().getPhoneNumber();
			}
		}
	}

	@Builder
	public ReservationStatusListResponse(Long totalReservations, int totalPages,
		List<ReservationInfo> reservations) {
		this.totalReservations = totalReservations;
		this.totalPages = totalPages;
		this.reservations = reservations;
	}
}

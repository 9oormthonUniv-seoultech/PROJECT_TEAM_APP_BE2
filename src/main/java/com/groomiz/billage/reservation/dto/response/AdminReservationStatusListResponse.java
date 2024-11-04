package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.groomiz.billage.reservation.entity.ReservationStatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "예약 상태별 조회 응답 DTO")
public class AdminReservationStatusListResponse {
	@Schema(description = "예약 상태", example = "예약 대기")
	private ReservationStatusType status;

	@Schema(description = "예약 목록")
	private List<ReservationInfo> reservations;

	@Getter
	@NoArgsConstructor
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
		private String studentName;

		@Schema(description = "학번", example = "20201234")
		private String studentNumber;

		@Builder
		public ReservationInfo(Long reservationId, LocalDate date, LocalTime startTime, LocalTime endTime,
			Integer headcount,
			String buildingName, Long floor, String classroomName, String studentName, String studentNumber) {
			this.reservationId = reservationId;
			this.date = date;
			this.startTime = startTime;
			this.endTime = endTime;
			this.headcount = headcount;
			this.buildingName = buildingName;
			this.floor = floor;
			this.classroomName = classroomName;
			this.studentName = studentName;
			this.studentNumber = studentNumber;
		}

		// public static ReservationInfo from(Reservation reservation) {
		// 	return ReservationInfo.builder()
		// 		.reservationId(reservation.getId())
		// 		.date(reservation.getApplyDate())
		// 		.startTime(reservation.getStartTime())
		// 		.endTime(reservation.getEndTime())
		// 		.headcount(reservation.getHeadcount())
		// 		.buildingName(reservation.getClassroom().getBuilding().getName())
		// 		.floor(reservation.getClassroom().getFloor())
		// 		.classroomName(reservation.getClassroom().getName())
		// 		.studentName(reservation.getReservationStatus().getRequester().getUsername())
		// 		.studentNumber(reservation.getReservationStatus().getRequester().getStudentNumber())
		// 		.build();
		//
		// }
	}

	@Builder
	public AdminReservationStatusListResponse(ReservationStatusType status, List<ReservationInfo> reservations) {
		this.status = status;
		this.reservations = reservations;
	}

	// public static AdminReservationStatusListResponse from(ReservationStatusType status, List<Reservation> reservations) {
	// 	return AdminReservationStatusListResponse.builder()
	// 		.status(status)
	// 		.reservations(reservations.stream()
	// 			.map(ReservationInfo::from)
	// 			.toList())
	// 		.build();
	// }
}

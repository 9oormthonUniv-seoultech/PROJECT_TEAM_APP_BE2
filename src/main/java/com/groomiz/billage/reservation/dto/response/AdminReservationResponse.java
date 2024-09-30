package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.groomiz.billage.reservation.entity.ReservationPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "예약 상세 조회 응답 DTO")
public class AdminReservationResponse {

	@Schema(description = "예약 날짜", example = "2024-09-03")
	private LocalDate date;

	@Schema(description = "예약 인원", example = "22")
	private Integer headcount;

	@Schema(description = "건물 이름", example = "어의관")
	private String buildingName;

	@Schema(description = "강의실 이름", example = "202호")
	private String classroomName;

	@Schema(description = "전화번호 목록", example = "[\"010-1234-1234\", \"010-5678-5678\"]")
	private List<String> phoneNumber;

	@Schema(description = "예약 목적", example = "동아리 행사")
	private ReservationPurpose purpose;

	@Schema(description = "예약 시작 시간", example = "9")
	private LocalTime startTime;

	@Schema(description = "예약 종료 시간", example = "10")
	private LocalTime endTime;

	@Schema(description = "비고 내용", example = "비고 내용입니다.")
	private String contents;
}

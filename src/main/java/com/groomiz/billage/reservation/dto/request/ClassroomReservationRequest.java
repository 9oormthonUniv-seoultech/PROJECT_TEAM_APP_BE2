package com.groomiz.billage.reservation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.member.valid.ValidPhoneNumber;
import com.groomiz.billage.reservation.entity.ReservationPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "강의실 예약 요청 DTO")
public class ClassroomReservationRequest {

	@Schema(description = "강의실 ID", example = "1")
	@NotNull
	private Long classroomId;
	@Schema(description = "예약자 추가 전화번호", example = "010-1234-5678")
	@ValidPhoneNumber
	private String phoneNumber;
	@Schema(description = "예약 날짜", example = "2024-08-01")
	@NotNull
	private LocalDate applyDate;
	@Schema(description = "예약 시작 시간", example = "09:00", type = "string")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@NotNull
	private LocalTime startTime;
	@Schema(description = "예약 종료 시간", example = "10:00", type = "string")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	@NotNull
	private LocalTime endTime;
	@Schema(description = "예약 인원", example = "30")
	@NotNull
	private Integer headcount;
	@Schema(description = "예약 사유", example = "단과대 행사")
	@NotNull
	private ReservationPurpose purpose;
	@Schema(description = "비고", example = "공대인의 밤 행사입니다.")
	private String contents;

	@Builder
	public ClassroomReservationRequest(Long classroomId, String phoneNumber, LocalDate applyDate, LocalTime startTime,
		LocalTime endTime, Integer headcount, ReservationPurpose purpose, String contents) {
		this.classroomId = classroomId;
		this.phoneNumber = phoneNumber;
		this.applyDate = applyDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.headcount = headcount;
		this.purpose = purpose;
		this.contents = contents;
	}
}

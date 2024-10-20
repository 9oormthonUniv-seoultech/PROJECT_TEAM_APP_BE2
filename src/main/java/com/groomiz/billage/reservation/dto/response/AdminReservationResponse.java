package com.groomiz.billage.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.reservation.entity.ReservationPurpose;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
	private List<String> phoneNumbers;

	@Schema(description = "예약 목적", example = "동아리 행사")
	private ReservationPurpose purpose;

	@Schema(description = "예약 시작 시간", example = "09:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime startTime;

	@Schema(description = "예약 종료 시간", example = "10:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime endTime;

	@Schema(description = "비고 내용", example = "비고 내용입니다.")
	private String contents;

	@Builder
	public AdminReservationResponse(LocalDate date, Integer headcount, String buildingName, String classroomName,
		String phoneNumber1, String phoneNumber2, ReservationPurpose purpose, LocalTime startTime, LocalTime endTime, String contents) {
		this.date = date;
		this.headcount = headcount;
		this.buildingName = buildingName;
		this.classroomName = classroomName;
		this.phoneNumbers = Stream.of(phoneNumber1, phoneNumber2)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
		this.purpose = purpose;
		this.startTime = startTime;
		this.endTime = endTime;
		this.contents = contents;
	}
}

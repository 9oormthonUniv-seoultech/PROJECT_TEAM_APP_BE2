package com.groomiz.billage.classroom.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.groomiz.billage.reservation.entity.Reservation;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationTime {

	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	@Builder
	public ReservationTime(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public static ReservationTime from(Reservation reservation) {
		return ReservationTime.builder()
			.startTime(reservation.getStartTime())
			.endTime(reservation.getEndTime())
			.build();
	}
}

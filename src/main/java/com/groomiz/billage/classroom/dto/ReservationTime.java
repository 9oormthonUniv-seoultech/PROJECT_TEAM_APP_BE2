package com.groomiz.billage.classroom.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
public class ReservationTime {

	@JsonFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@JsonFormat(pattern = "HH:mm")
	private LocalTime endTime;

	public ReservationTime(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
}

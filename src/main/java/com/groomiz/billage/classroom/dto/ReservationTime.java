package com.groomiz.billage.classroom.dto;

import java.time.LocalTime;

import lombok.Getter;

@Getter
public class ReservationTime {

	private LocalTime startTime;
	private LocalTime endTime;

	public ReservationTime(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
}

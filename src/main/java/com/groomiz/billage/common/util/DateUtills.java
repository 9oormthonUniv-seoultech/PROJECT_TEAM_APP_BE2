package com.groomiz.billage.common.util;

import java.time.LocalTime;

public class DateUtills {

	public static boolean isTimeOverlapping(LocalTime startTime1, LocalTime endTime1, LocalTime startTime2,
		LocalTime endTime2) {
		return startTime1.isBefore(endTime2) && startTime2.isBefore(endTime1);
	}
}

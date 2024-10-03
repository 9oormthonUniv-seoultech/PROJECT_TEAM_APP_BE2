package com.groomiz.billage.reservation.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

public class AdminReservationException extends GlobalCodeException {
	public AdminReservationException(AdminReservationErrorCode errorCode) {
		super(errorCode);
	}
}
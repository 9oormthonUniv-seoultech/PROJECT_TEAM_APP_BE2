package com.groomiz.billage.reservation.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

public class ReservationException extends GlobalCodeException {
	public ReservationException(ReservationErrorCode errorCode) {
		super(errorCode);
	}
}

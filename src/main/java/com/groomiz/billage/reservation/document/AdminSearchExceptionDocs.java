package com.groomiz.billage.reservation.document;

import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.AdminReservationErrorCode;
import com.groomiz.billage.reservation.exception.AdminReservationException;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class AdminSearchExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError("예약 아이디가 존재하지 않는 경우 발생하는 오류입니다.")
	public GlobalCodeException 존재하지_않는_예약 = new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);

}

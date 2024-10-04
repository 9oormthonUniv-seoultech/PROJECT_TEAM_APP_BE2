package com.groomiz.billage.reservation.document;

import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class AdminApproveRejectExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError("예약이 존재하지 않는 경우 발생합니다.(예약 아이디가 없는 경우)")
	public GlobalCodeException 예약_없음 = new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);

	@ExplainError("이미 삭제된 예약인 경우에 발생합니다.")
	public GlobalCodeException 이미_삭제된_예약 = new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_DELETED);

	@ExplainError("예약이 이미 거절된 경우 발생합니다.")
	public GlobalCodeException 예약_거절_오류 = new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);

}

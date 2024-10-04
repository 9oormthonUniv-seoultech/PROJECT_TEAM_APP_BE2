package com.groomiz.billage.reservation.document;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.AdminReservationErrorCode;
import com.groomiz.billage.reservation.exception.AdminReservationException;

public class AdminbyStatusExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError("status 값이 유효하지 않을 때 발생하는 오류입니다.")
	public GlobalCodeException 잘못된_상태_값 = new AdminReservationException(AdminReservationErrorCode.INVALID_STATUS_FORMAT);

	@ExplainError("status 파라미터가 누락되었을 때 발생하는 오류입니다.")
	public GlobalCodeException 상태_파라미터_누락 = new AdminReservationException(AdminReservationErrorCode.MISSING_STATUS_PARAMETER);

}

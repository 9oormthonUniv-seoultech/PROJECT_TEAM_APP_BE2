package com.groomiz.billage.reservation.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class ReservationCancelExceptionDocs implements SwaggerExampleExceptions {
	@ExplainError
	public GlobalCodeException 토큰_만료 = new AuthException(AuthErrorCode.TOKEN_EXPIRED);

	@ExplainError
	public GlobalCodeException 토큰_없음 = new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 토큰_유효하지_않음 = new AuthException(AuthErrorCode.INVALID_TOKEN);

	@ExplainError("예약이 존재하지 않는 경우 발생합니다.(예약 아이디가 없는 경우)")
	public GlobalCodeException 예약_없음 = new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);

	@ExplainError("이미 삭제 된 예약인 경우에 발생합니다.")
	public GlobalCodeException 이미_삭제된_예약 = new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_DELETED);

	@ExplainError("예약이 이미 거절된 경우 발생하는 오류입니다.")
	public GlobalCodeException 예약_거절_오류 = new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_REJECTED);

	@ExplainError("이미 예약 기간이 지난 경우 발생하는 오류입니다.")
	public GlobalCodeException 예약_만료_오류 = new ReservationException(ReservationErrorCode.RESERVATION_EXPIRED);

	@ExplainError("유저와 강의실 예약자가 다른 경우 발생하는 오류입니다.")
	public GlobalCodeException 유저_예약자_불일치 = new ReservationException(ReservationErrorCode.USER_RESERVATION_MISMATCH);
}



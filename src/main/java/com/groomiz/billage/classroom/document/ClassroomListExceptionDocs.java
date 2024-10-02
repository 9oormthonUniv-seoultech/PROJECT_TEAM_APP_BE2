package com.groomiz.billage.classroom.document;

import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class ClassroomListExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 예약_날짜_과거 = new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);

	@ExplainError
	public GlobalCodeException 예약_날짜_한달_이후 = new ReservationException(ReservationErrorCode.FUTURE_DATE_LIMIT_EXCEEDED);

	@ExplainError
	public GlobalCodeException 층_건물_불일치 = new BuildingException(
		BuildingErrorCode.FLOOR_BUILDING_MISMATCH);

	@ExplainError
	public GlobalCodeException 건물_존재하지_않음 = new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 층_존재하지_않음 = new BuildingException(BuildingErrorCode.FLOOR_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 인원_음수 = new ReservationException(ReservationErrorCode.NEGATIVE_PARTICIPANTS);

	@ExplainError
	public GlobalCodeException 최대_인원_초과 = new ReservationException(ReservationErrorCode.MAXIMUM_PARTICIPANTS_EXCEEDED);
}

package com.groomiz.billage.classroom.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.building.exception.BuildingErrorCode;
import com.groomiz.billage.building.exception.BuildingException;
import com.groomiz.billage.classroom.exception.ClassroomErrorCode;
import com.groomiz.billage.classroom.exception.ClassroomException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.reservation.exception.AdminReservationErrorCode;
import com.groomiz.billage.reservation.exception.AdminReservationException;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

@ExceptionDoc
public class AdminClassroomSearchExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError("존재하지 않는 건물일 경우 발생하는 오류입니다.")
	public GlobalCodeException 존재하지_않는_건물 = new BuildingException(BuildingErrorCode.BUILDING_NOT_FOUND);

	@ExplainError("존재하지 않는 층일 경우 발생하는 오류입니다.")
	public GlobalCodeException 존재하지_않는_층 = new BuildingException(BuildingErrorCode.FLOOR_NOT_FOUND);

	@ExplainError("존재하지 않는 강의실일 경우 발생하는 오류입니다.")
	public GlobalCodeException 존재하지_않는_강의실 = new ClassroomException(ClassroomErrorCode.CLASSROOM_NOT_FOUND);

	@ExplainError("강의실 아이디가 건물 아이디에 매칭되지 않는 경우 발생하는 오류입니다.")
	public GlobalCodeException 건물에_없는_강의실 = new ClassroomException(ClassroomErrorCode.CLASSROOM_BUILDING_MISMATCH);

	@ExplainError("예약 날짜가 과거인 경우 발생하는 오류입니다.")
	public GlobalCodeException 과거_날짜_예약 = new ReservationException(ReservationErrorCode.PAST_DATE_RESERVATION);

	@ExplainError("예약 날짜가 한 달 이후인 경우 발생하는 오류입니다.")
	public GlobalCodeException 한달_이후_예약 = new ReservationException(ReservationErrorCode.FUTURE_DATE_LIMIT_EXCEEDED);

}

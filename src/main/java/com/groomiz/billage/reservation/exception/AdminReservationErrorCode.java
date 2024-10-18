package com.groomiz.billage.reservation.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdminReservationErrorCode implements BaseErrorCode {

	@ExplainError("예약 타입이 유효하지 않을 때 발생하는 오류입니다.")
	INVALID_STATUS_FORMAT(BAD_REQUEST, "ADMIN_RESERVATION_400_1", "예약 상태 값이 올바르지 않습니다. 허용되는 값은 '대기', '승인', '거절'입니다."),

	@ExplainError("예약 타입 파라미터가 누락되었을 때 발생하는 오류입니다.")
	MISSING_STATUS_PARAMETER(BAD_REQUEST, "ADMIN_RESERVATION_400_2", "예약 상태 파라미터가 필요합니다."),

	@ExplainError("예약 유형 값이 올바르지 않을 때 발생하는 오류입니다.")
	INVALID_RESERVATION_TYPE(BAD_REQUEST, "ADMIN_RESERVATION_400_3", "예약 유형 값이 올바르지 않습니다. 허용되는 값은 '일반', '기간', '반복'입니다."),

	@ExplainError("예약 유형 값이 누락되었을 때 발생하는 오류입니다.")
	MISSING_RESERVATION_TYPE(BAD_REQUEST, "ADMIN_RESERVATION_400_4", "예약 유형 값이 올바르지 않습니다. 허용되는 값은 '일반', '기간', '반복'입니다."),

	@ExplainError("이미 예약 대기 상태 혹은 승인된 강의실인 경우 발생하는 오류입니다.")
	DUPLICATE_RESERVATION(BAD_REQUEST, "ADMIN_RESERVATION_400_5", "이미 예약 대기 상태 혹은 승인된 강의실입니다."),

	@ExplainError("예약 그룹이 존재하지 않을 때 발생하는 오류입니다.")
	RESERVATION_GROUP_NOT_FOUND(NOT_FOUND, "ADMIN_RESERVATION_404_1", "예약 그룹이 존재하지 않습니다."),

	@ExplainError("관리자가 관리하는 건물이 존재하지 않을 때 발생하는 오류입니다.")
	BUILDING_ADMIN_NOT_FOUND(NOT_FOUND, "ADMIN_RESERVATION_404_2", "관리자가 관리하는 건물이 존재하지 않습니다.");

	private final Integer status;
	private final String code;
	private final String reason;

	@Override
	public ErrorReason getErrorReason() {
		return ErrorReason.builder().reason(reason).code(code).status(status).build();
	}

	@Override
	public String getExplainError() {
		return this.reason;
	}
}

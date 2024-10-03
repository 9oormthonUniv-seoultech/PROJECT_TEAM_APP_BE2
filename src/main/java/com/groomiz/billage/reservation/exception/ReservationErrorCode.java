package com.groomiz.billage.reservation.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements BaseErrorCode {

	@ExplainError("예약 날짜가 과거인 경우 발생하는 오류입니다.")
	PAST_DATE_RESERVATION(BAD_REQUEST, "RESERVATION_400_1", "예약 날짜가 과거일 수 없습니다."),

	@ExplainError("예약 날짜가 한달 이후인 경우 발생하는 오류입니다.")
	FUTURE_DATE_LIMIT_EXCEEDED(BAD_REQUEST, "RESERVATION_400_2", "예약 날짜는 한달 이후일 수 없습니다."),

	@ExplainError("인원이 음수인 경우 발생하는 오류입니다.")
	NEGATIVE_PARTICIPANTS(BAD_REQUEST, "RESERVATION_400_3", "인원이 음수일 수 없습니다."),

	@ExplainError("최대 인원을 초과한 경우 발생하는 오류입니다.")
	MAXIMUM_PARTICIPANTS_EXCEEDED(BAD_REQUEST, "RESERVATION_400_4", "인원이 300명 이상일 수 없습니다."),

	@ExplainError("예약 아이디가 존재하지 않는 경우 발생하는 오류입니다.")
	RESERVATION_NOT_FOUND(NOT_FOUND, "RESERVATION_404_1", "해당 예약이 존재하지 않습니다."),

	@ExplainError("이미 만료된 예약인 경우 발생하는 오류입니다.")
	RESERVATION_EXPIRED(BAD_REQUEST, "RESERVATION_400_4", "이미 만료된 예약입니다."),

	@ExplainError("유저와 강의실 예약자가 다른 경우 발생하는 오류입니다.")
	USER_RESERVATION_MISMATCH(FORBIDDEN, "RESERVATION_403_1", "예약자가 유저와 일치하지 않습니다."),

	@ExplainError("예약이 이미 승인된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_APPROVED(BAD_REQUEST, "RESERVATION_400_5", "이미 승인된 예약입니다."),

	@ExplainError("예약이 이미 거절된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_REJECTED(BAD_REQUEST, "RESERVATION_400_6", "이미 거절된 예약입니다."),

	@ExplainError("예약이 이미 삭제된 경우 발생하는 오류입니다.")
	RESERVATION_ALREADY_DELETED(BAD_REQUEST, "RESERVATION_400_7", "이미 삭제된 예약입니다.");

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
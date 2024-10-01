package com.groomiz.billage.classroom.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassroomErrorCode implements BaseErrorCode {

	@ExplainError("강의실이 없는 경우 발생하는 오류입니다.")
	CLASSROOM_NOT_FOUND(NOT_FOUND, "CLASSROOM_404_1", "해당 강의실이 존재하지 않습니다."),

	@ExplainError("강의실 아이디가 건물 아이디에 매칭되지 않는 경우 발생하는 오류입니다.")
	CLASSROOM_BUILDING_MISMATCH(BAD_REQUEST, "CLASSROOM_400_1", "강의실과 건물 아이디가 일치하지 않습니다."),

	@ExplainError("강의실 최대 인원을 초과한 경우 발생하는 오류입니다.")
	CLASSROOM_CAPACITY_EXCEEDED(BAD_REQUEST, "CLASSROOM_400_2", "강의실 최대 인원을 초과했습니다."),

	@ExplainError("목적이 기타인데 contents가 빈 경우 발생하는 오류입니다.")
	CONTENTS_REQUIRED_FOR_OTHER_PURPOSE(BAD_REQUEST, "CLASSROOM_400_3", "목적이 기타인 경우 내용을 입력해야 합니다."),

	@ExplainError("목적 값이 유효하지 않은 enum 값인 경우 발생하는 오류입니다.")
	INVALID_PURPOSE_ENUM(BAD_REQUEST, "CLASSROOM_400_4", "잘못된 목적 값입니다.");

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
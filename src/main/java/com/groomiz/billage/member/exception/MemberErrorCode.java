package com.groomiz.billage.member.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

	@ExplainError("학번이 8자리가 아닌 경우 발생하는 오류입니다.")
	INVALID_STUDENT_ID(BAD_REQUEST, "MEMBER_400_1", "학번은 8자리여야 합니다."),

	@ExplainError("비밀번호가 형식에 맞지 않는 경우 발생하는 오류입니다.")
	INVALID_PASSWORD_FORMAT(BAD_REQUEST, "MEMBER_400_2", "비밀번호가 형식에 맞지 않습니다."),

	@ExplainError("전화번호 형식이 맞지 않는 경우 발생하는 오류입니다.")
	INVALID_PHONE_NUMBER(BAD_REQUEST, "MEMBER_400_3", "전화번호 형식이 잘못되었습니다."),

	@ExplainError("이미 가입된 학번인 경우 발생하는 오류입니다.")
	STUDENT_ID_ALREADY_REGISTERED(CONFLICT, "MEMBER_409_1", "이미 가입된 학번입니다."),

	@ExplainError("비밀번호가 이전 값과 같은 경우 발생하는 오류입니다.")
	PASSWORD_SAME_AS_OLD(BAD_REQUEST, "MEMBER_400_4", "비밀번호가 이전 값과 동일합니다."),

	@ExplainError("비밀번호가 조건과 맞지 않는 경우 발생하는 오류입니다.")
	PASSWORD_CONSTRAINT_VIOLATION(BAD_REQUEST, "MEMBER_400_5", "비밀번호가 요구 조건에 맞지 않습니다."),

	@ExplainError("단과대 값이 유효하지 않은 enum인 경우 발생하는 오류입니다.")
	INVALID_COLLEGE_ENUM(BAD_REQUEST, "MEMBER_400_6", "잘못된 단과대 값입니다."),

	@ExplainError("학과 값이 유효하지 않은 enum인 경우 발생하는 오류입니다.")
	INVALID_MAJOR_ENUM(BAD_REQUEST, "MEMBER_400_7", "잘못된 학과 값입니다."),

	@ExplainError("회원이 존재하지 않는 경우 발생하는 오류입니다.")
	MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER_404_1", "해당 회원이 존재하지 않습니다.");

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

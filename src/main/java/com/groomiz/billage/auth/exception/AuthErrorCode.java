package com.groomiz.billage.auth.exception;

import static com.groomiz.billage.global.consts.BillageStatic.*;

import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.dto.ErrorReason;
import com.groomiz.billage.global.exception.BaseErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

	@ExplainError("아이디가 틀린 경우 발생하는 오류입니다.")
	INVALID_USER_ID(BAD_REQUEST, "AUTH_400_1", "잘못된 아이디입니다."),

	@ExplainError("비밀번호가 틀린 경우 발생하는 오류입니다.")
	INVALID_PASSWORD(BAD_REQUEST, "AUTH_400_2", "잘못된 비밀번호입니다."),

	@ExplainError("JWT 토큰이 없는 경우 발생하는 오류입니다.")
	TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH_401_1", "인증 토큰이 존재하지 않습니다."),

	@ExplainError("유효하지 않은 JWT 토큰인 경우 발생하는 오류입니다.")
	INVALID_TOKEN(UNAUTHORIZED, "AUTH_401_2", "유효하지 않은 인증 토큰입니다."),

	@ExplainError("JWT 토큰이 만료된 경우 발생하는 오류입니다.")
	TOKEN_EXPIRED(UNAUTHORIZED, "AUTH_401_3", "만료된 인증 토큰입니다."),

	@ExplainError("인증 로직이 실패한 경우 발생하는 오류입니다.")
	AUTHENTICATION_FAIL(UNAUTHORIZED, "AUTH_401_4", "인증에 실패하였습니다."),

	@ExplainError("유효하지 않은 토큰 클레임입니다.")
	INVALID_CLAIMS(BAD_REQUEST, "AUTH_401_4", "유효하지 않은 토큰 클레임입니다."),

	@ExplainError("회원이 존재하지 않는 경우 발생하는 오류입니다.")
	MEMBER_NOT_FOUND(NOT_FOUND, "AUTH_404_1", "해당 회원이 존재하지 않습니다."),

	@ExplainError("이미 탈퇴한 회원인 경우 발생하는 오류입니다.")
	MEMBER_ALREADY_WITHDRAWN(BAD_REQUEST, "AUTH_400_3", "이미 탈퇴한 회원입니다.");

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


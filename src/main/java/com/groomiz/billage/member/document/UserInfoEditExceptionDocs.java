package com.groomiz.billage.member.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

@ExceptionDoc
public class UserInfoEditExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 토큰_만료 = new AuthException(AuthErrorCode.TOKEN_EXPIRED);

	@ExplainError
	public GlobalCodeException 토큰_없음 = new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 토큰_유효하지_않음 = new AuthException(AuthErrorCode.INVALID_TOKEN);

	@ExplainError("전화번호 형식이 맞지 않는 경우 발생하는 오류입니다.")
	public GlobalCodeException 전화번호_형식_오류 = new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);

}

package com.groomiz.billage.auth.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;

@ExceptionDoc
public class JwtExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 토큰_만료 = new AuthException(AuthErrorCode.TOKEN_EXPIRED);

	@ExplainError
	public GlobalCodeException 토큰_없음 = new AuthException(AuthErrorCode.TOKEN_NOT_FOUND);

	@ExplainError
	public GlobalCodeException 토큰_유효하지_않음 = new AuthException(AuthErrorCode.INVALID_TOKEN);
}

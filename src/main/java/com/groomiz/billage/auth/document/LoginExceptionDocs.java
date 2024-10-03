package com.groomiz.billage.auth.document;

import com.groomiz.billage.auth.exception.AuthErrorCode;
import com.groomiz.billage.auth.exception.AuthException;
import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;

@ExceptionDoc
public class LoginExceptionDocs implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 아이디_틀림 = new AuthException(AuthErrorCode.INVALID_USER_ID);
	@ExplainError
	public GlobalCodeException 비밀번호_틀림 = new AuthException(AuthErrorCode.INVALID_PASSWORD);

}

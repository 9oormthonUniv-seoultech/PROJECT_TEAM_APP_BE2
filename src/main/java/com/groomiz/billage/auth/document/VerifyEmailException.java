package com.groomiz.billage.auth.document;

import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

@ExceptionDoc
public class VerifyEmailException implements SwaggerExampleExceptions {

	@ExplainError
	public GlobalCodeException 인증_코드_불일치 = new MemberException(MemberErrorCode.INVALID_VERIFICATION_CODE);

	@ExplainError
	public GlobalCodeException 인증_코드_만료 = new MemberException(MemberErrorCode.EXPIRATION_VERIFICATION_CODE);

}

package com.groomiz.billage.auth.document;

import com.groomiz.billage.global.anotation.ExceptionDoc;
import com.groomiz.billage.global.anotation.ExplainError;
import com.groomiz.billage.global.exception.GlobalCodeException;
import com.groomiz.billage.global.interfaces.SwaggerExampleExceptions;
import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

@ExceptionDoc
public class RegisterExceptionDocs implements SwaggerExampleExceptions {
	@ExplainError
	public GlobalCodeException 학번_중복 = new MemberException(MemberErrorCode.STUDENT_ID_ALREADY_REGISTERED);

	@ExplainError
	public GlobalCodeException 이메일_형식_오류 = new MemberException(MemberErrorCode.INVALID_EMAIL);

	@ExplainError
	public GlobalCodeException 비밀번호_형식_오류 = new MemberException(MemberErrorCode.INVALID_PASSWORD_FORMAT);

	@ExplainError
	public GlobalCodeException 전화번호_형식_오류 = new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);

	@ExplainError
	public GlobalCodeException 단과대_학과_불일치 = new MemberException(MemberErrorCode.INVALID_COLLEGE_AND_MAJOR);

	@ExplainError
	public GlobalCodeException 단과대_미존재 = new MemberException(MemberErrorCode.INVALID_COLLEGE_ENUM);

	@ExplainError
	public GlobalCodeException 학과_미존재 = new MemberException(MemberErrorCode.INVALID_MAJOR_ENUM);

	@ExplainError
	public GlobalCodeException 학번_형식_오류 = new MemberException(MemberErrorCode.INVALID_STUDENT_ID);
}

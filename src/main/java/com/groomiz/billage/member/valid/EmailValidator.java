package com.groomiz.billage.member.valid;

import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

	// 이메일 형식을 검증하는 정규식
	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			// 기본 오류 메시지를 비활성화하고 커스텀 메시지를 설정
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("이메일을 입력해주세요.")
				.addConstraintViolation();
			return false;
		}

		if (!value.matches(EMAIL_REGEX)) {
			throw new MemberException(MemberErrorCode.INVALID_EMAIL);
		}

		return true;  // 이메일 형식이 유효한 경우 true 반환
	}
}
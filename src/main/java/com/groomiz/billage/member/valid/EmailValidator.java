package com.groomiz.billage.member.valid;

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
			context.buildConstraintViolationWithTemplate("EMAIL_NOT_NULL")
				.addConstraintViolation();
			return false;
		}

		if (!value.matches(EMAIL_REGEX)) {
			// 기본 오류 메시지를 비활성화하고 커스텀 메시지를 설정
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("INVALID_EMAIL")
				.addConstraintViolation();
			return false;
		}

		return true;  // 이메일 형식이 유효한 경우 true 반환
	}
}

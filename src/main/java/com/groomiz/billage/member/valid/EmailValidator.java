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
			context.buildConstraintViolationWithTemplate("이메일을 입력해주세요.")
				.addConstraintViolation();
			return false;
		}

		if (!value.matches(EMAIL_REGEX)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("유효하지 않은 이메일 형식입니다. (예: user@example.com)")
				.addConstraintViolation();
			return false;
		}

		return true;  // 이메일 형식이 유효한 경우 true 반환
	}
}
package com.groomiz.billage.member.valid;

import com.groomiz.billage.member.exception.MemberErrorCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	private static final String PHONE_REGEX = "^(010|02)-\\d{3,4}-\\d{4}$";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			// 기본 오류 메시지를 비활성화하고 커스텀 메시지를 설정
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("전화번호를 입력해주세요.")
				.addConstraintViolation();
			return false;
		}

		if (!value.matches(PHONE_REGEX)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678 또는 02-123-5678)")
				.addConstraintViolation();
			return false;
		}

		return true;
	}
}
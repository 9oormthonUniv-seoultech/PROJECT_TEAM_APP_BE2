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
			context.buildConstraintViolationWithTemplate("PHONE_NUMBER_NOT_NULL")
				.addConstraintViolation();
			return false;
		}

		if (!value.matches(PHONE_REGEX)) {
			// 기본 오류 메시지를 비활성화하고 커스텀 메시지를 설정
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("INVALID_PHONE_NUMBER")
				.addConstraintViolation();
			return false;
		}

		return true;  // 전화번호 형식이 유효한 경우 true 반환
	}
}

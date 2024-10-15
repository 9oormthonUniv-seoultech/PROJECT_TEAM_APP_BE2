package com.groomiz.billage.member.valid;

import com.groomiz.billage.member.exception.MemberErrorCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

	private static final String PHONE_REGEX = "^(010|02)-\\d{3,4}-\\d{4}$";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}

		return value.matches(PHONE_REGEX);
	}
}
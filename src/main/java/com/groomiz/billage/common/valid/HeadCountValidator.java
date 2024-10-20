package com.groomiz.billage.common.valid;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class HeadCountValidator implements ConstraintValidator<ValidHeadCount, Integer> {
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true; // null 값은 다른 어노테이션에서 처리하도록 함
		}

		// 음수 값 처리
		if (value < 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("NEGATIVE_PARTICIPANTS")
				.addConstraintViolation();
			return false;
		}

		// 300명 이상의 값 처리
		if (value > 300) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("EXCEED_MAX_PARTICIPANTS")
				.addConstraintViolation();
			return false;
		}

		return true;
	}
}

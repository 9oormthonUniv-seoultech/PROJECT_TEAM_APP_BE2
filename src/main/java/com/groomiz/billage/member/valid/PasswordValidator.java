package com.groomiz.billage.member.valid;

import com.groomiz.billage.member.exception.MemberErrorCode;
import com.groomiz.billage.member.exception.MemberException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	// 영문 8~16자리, 특수문자 1개 이상 포함
	private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()_+=-]).{8,16}$";

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// 비밀번호가 null이거나 빈 문자열인 경우
		if (value == null || value.isEmpty()) {
			// 기본 오류 메시지를 비활성화하고 커스텀 메시지를 설정
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("비밀번호를 입력해주세요.")
				.addConstraintViolation();
			return false;
		}

		// 정규식을 만족하지 않는 경우
		if (!value.matches(PASSWORD_REGEX)) {
			throw new MemberException(MemberErrorCode.INVALID_PASSWORD_FORMAT);
		}

		// 모든 조건을 통과한 경우 true 반환
		return true;
	}
}
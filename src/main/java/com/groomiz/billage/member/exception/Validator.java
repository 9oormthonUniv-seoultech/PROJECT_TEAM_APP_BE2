package com.groomiz.billage.member.exception;

import java.util.regex.Pattern;

public class Validator {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	private static final String PHONE_NUMBER_REGEX = "^(010)-(\\d{4})-(\\d{4})$";
	private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);

	public static void validateEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new IllegalArgumentException("이메일이 비어있습니다.");
		}

		// 이메일 형식이 올바른지 확인
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new MemberException(MemberErrorCode.INVALID_EMAIL);
		}
	}

	public static void validatePhoneNumber(String phoneNumber) {
		if (phoneNumber == null || phoneNumber.isEmpty()) {
			throw new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);
		}

		// 전화번호 형식이 올바른지 확인 (하이픈이 반드시 있어야 함)
		if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
			throw new MemberException(MemberErrorCode.INVALID_PHONE_NUMBER);
		}
	}



}

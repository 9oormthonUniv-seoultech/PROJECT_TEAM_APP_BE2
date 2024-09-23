package com.groomiz.billage.auth.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

import lombok.Getter;

@Getter
public class AuthException extends GlobalCodeException {

	public AuthException(AuthErrorCode errorCode) {
		super(errorCode);
	}
}

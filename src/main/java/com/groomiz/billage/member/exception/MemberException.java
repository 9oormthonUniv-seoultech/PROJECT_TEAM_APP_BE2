package com.groomiz.billage.member.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

public class MemberException extends GlobalCodeException {
	public MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}
}

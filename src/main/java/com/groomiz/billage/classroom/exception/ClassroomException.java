package com.groomiz.billage.classroom.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

public class ClassroomException extends GlobalCodeException {
	public ClassroomException(ClassroomErrorCode errorCode) {
		super(errorCode);
	}
}

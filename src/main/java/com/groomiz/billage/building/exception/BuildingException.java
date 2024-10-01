package com.groomiz.billage.building.exception;

import com.groomiz.billage.global.exception.GlobalCodeException;

public class BuildingException extends GlobalCodeException {
	public BuildingException(BuildingErrorCode errorCode) {
		super(errorCode);
	}
}

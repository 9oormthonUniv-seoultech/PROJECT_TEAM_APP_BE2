package com.groomiz.billage.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorReason {
	private final Integer status;
	private final String code;
	private final String reason;
}


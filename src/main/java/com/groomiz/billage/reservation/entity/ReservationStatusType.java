package com.groomiz.billage.reservation.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.groomiz.billage.reservation.exception.ReservationErrorCode;
import com.groomiz.billage.reservation.exception.ReservationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationStatusType {
	APPROVED("예약 승인"),
	PENDING("예약 대기"),
	REJECTED("예약 거절"),
	STUDENT_CANCELED("학생 취소"),
	ADMIN_CANCELED("관리자 취소");

	private final String name;

	private static final Map<String, ReservationStatusType> NAME_TO_ENUM_MAP = new HashMap<>();

	static {
		for (ReservationStatusType type : ReservationStatusType.values()) {
			NAME_TO_ENUM_MAP.put(type.getName(), type);
		}
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static ReservationStatusType fromName(String name) {
		ReservationStatusType type = NAME_TO_ENUM_MAP.get(name);

		if (type == null) {
			throw new ReservationException(ReservationErrorCode.INVALID_RESERVATION_STATUS_TYPE);
		}

		return type;
	}
}

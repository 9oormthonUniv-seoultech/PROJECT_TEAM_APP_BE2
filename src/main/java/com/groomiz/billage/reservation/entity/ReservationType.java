package com.groomiz.billage.reservation.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.groomiz.billage.reservation.exception.AdminReservationErrorCode;
import com.groomiz.billage.reservation.exception.AdminReservationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationType {
	SINGLE("일반"),
	PERIOD("기간"),
	RECURRING("반복");

	private final String name;

	private static final Map<String, ReservationType> NAME_TO_ENUM_MAP = new HashMap<>();

	static {
		for (ReservationType type : ReservationType.values()) {
			NAME_TO_ENUM_MAP.put(type.name, type);
		}
	}

	@JsonValue
	public String getName() {
		return name;
	}

	@JsonCreator
	public static ReservationType fromName(String name) {
		ReservationType type = NAME_TO_ENUM_MAP.get(name);

		if (type == null) {
			throw new AdminReservationException(AdminReservationErrorCode.INVALID_RESERVATION_TYPE);
		}

		return type;
	}
}

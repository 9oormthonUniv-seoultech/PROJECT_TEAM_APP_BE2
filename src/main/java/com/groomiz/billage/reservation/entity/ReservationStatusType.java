package com.groomiz.billage.reservation.entity;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationStatusType {
	APPROVED("예약 승인"),
	PENDING("예약 대기"),
	REJECTED("예약 거절"),
	STUDENT_CANCLED("학생 취소"),
	ADMIN_CANCLED("관리자 취소");

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
			// TODO: 예외 처리
		}

		return type;
	}
}

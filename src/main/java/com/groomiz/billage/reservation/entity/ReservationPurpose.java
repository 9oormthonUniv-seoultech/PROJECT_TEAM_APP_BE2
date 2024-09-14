package com.groomiz.billage.reservation.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum ReservationPurpose {
    CLUB_EVENT("동아리 행사"),
    DEPARTMENT_EVENT("과 행사"),
    COLLEGE_EVENT("단과대 행사"),
    OTHERS("기타(메모에 작성 필수)");

    private final String name;

    private static final Map<String, ReservationPurpose> NAME_TO_ENUM_MAP = new HashMap<>();

    static {
        for (ReservationPurpose purpose : ReservationPurpose.values()) {
            NAME_TO_ENUM_MAP.put(purpose.name, purpose);
        }
    }

    @JsonValue
    public String getName() { return name; }

    @JsonCreator
    public static ReservationPurpose fromName(String name) {
        ReservationPurpose purpose = NAME_TO_ENUM_MAP.get(name);

        if (purpose == null) {
            // TODO: 예외 처리
        }

        return purpose;
    }
}

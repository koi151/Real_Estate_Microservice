package com.koi151.msproperty.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DaysPostedEnum {
    SEVEN_DAY("7"),
    TEN_DAY("10"),
    FIFTEEN_DAY("15"),
    THIRTY_DAY("30");

    private final String day;

    private static final DaysPostedEnum[] VALUES = values();

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DaysPostedEnum fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid DaysPosted enum value: " + s));
    }
}














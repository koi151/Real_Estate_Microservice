package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DirectionEnum {
    EAST("East"),
    WEST("West"),
    NORTH("North"),
    SOUTH("South"),
    NORTH_EAST("North East"),
    SOUTH_EAST("South East"),
    NORTH_WEST("North West"),
    SOUTH_WEST("South West");

    private final String directionName;
    private static final DirectionEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DirectionEnum fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase())) // case-sensitive comparison
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid DirectionEnum enum value: " + s));
    }

}

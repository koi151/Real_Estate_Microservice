package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DirectionEnum fromString(String status) {
        return status == null ? null : DirectionEnum.valueOf(status.toUpperCase());
    }
}

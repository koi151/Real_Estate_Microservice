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
    public static PaymentScheduleEnum fromString(String status) {
        try {
            return status == null ? null : PaymentScheduleEnum.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid status value");
        }
    }
}

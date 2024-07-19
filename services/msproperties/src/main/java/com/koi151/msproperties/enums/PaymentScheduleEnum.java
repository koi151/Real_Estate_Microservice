package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentScheduleEnum {
    DAILY("Pay daily"),
    WEEKLY("Pay weekly"),
    MONTHLY("Pay monthly"),
    QUARTERLY("Pay quarterly"),
    YEARLY("Pay yearly"),
    OTHER("According to agreement");

    private final String scheduleName;

    private static final PaymentScheduleEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentScheduleEnum fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase())) // case-sensitive comparison
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid PaymentSchedule enum value: " + s));
    }
}

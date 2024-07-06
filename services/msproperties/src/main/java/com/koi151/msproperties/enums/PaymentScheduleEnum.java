package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import customExceptions.InvalidEnumValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentScheduleEnum {
    WEEKLY("Pay weekly"),
    MONTHLY("Pay monthly"),
    QUARTERLY("Pay quarterly"),
    YEARLY("Pay yearly"),
    OTHER("According to agreement");

    private final String scheduleName;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentScheduleEnum fromString(String status) {
        try {
            return status == null ? null : PaymentScheduleEnum.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid status value");
        }
    }
}

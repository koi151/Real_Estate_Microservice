package com.koi151.msproperties.enums;

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
}

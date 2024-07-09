package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PropertyTypeEnum {
    RENT("For rent"),
    SALE("For sale");

    private final String type;

    public boolean isPropertyForRent() {
        return this == RENT;
    }

    public boolean isPropertyForSale() {
        return this == SALE;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentScheduleEnum fromString(String status) {
        try {
            return status == null ? null : PaymentScheduleEnum.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid status value");
        }
    }
}

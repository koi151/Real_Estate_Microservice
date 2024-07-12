package com.koi151.msproperties.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.TreeMap;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {

    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    PENDING ("Pending");

    private final String statusName;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PaymentScheduleEnum fromString(String status) {
        try {
            return status == null ? null : PaymentScheduleEnum.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid status value");
        }
    }
}

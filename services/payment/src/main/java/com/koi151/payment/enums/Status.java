package com.koi151.payment.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.payment.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Status {

    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    PENDING ("Pending");

    private final String statusName;

    private static final Status[] VALUES = values();

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid status enum value: " + s));
    }
}

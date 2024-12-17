package com.koi151.msproperty.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    CREATING("Creating"),
    CANCELED("Canceled"),
    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String statusName;
    private static final StatusEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatusEnum fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid StatusEnum enum value: " + s));
    }
}

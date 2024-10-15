package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PropertyTypeEnum {
    RENT("For rent"),
    SALE("For sale"),
    RENT_SALE("For rent and sale");

    private final String type;
    private static final PropertyTypeEnum[] VALUES = values(); // Cache enum values for performance

    public boolean isPropertyForRent() {
        return this == RENT;
    }

    public boolean isPropertyForSale() {
        return this == SALE;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PropertyTypeEnum fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid PropertyTypeEnum enum value: " + s));
    }
}

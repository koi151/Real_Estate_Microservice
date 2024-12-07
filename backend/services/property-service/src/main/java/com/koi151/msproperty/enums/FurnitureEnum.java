package com.koi151.msproperty.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FurnitureEnum {

    FULL ("Full furniture"),
    SIMPLE ("Simple furniture"),
    NONE("No furniture");

    private final String furnitureName;
    private static final FurnitureEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FurnitureEnum fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid FurnitureStatusEnum value: " + s));
    }
}

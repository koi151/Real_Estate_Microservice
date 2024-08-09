package com.koi151.listing_services.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.listing_services.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PackageType {
    STANDARD("Standard package"),
    PREMIUM("Premium package"),
    EXCLUSIVE("Exclusive package");

    private final String packageName;
    private static final PackageType[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PackageType fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid PackageType enum value: " + s));
    }
}

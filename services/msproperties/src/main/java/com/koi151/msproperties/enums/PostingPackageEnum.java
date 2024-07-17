package com.koi151.msproperties.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperties.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PostingPackageEnum {
    STANDARD("Standard package"),
    PREMIUM("Premium package"),
    EXCLUSIVE("Exclusive package");

    private final String packageName;
    private static final PostingPackageEnum[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PostingPackageEnum fromString(String s) {
        return s == null ? null
                : Arrays.stream(VALUES)
                .filter(value -> value.name().equals(s.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid PostingPackageEnum enum value: " + s));
    }
}

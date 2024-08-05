package com.koi151.listing_services.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.listing_services.customExceptions.InvalidEnumValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String statusName;
    private static final Status[] VALUES = values();

    // indicate that a particular constructor or factory method should be used when deserializing a JSON object into a Java object
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase())) // case-sensitive comparison
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid Status enum value: " + s));
    }
}

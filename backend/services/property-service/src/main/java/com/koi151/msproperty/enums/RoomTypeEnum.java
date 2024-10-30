package com.koi151.msproperty.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.msproperty.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoomTypeEnum {
    BEDROOM("Bedroom"),
    BATHROOM("Bathroom"),
    KITCHEN("Kitchen");

    private final String name;
    private static final RoomTypeEnum[] VALUES = values();

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RoomTypeEnum fromString(String s) {
        return s == null ? null
            : Arrays.stream(VALUES)
            .filter(value -> value.name().equals(s.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new InvalidEnumValueException("Invalid RoomTypeEnum enum value: " + s));
    }
}

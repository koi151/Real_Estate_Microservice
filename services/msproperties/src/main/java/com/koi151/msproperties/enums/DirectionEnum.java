package com.koi151.msproperties.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DirectionEnum {
    EAST("East"),
    WEST("West"),
    NORTH("North"),
    SOUTH("South"),
    NORTH_EAST("North East"),
    SOUTH_EAST("South East"),
    NORTH_WEST("North West"),
    SOUTH_WEST("South West");

    private final String directionName;
}

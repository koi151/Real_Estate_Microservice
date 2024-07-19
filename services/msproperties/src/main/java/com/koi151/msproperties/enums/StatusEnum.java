package com.koi151.msproperties.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {

    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    DRAFT("Draft"),
    PENDING ("Pending"),
    EXPIRED("Expired");

    private final String statusName;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StatusEnum fromString(String status) {
        return status == null ? null : StatusEnum.valueOf(status.toUpperCase());
    }
}

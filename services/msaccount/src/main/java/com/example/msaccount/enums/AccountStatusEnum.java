package com.example.msaccount.enums;

import com.example.msaccount.customExceptions.InvalidEnumValueException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatusEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BANNED("Banned");

    private final String status;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AccountStatusEnum fromString(String status) {
       try {
           return status == null ? null : AccountStatusEnum.valueOf(status.toUpperCase());
       } catch (Exception e) {
           throw new InvalidEnumValueException("Invalid status value");
       }
    }
}


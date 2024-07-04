package com.example.msaccount.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountTypeEnum {
    ADMIN("Admin account"),
    CLIENT("Client account");

    private final String accountType;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AccountTypeEnum fromString(String status) {
        return status == null ? null : AccountTypeEnum.valueOf(status.toUpperCase());
    }
}

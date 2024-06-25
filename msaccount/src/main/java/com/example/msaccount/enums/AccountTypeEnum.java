package com.example.msaccount.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountTypeEnum {
    ADMIN("Admin account"),
    CLIENT("Client account");

    private final String accountType;
}

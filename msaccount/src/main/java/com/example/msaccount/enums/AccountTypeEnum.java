package com.example.msaccount.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountTypeEnum {
    ADMIN("Admin"),
    CLIENT("Client");

    private final String accountType;
}

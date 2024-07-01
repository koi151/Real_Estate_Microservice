package com.example.msaccount.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatusEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BANNED("Banned");

    private final String status;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isBanned() {
        return this == BANNED;
    }
}

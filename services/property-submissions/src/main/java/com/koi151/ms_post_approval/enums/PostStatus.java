package com.koi151.ms_post_approval.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.koi151.ms_post_approval.customExceptions.InvalidEnumValueException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    REJECTED("Rejected");

    private final String status;

    private static final PostStatus[] VALUES = values(); // Cache enum values for performance

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PostStatus fromString(String status) {
        return status == null ? null
            : Arrays.stream(VALUES)
                .filter(value -> value.status.equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidEnumValueException("Invalid status enum value: " + status));
    }
}

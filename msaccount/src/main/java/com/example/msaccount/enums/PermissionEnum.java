package com.example.msaccount.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum {
    PROPERTY_VIEW("Property view"),
    PROPERTY_CREATE("Property creation"),
    PROPERTY_EDIT("Property edit"),
    PROPERTY_DELETE("Property deletion"),

    PROPERTY_CATE_VIEW("Property category view"),
    PROPERTY_CATE_CREATE("Property category creation"),
    PROPERTY_CATE_EDIT("Property category edit"),
    PROPERTY_CATE_DELETE("Property category deletion"),

    ACCOUNT_VIEW("Account view"),
    ACCOUNT_EDIT("Account edit"),
    ACCOUNT_CREATE("Account creation"),
    ACCOUNT_DEL("Account deletion");

    private final String permissionName;
}

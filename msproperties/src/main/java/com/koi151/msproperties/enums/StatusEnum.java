package com.koi151.msproperties.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.TreeMap;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {

    ACTIVE ("Active"),
    INACTIVE ("Inactive"),
    PENDING ("Pending");

    private final String statusName;

    public static Map<String, String> type() {
        Map<String, String> listType = new TreeMap<>();
        for (StatusEnum item : StatusEnum.values()) {
            listType.put(item.toString(), item.getStatusName());
        }

        return listType;
    }
}

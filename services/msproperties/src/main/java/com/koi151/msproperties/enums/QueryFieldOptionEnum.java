package com.koi151.msproperties.enums;

public enum QueryFieldOptionEnum {
    LIKE_STRING,      // String field with LIKE comparison (partial match)
    EXACT_STRING,     // String field with EQUAL comparison (exact match)
    NUMBER,           // Numeric field with EQUAL comparison
    ENUM              // Enum field with EQUAL comparison
}

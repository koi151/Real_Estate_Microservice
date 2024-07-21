package com.koi151.ms_post_approval.enums;

public enum QueryFieldOption {
    EXCLUDE,          // Exclude field from the query
    LIKE_STRING,      // String field with LIKE comparison (partial match)
    EXACT_STRING,     // String field with EQUAL comparison (exact match)
    NUMBER,           // Numeric field with EQUAL comparison
    ENUM              // Enum field with EQUAL comparison
}
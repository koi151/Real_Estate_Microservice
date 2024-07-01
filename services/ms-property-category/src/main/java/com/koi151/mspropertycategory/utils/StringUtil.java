package com.koi151.mspropertycategory.utils;

public class StringUtil {
    public static boolean checkString(String s) { //  .isBlank (JAVA 11) included empty check
        return s != null && !s.isBlank();
    }
}

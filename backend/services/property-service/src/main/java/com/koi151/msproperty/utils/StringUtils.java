package com.koi151.msproperty.utils;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

    public static boolean checkString(String s) { //  .isBlank (JAVA 11) included empty check
        return s != null && !s.isBlank();
    }

    public static String toStringSeparateByRegex(List<String> list, String regex) {
        return list.stream()
                .filter(StringUtils::checkString)
                .collect(Collectors.joining(regex));
    }
}

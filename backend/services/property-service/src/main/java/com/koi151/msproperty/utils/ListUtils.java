package com.koi151.msproperty.utils;

import java.util.Arrays;
import java.util.List;

public class ListUtils {

    public static List<String> splitStringByRegexToList(String s, String regex) {
        if (StringUtils.checkString(s))
            return Arrays.stream(s.split(regex)).toList();
        return null;
    }
}

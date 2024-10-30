package com.koi151.msproperty.utils;

import java.util.Arrays;
import java.util.List;

public class ListUtil {

    public static List<String> splitStringByRegexToList(String s, String regex) {
        if (StringUtil.checkString(s))
            return Arrays.stream(s.split(regex)).toList();
        return null;
    }
}

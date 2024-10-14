package com.koi151.msproperties.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtil {

    public static List<String> splitStringByRegexToList(String s, String regex) {
        if (StringUtil.checkString(s))
            return Arrays.stream(s.split(regex)).toList();
        return null;
    }
}

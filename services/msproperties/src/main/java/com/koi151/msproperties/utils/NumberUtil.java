package com.koi151.msproperties.utils;

public class NumberUtil {
    public static boolean isInteger(String s) {
        try {
            Integer number = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}

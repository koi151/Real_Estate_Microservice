package com.koi151.msproperties.utils;

public class StringUtil {

    public static boolean checkString(String s) { //  .isBlank (JAVA 11) included empty check
        return s != null && !s.isBlank();
    }

//    public static String camelCaseToUnderScore(String camelCaseString) {
//        if (camelCaseString == null || camelCaseString.isEmpty())
//            return null;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(Character.toLowerCase(camelCaseString.charAt(0))); // lowercase 1st char
//
//        for (int i = 1; i < camelCaseString.length(); i++) {
//            char curChar = camelCaseString.charAt(i);
//            if (Character.isUpperCase(curChar) && !Character.isUpperCase(camelCaseString.charAt(i - 1)))
//                sb.append("_");
//            sb.append(Character.toLowerCase(curChar));
//        }
//
//        return sb.toString();
//    }
}

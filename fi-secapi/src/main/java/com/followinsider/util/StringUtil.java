package com.followinsider.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {

    public static String substring(String str, String open, String close) {
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) return str.substring(start + open.length(), end);
        }
        return null;
    }

    public static String trimLeading(String value, char c) {
        return value.replaceFirst("^" + c + "+(?!$)", "");
    }

}

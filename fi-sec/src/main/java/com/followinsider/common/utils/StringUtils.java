package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public String substring(String str, String open, String close) {
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) return str.substring(start + open.length(), end);
        }
        return null;
    }

    public String trimLeading(String value, char c) {
        return value.replaceFirst("^" + c + "+(?!$)", "");
    }

    public String pad(String value, int size, char padding) {
        return pad(value, size, padding, true);
    }

    public String pad(String value, int size, char padding, boolean leading) {
        String format = (leading ? "%" : "%-") + size + "s";
        return String.format(format, value).replace(' ', padding);
    }

}

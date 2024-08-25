package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class StringUtils {

    public String substring(String value, String open, String close) {
        int start = value.indexOf(open);
        if (start == -1) return null;

        int end = value.indexOf(close, start + open.length());
        if (end == -1) return null;

        return value.substring(start + open.length(), end);
    }

    public String overflow(String value, int length) {
        return overflow(value, length, "...");
    }

    public String overflow(String value, int length, String trail) {
        return value.length() > length
                ? value.substring(0, length - trail.length()) + trail
                : value;
    }

    public String trimLeft(String value, char c) {
        return value.substring(countFirst(value, c));
    }

    public String trimRight(String value, char c) {
        return value.substring(0, value.length() - countLast(value, c));
    }

    public String padRight(String value, int length, char c) {
        String padding = generate(length - value.length(), c);
        return value + padding;
    }

    public String padLeft(String value, int length, char c) {
        String padding = generate(length - value.length(), c);
        return padding + value;
    }

    public int countFirst(String value, char c) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != c) {
                return i;
            }
        }
        return value.length();
    }

    public int countLast(String value, char c) {
        for (int i = value.length() - 1; i >= 0; i--) {
            if (value.charAt(i) != c) {
                return value.length() - i - 1;
            }
        }
        return value.length();
    }

    public String generate(int size, char c) {
        if (size <= 0) return "";
        char[] chars = new char[size];
        Arrays.fill(chars, c);
        return new String(chars);
    }

}

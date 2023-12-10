package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public static String overflow(String text, int length) {
        return text.length() > length
                ? text.substring(0, length - 3) + "..."
                : text;
    }

}

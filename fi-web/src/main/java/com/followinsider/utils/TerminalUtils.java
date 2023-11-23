package com.followinsider.utils;

import com.followinsider.common.Color;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class TerminalUtils {

    private static final Map<Color, String> COLOR_MAP = Map.of(
            Color.RED, "\033[31m",
            Color.GREEN, "\033[32m"
    );

    public static String color(String value, Color color) {
        return COLOR_MAP.get(color) + value + "\033[0m";
    }

}

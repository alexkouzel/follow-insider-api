package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

@UtilityClass
public class StringUtils {

    public static void readByLine(InputStream stream, Consumer<String> consumer) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        }
    }

    public static String handleOverflow(String text, int length) {
        return text.length() > length
                ? text.substring(0, length - 3) + "..."
                : text;
    }

}

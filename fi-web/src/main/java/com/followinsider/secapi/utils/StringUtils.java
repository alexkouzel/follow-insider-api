package com.followinsider.secapi.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;

@UtilityClass
public class StringUtils {

    public static List<String> substringMany(String str, String open, String close) {
        List<String> values = new ArrayList<>();
        int start = str.indexOf(open);
        while (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end == -1) break;
            values.add(str.substring(start + open.length(), end));
            start = str.indexOf(open, end + close.length());
        }
        return values;
    }

    public static String substring(String str, String open, String close) {
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) return str.substring(start + open.length(), end);
        }
        return null;
    }

    public static void readByLine(BufferedReader reader,
                                  BiConsumer<String, Integer> consumer) throws IOException {
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            consumer.accept(line, index);
            index++;
        }
    }

    public static String trimLeading(String value, char c) {
        return value.replaceFirst("^" + c + "+(?!$)", "");
    }

    public static String stringify(double value) {
        return NumberFormat.getNumberInstance().format(value);
    }

    public static String stringify(Date date) {
        return new SimpleDateFormat("MMM d, yyyy").format(date);
    }

}

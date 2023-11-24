package com.followinsider.util;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@UtilityClass
public class DateUtil {

    public static Date parse(String value, String pattern) throws ParseException {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.parse(value);
    }

    public static Optional<Date> tryParse(String value, String pattern) {
        try {
            return Optional.ofNullable(parse(value, pattern));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

}

package com.followinsider.common.utils;

import com.followinsider.common.entities.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@UtilityClass
public class DateUtils {

    public static Tuple2<Date, Date> getTimeSpan(Set<Date> dates) {
        Date minDate = Collections.min(dates);
        Date maxDate = Collections.max(dates);
        return new Tuple2<>(minDate, maxDate);
    }

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

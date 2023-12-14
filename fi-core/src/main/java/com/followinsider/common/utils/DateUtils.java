package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtils {

    public Date[] getMinMax(Set<Date> dates) {
        Date minDate = Collections.min(dates);
        Date maxDate = Collections.max(dates);
        return new Date[]{minDate, maxDate};
    }

    public Optional<Date> tryParse(String value, String pattern) {
        try {
            Date date = parse(value, pattern);
            return Optional.of(date);

        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    public Date parse(String value, String pattern) throws ParseException {
        DateFormat format = getFormat(pattern);
        return format.parse(value);
    }

    public String format(Date date, String pattern) {
        DateFormat format = getFormat(pattern);
        return format.format(date);
    }

    private DateFormat getFormat(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}

package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@UtilityClass
public class DateUtils {

    public static Date[] getTimeRange(Set<Date> dates) {
        Date minDate = Collections.min(dates);
        Date maxDate = Collections.max(dates);
        return new Date[]{minDate, maxDate};
    }

    public static String formatDate(Date date, String pattern) {
        return getFormat(pattern).format(date);
    }

    public static String[] formatDates(Date[] dates, String pattern) {
        String[] result = new String[dates.length];
        DateFormat format = getFormat(pattern);
        for (int i = 0; i < dates.length; i++) {
            result[i] = format.format(dates[i]);
        }
        return result;
    }

    private static DateFormat getFormat(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}

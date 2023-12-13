package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DateUtils {

    public static int daysPassed(Date date) {
        return daysBetween(date, new Date());
    }

    public static int daysBetween(Date date1, Date date2) {
        long duration = Math.abs(date1.getTime() - date2.getTime());
        return (int) TimeUnit.MILLISECONDS.toDays(duration);
    }

    public Date[] getMinMax(Set<Date> dates) {
        Date minDate = Collections.min(dates);
        Date maxDate = Collections.max(dates);
        return new Date[]{minDate, maxDate};
    }

    public String[] formatDates(Date[] dates, String pattern) {
        String[] result = new String[dates.length];
        DateFormat format = getFormat(pattern);
        for (int i = 0; i < dates.length; i++) {
            result[i] = format.format(dates[i]);
        }
        return result;
    }

    private DateFormat getFormat(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}

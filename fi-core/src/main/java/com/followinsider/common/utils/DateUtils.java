package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@UtilityClass
public class DateUtils {

    /** Gets the minimum and maximum dates from a list of dates. */
    public Date[] getMinMax(Set<Date> dates) {
        Date minDate = Collections.min(dates);
        Date maxDate = Collections.max(dates);
        return new Date[]{minDate, maxDate};
    }

    /** Formats a list of dates using a pattern. */
    public String[] formatDates(Date[] dates, String pattern) {
        String[] result = new String[dates.length];
        DateFormat format = getFormat(pattern);
        for (int i = 0; i < dates.length; i++) {
            result[i] = format.format(dates[i]);
        }
        return result;
    }

    /** Formats a date using a pattern. */
    private DateFormat getFormat(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

}

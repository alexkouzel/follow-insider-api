package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@UtilityClass
public class DateUtils {

    public Optional<LocalDate> tryParse(String value, String pattern) {
        try {
            LocalDate date = parse(value, pattern);
            return Optional.of(date);

        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public LocalDate parse(String value, String pattern) throws DateTimeParseException {
        return LocalDate.parse(value, getFormatter(pattern));
    }

    public String format(LocalDate date, String pattern) {
        return getFormatter(pattern).format(date);
    }

    private DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormatter
                .ofPattern(pattern)
                .withZone(ZoneId.of("UTC"));
    }



}

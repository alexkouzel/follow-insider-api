package com.followinsider.common.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeRangeTest {

    @Test
    public void duration() {
        long days = mockTimeRange().duration(ChronoUnit.DAYS);
        assertEquals(14, days);
    }

    private TimeRange mockTimeRange() {
        LocalDate from = LocalDate.of(1970, 1, 1);
        LocalDate to = LocalDate.of(1970, 1, 15);
        return new TimeRange(from, to);
    }

}

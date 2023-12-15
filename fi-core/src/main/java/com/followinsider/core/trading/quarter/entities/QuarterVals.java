package com.followinsider.core.trading.quarter.entities;

import com.followinsider.common.entities.TimeRange;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public record QuarterVals(int year, int quarter) {

    public QuarterVals(String alias) {
        this(fromAlias(alias));
    }

    public QuarterVals(QuarterVals vals) {
        this(vals.year, vals.quarter);
    }

    public TimeRange range() {
        int fromMonth = (quarter - 1) * 3 + 1;

        LocalDate from = LocalDate.of(year, fromMonth, 1);
        LocalDate to = from.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        return new TimeRange(from, to);
    }

    public static QuarterVals fromAlias(String alias) {
        String[] parts = alias.split("Q");

        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);

        return new QuarterVals(year, quarter);
    }

    public String getAlias() {
        return year + "Q" + quarter;
    }

}

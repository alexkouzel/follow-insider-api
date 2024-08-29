package com.followinsider.modules.trading.fiscalquarter.models;

import com.followinsider.common.models.TimeRange;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public record FiscalQuarterVals(int year, int quarter) {

    public FiscalQuarterVals(String alias) {
        this(fromAlias(alias));
    }

    public FiscalQuarterVals(FiscalQuarterVals vals) {
        this(vals.year, vals.quarter);
    }

    public TimeRange range() {
        int fromMonth = (quarter - 1) * 3 + 1;

        LocalDate from = LocalDate.of(year, fromMonth, 1);
        LocalDate to = from.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());

        return new TimeRange(from, to);
    }

    public static FiscalQuarterVals fromAlias(String alias) {
        String[] parts = alias.split("Q");

        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);

        return new FiscalQuarterVals(year, quarter);
    }

    public String toAlias() {
        return year + "Q" + quarter;
    }

}

package com.followinsider.core.trading.quarter.entities;

public record QuarterVals(int year, int quarter) {

    public QuarterVals(String alias) {
        this(fromAlias(alias));
    }

    public QuarterVals(QuarterVals vals) {
        this(vals.year, vals.quarter);
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

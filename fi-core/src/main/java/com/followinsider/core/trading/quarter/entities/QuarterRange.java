package com.followinsider.core.trading.quarter.entities;

import java.util.ArrayList;
import java.util.List;

public record QuarterRange(QuarterVals from, QuarterVals to) {

    public QuarterRange(int fromYear, int fromQuarter, int toYear, int toQuarter) {
        this(new QuarterVals(fromYear, fromQuarter), new QuarterVals(toYear, toQuarter));
    }

    public QuarterRange(String from, String to) {
        this(new QuarterVals(from), new QuarterVals(to));
    }

    public List<QuarterVals> generate() {
        List<QuarterVals> quarters = new ArrayList<>();

        for (int year = from.year(); year <= to.year(); year++) {
            int q1 = (year == from.year()) ? from.quarter() : 1;
            int q2 = (year == to.year()) ? to.quarter() : 4;

            for (int quarter = q1; quarter <= q2; quarter++) {
                quarters.add(new QuarterVals(year, quarter));
            }
        }
        return quarters;
    }

}

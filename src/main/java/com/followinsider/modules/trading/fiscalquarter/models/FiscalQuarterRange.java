package com.followinsider.modules.trading.fiscalquarter.models;

import java.util.ArrayList;
import java.util.List;

public record FiscalQuarterRange(FiscalQuarterVals from, FiscalQuarterVals to) {

    public List<FiscalQuarterVals> generate() {
        List<FiscalQuarterVals> result = new ArrayList<>();

        for (int year = from.year(); year <= to.year(); year++) {
            int q1 = (year == from.year()) ? from.quarter() : 1;
            int q2 = (year == to.year()) ? to.quarter() : 4;

            for (int quarter = q1; quarter <= q2; quarter++) {
                result.add(new FiscalQuarterVals(year, quarter));
            }
        }
        return result;
    }

}

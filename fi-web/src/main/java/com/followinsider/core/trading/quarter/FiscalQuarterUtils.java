package com.followinsider.core.trading.quarter;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class FiscalQuarterUtils {

    public static String getAlias(FiscalQuarter fiscalQuarter) {
        return fiscalQuarter.getYearVal() + "Q" + fiscalQuarter.getQuarterVal();
    }

    public static List<int[]> generate(int fromYear, int fromQuarter, int toYear, int toQuarter) {
        List<int[]> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            int fromIdx = year == fromYear ? fromQuarter : 1;
            int toIdx = year == toYear ? toQuarter : 4;

            for (int quarter = fromIdx; quarter <= toIdx; quarter++) {
                quarters.add(new int[]{year, quarter});
            }
        }
        return quarters;
    }

    public static void sortDesc(List<FiscalQuarter> quarters) {
        Comparator<FiscalQuarter> comparator = Comparator
                .comparingInt(FiscalQuarter::getYearVal)
                .thenComparingInt(FiscalQuarter::getQuarterVal);

        quarters.sort(comparator.reversed());
    }

}

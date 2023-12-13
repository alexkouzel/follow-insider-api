package com.followinsider.core.trading.quarter;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class QuarterUtils {

    public String alias(Quarter quarter) {
        return alias(quarter.getYearVal(), quarter.getQuarterVal());
    }

    public String alias(int yearVal, int quarterVal) {
        return yearVal + "Q" + quarterVal;
    }

    public List<Quarter> generate(int fromYear, int toYear) {
        return generate(fromYear, 1, toYear, 4);
    }

    public List<Quarter> generate(int fromYear, int fromQtr, int toYear, int toQtr) {
        List<Quarter> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            int startQtr = (year == fromYear) ? fromQtr : 1;
            int endQtr = (year == toYear) ? toQtr : 4;

            for (int quarter = startQtr; quarter <= endQtr; quarter++) {
                quarters.add(new Quarter(year, quarter));
            }
        }
        return quarters;
    }

    public void sortDesc(List<Quarter> quarters) {
        quarters.sort(comparatorDesc());
    }

    public void sortAsc(List<Quarter> quarters) {
        quarters.sort(comparatorAsc());
    }

    public Comparator<Quarter> comparatorDesc() {
        return comparatorAsc().reversed();
    }

    public Comparator<Quarter> comparatorAsc() {
        return Comparator
                .comparingInt(Quarter::getYearVal)
                .thenComparingInt(Quarter::getQuarterVal);
    }

}

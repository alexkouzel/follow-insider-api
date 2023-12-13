package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.tuples.Tuple2;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class QuarterUtils {

    public Tuple2<Integer, Integer> current() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int quarter = now.get(IsoFields.QUARTER_OF_YEAR);
        return new Tuple2<>(year, quarter);
    }

    public String alias(Quarter quarter) {
        return alias(quarter.getYearVal(), quarter.getQuarterVal());
    }

    public String alias(int yearVal, int quarterVal) {
        return yearVal + "Q" + quarterVal;
    }

    public List<Tuple2<Integer, Integer>> generate(int fromYear, int toYear) {
        return generate(fromYear, 1, toYear, 4);
    }

    public List<Tuple2<Integer, Integer>> generate(
            int fromYear, int fromQuarter, int toYear, int toQuarter) {

        List<Tuple2<Integer, Integer>> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            int fromIdx = year == fromYear ? fromQuarter : 1;
            int toIdx = year == toYear ? toQuarter : 4;

            for (int quarter = fromIdx; quarter <= toIdx; quarter++) {
                quarters.add(new Tuple2<>(year, quarter));
            }
        }
        return quarters;
    }

    public void sortDesc(List<Quarter> quarters) {
        sortAsc(quarters);
        Collections.reverse(quarters);
    }

    public void sortAsc(List<Quarter> quarters) {
        Comparator<Quarter> comparator = Comparator
                .comparingInt(Quarter::getYearVal)
                .thenComparingInt(Quarter::getQuarterVal);

        quarters.sort(comparator);
    }

}

package com.followinsider.core.trading.quarter;

import com.followinsider.common.entities.tuples.Tuple2;
import com.followinsider.common.entities.sync.SyncProgress;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class QuarterUtils {

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

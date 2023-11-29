package com.followinsider.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FiscalUtil {

    public static int[] getCurrentQuarter() {
        LocalDate now = LocalDate.now();
        int quarter = now.get(IsoFields.QUARTER_OF_YEAR);
        int year = now.getYear();
        return new int[]{year, quarter};
    }

    public static List<int[]> generateQuarters(int fromYear, int fromQuarter, int toYear, int toQuarter) {
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

}

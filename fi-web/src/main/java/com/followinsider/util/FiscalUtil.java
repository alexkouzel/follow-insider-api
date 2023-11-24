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
        int qtr = now.get(IsoFields.QUARTER_OF_YEAR);
        int year = now.getYear();
        return new int[]{year, qtr};
    }

    public static List<int[]> generateQuarters(int fromYear, int fromQtr, int toYear, int toQtr) {
        List<int[]> quarters = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            int qtr1 = year == fromYear ? fromQtr : 1;
            int qtr2 = year == toYear ? toQtr : 4;

            for (int qtr = qtr1; qtr <= qtr2; qtr++) {
                quarters.add(new int[]{year, qtr});
            }
        }
        return quarters;
    }

}

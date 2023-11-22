package com.followinsider.secapi.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class CollectionUtils {

    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }
    
    public static List<Integer> generateNums(int from, int to) {
        List<Integer> nums = new ArrayList<>(to - from);
        for (int i = from; i <= to; i++) {
            nums.add(i);
        }
        return nums;
    }

    public static <T> List<List<T>> partition(List<T> list, int groupNum) {
        return divide(list, list.size() / groupNum);
    }

    public static <T> List<List<T>> divide(List<T> list, int groupSize) {
        List<List<T>> groups = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % groupSize == 0) {
                groups.add(new ArrayList<>());
            }
            List<T> lastGroup = groups.get(groups.size() - 1);
            lastGroup.add(list.get(i));
        }
        return groups;
    }

}

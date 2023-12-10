package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class CollectionUtils {

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> List<T> filterClass(List<Object> list, Class<T> c) {
        return list.stream().filter(c::isInstance).map(c::cast).toList();
    }

    public static List<Integer> generateNums(int from, int to) {
        List<Integer> nums = new ArrayList<>(to - from);
        for (int i = from; i <= to; i++) nums.add(i);
        return nums;
    }

    public static <T> List<List<T>> divideToNum(List<T> list, int num) {
        return divideBySize(list, list.size() / num);
    }

    public static <T> List<List<T>> divideBySize(List<T> list, int size) {
        List<List<T>> groups = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % size == 0) {
                groups.add(new ArrayList<>());
            }
            List<T> lastGroup = groups.get(groups.size() - 1);
            lastGroup.add(list.get(i));
        }
        return groups;
    }

}

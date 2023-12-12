package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ListUtils {

    /** Checks if a list is null or empty. */
    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /** Filters and casts elements of a list to a specific type. */
    public <T> List<T> filterType(List<Object> list, Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    /** Generates a list of numbers from {@code from} to {@code to}. */
    public List<Integer> generateNums(int from, int to) {
        return IntStream
                .rangeClosed(from, to).boxed()
                .collect(Collectors.toList());
    }

    /** Divides a list into a {@code num} of lists. */
    public <T> List<List<T>> divideToNum(List<T> list, int num) {
        return divideBySize(list, list.size() / num);
    }

    /** Divides a list into a list of lists of a specific size. */
    public <T> List<List<T>> divideBySize(List<T> list, int size) {
        return new ArrayList<>(list.stream()
                .collect(Collectors.groupingBy(e -> (list.indexOf(e) / size)))
                .values());
    }

}

package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ListUtils {

    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public <T> List<T> filterType(List<Object> list, Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public List<Integer> generateNums(int from, int to) {
        return IntStream
                .rangeClosed(from, to).boxed()
                .collect(Collectors.toList());
    }

    public <T> List<List<T>> divideToNum(List<T> list, int num) {
        return divideBySize(list, list.size() / num);
    }

    public <T> List<List<T>> divideBySize(List<T> list, int size) {
        Collection<List<T>> groups = list.stream()
                .collect(Collectors.groupingBy(e -> (list.indexOf(e) / size)))
                .values();

        return new ArrayList<>(groups);
    }

}

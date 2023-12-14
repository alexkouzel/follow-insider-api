package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;
import org.hibernate.event.spi.PreInsertEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ListUtils {

    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public <T> List<T> filter(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).collect(Collectors.toList());
    }

    public <T> List<T> filterType(List<Object> list, Class<T> type) {
        return list.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public List<Integer> generateNums(int from, int to) {
        return generate(from, to, i -> i);
    }

    public <T> List<T> generate(int from, int to, Function<Integer, T> generator) {
        return IntStream
                .rangeClosed(from, to).boxed()
                .map(generator)
                .collect(Collectors.toList());
    }

    public <T> List<List<T>> divideToNum(List<T> list, int num) {
        return divideBySize(list, list.size() / num);
    }

    public <T> List<List<T>> divideBySize(List<T> list, int size) {
        return list.size() < size
                ? List.of(list)
                : new ArrayList<>(list.stream()
                .collect(Collectors.groupingBy(i -> list.indexOf(i) / size))
                .values());
    }

}

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

    public <T> List<T> generate(int number, Function<Integer, T> generator) {
        return generate(0, number, generator);
    }

    public <T> List<T> generate(int from, int to, Function<Integer, T> generator) {
        List<T> list = new ArrayList<>();
        for (int i = from; i < to; i++) {
            list.add(generator.apply(i));
        }
        return list;
    }

    public <T> List<T> filter(List<T> list, Predicate<T> filter) {
        return list.stream().filter(filter).collect(Collectors.toList());
    }

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
        if (list.size() < size) return List.of(list);
        return new ArrayList<>(list.stream()
                .collect(Collectors.groupingBy(e -> (list.indexOf(e) / size)))
                .values());
    }

}

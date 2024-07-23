package com.followinsider.common.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class ListUtils {

    public <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public <T> List<T> inverse(List<T> list) {
        List<T> result = new ArrayList<>(list.size());
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(list.get(i));
        }
        return result;
    }

    public <T, U> List<T> removeDups(List<T> list, Function<T, U> keyMapper) {
        return new ArrayList<>(list.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity(), (e1, e2) -> e1))
                .values());
    }

    public <T, U> List<U> map(List<T> list, Function<T, U> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
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

    public <T> List<T> generate(int from, int to, Function<Integer, T> generator) {
        return IntStream
                .rangeClosed(from, to).boxed()
                .map(generator)
                .collect(Collectors.toList());
    }

    public List<Integer> generateNums(int from, int to) {
        int diff = to - from;
        return diff >= 0
                ? generate(from, to, i -> i)
                : generate(0, -diff, i -> from - i);
    }

    public <T> List<List<T>> splitByCount(List<T> list, int count) {
        return splitBySize(list, list.size() / count + 1);
    }

    public <T> List<List<T>> splitBySize(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(new ArrayList<>(list.subList(i, Math.min(list.size(), i + size))));
        }
        return result;
    }

}

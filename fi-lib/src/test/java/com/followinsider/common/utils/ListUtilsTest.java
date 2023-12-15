package com.followinsider.common.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListUtilsTest {

    @Test
    public void inverse() {
        List<Integer> nums = ListUtils.generateNums(1, 5);
        assertEquals(List.of(5, 4, 3, 2, 1), ListUtils.inverse(nums));
    }

    @Test
    public void removeDups() {
        List<Integer> nums = List.of(1, 2, 2, 2, 3, 4);
        assertEquals(List.of(1, 2, 3, 4), ListUtils.removeDups(nums, i -> i));
    }

    @Test
    public void generate() {
        List<Integer> twos = ListUtils.generate(1, 10, i -> 2);
        assertEquals(10, twos.size());
        twos.forEach(two -> assertEquals(2, two));
    }

    @Test
    public void generateNums() {
        List<Integer> nums = ListUtils.generateNums(4, 10);
        assertEquals(List.of(4, 5, 6, 7, 8, 9, 10), nums);
    }

    @Test
    public void generateInvNums() {
        List<Integer> nums = ListUtils.generateNums(6, 2);
        assertEquals(List.of(6, 5, 4, 3, 2), nums);
    }
    
    @Test
    public void splitByCount() {
        List<Integer> nums = ListUtils.generateNums(1, 10);
        List<List<Integer>> groups = ListUtils.splitByCount(nums, 4);
        assertEquals(List.of(1, 2, 3), groups.get(0));
        assertEquals(List.of(4, 5, 6), groups.get(1));
        assertEquals(List.of(7, 8, 9), groups.get(2));
        assertEquals(List.of(10), groups.get(3));
    }

    @Test
    public void splitBySize() {
        List<Integer> nums = ListUtils.generateNums(1, 10);
        List<List<Integer>> groups = ListUtils.splitBySize(nums, 4);
        assertEquals(List.of(1, 2, 3, 4), groups.get(0));
        assertEquals(List.of(5, 6, 7, 8), groups.get(1));
        assertEquals(List.of(9, 10), groups.get(2));
    }

}

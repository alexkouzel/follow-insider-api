package com.followinsider.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilTest {

    @Test
    public void testMap() {
        List<Integer> nums = List.of(1, 2, 3);
        assertEquals(List.of(2, 4, 6), CollectionUtil.map(nums, num -> num * 2));
    }

    @Test
    public void testGenerateNums() {
        List<Integer> nums = CollectionUtil.generateNums(4, 10);
        assertEquals(List.of(4, 5, 6, 7, 8, 9, 10), nums);
    }
    
    @Test
    public void testPartition() {
        List<Integer> nums = CollectionUtil.generateNums(1, 8);
        List<List<Integer>> groups = CollectionUtil.divideToNum(nums, 4);
        assertEquals(List.of(1, 2), groups.get(0));
        assertEquals(List.of(3, 4), groups.get(1));
        assertEquals(List.of(5, 6), groups.get(2));
        assertEquals(List.of(7, 8), groups.get(3));
    }

    @Test
    public void testDivide() {
        List<Integer> nums = CollectionUtil.generateNums(1, 7);
        List<List<Integer>> groups = CollectionUtil.divideBySize(nums, 4);
        assertEquals(List.of(1, 2, 3, 4), groups.get(0));
        assertEquals(List.of(5, 6, 7), groups.get(1));
    }

}

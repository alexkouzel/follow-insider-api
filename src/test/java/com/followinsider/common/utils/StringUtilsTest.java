package com.followinsider.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {

    @Test
    public void substring() {
        String result = StringUtils.substring("test <A>test<B> test", "<A>", "<B>");
        assertEquals("test", result);
    }

    @Test
    public void overflow() {
        String result = StringUtils.overflow("test test test", 5);
        assertEquals("te...", result);
    }

    @Test
    public void trimLeft() {
        String result = StringUtils.trimLeft("aaaaaaTEST", 'a');
        assertEquals("TEST", result);
    }

    @Test
    public void trimRight() {
        String result = StringUtils.trimRight("TESTbbbbbb", 'b');
        assertEquals("TEST", result);
    }

    @Test
    public void padLeft() {
        String result = StringUtils.padLeft("TEST", 8, 'c');
        assertEquals("ccccTEST", result);
    }

    @Test
    public void padRight() {
        String result = StringUtils.padRight("", 3, 'a');
        assertEquals("aaa", result);
    }

    @Test
    public void countFirst() {
        int result = StringUtils.countFirst("aaabbb", 'a');
        assertEquals(3, result);
    }

    @Test
    public void countLast() {
        int result = StringUtils.countLast("qwere", 'e');
        assertEquals(1, result);
    }

    @Test
    public void generate() {
        String result = StringUtils.generate(5, 'b');
        assertEquals("bbbbb", result);
    }

}

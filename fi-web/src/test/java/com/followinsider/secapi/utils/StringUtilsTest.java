package com.followinsider.secapi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilsTest {

    @Test
    public void test() {
        String str = "hello<HEADER>test</HEADER>bye";
        String actual = StringUtils.substring(str, "<HEADER>", "</HEADER>");
        assertEquals("test", actual);
    }

}

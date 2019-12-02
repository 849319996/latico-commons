package com.latico.commons.common.util.logging;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class AbstractLoggerTest {

    /**
     *
     */
    @Test
    public void test() {
        String abc = "ajjg{}{}jajg{}{}ag{}";
        System.out.println(Arrays.toString(abc.split("\\{\\}")));
        System.out.println(Arrays.toString(abc.split("\\{\\}", -1)));
    }
}
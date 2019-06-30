package com.latico.commons.common.util.math;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void toLong() {

        System.out.println(NumberUtils.toLongRound(88.8));
        System.out.println(NumberUtils.toLongRound(100.8));
        System.out.println(NumberUtils.toLongRound(101.4));
    }
}
package com.latico.commons.algorithm.string;

import org.junit.Test;

public class CharRandomTest {

    @Test
    public void order() {
        String str = "ab";
        CharRandom.order(str,2);
    }

    @Test
    public void order2() {
        String str = "abc";
        CharRandom.order(str,2);
    }
}
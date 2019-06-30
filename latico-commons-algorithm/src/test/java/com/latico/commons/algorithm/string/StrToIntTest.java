package com.latico.commons.algorithm.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class StrToIntTest {

    @Test
    public void convert() {
        String str = "12357";
        StrToInt.convert(str);
    }
}
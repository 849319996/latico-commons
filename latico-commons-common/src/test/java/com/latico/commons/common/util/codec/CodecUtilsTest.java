package com.latico.commons.common.util.codec;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CodecUtilsTest {

    @Test
    public void shortToByte() {
        short s = (short)0xFFFF;
        System.out.println(Arrays.toString(CodecUtils.shortToByte(s)));
    }
}
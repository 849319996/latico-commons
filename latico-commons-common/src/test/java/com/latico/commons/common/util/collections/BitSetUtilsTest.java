package com.latico.commons.common.util.collections;

import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class BitSetUtilsTest {

    @Test
    public void createBitSet() {
        BitSet bitSet = BitSetUtils.createBitSet(10);

        bitSet.set(1);
        long[] longs = bitSet.toLongArray();
        System.out.println(bitSet.length());
        System.out.println(bitSet.toString());
        System.out.println(ArrayUtils.toString(longs));

        bitSet.set(3, 11);

        System.out.println(bitSet.length());
        System.out.println(bitSet.toString());


    }
}
package com.latico.commons.common.util.collections;

import org.junit.Test;

public class ArrayUtilsTest {

    @Test
    public void test(){
        Integer[] array = new Integer[5] ;
        array[0] = 1;
        array[1] = 2;
        array[4] = 3;
        System.out.println(ArrayUtils.toStringSpecialDelimiter(array, ";"));
    }
}
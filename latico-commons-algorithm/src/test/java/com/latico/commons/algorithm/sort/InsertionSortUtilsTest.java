package com.latico.commons.algorithm.sort;

import org.junit.Test;

import java.util.Arrays;

public class InsertionSortUtilsTest {

    @Test
    public void sort() {
        int[] arr = new int[]{2,1,3,5,7,2,7,0,8,4};
        InsertionSortUtils.sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void sort2() {
        int[] arr = new int[]{2,1,3,5,7,2,7,0,8,4};
        InsertionSortUtils.sort2(arr);
        System.out.println(Arrays.toString(arr));
    }
}
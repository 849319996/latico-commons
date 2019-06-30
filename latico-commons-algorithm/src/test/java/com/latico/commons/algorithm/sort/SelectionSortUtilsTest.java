package com.latico.commons.algorithm.sort;

import org.junit.Test;

import java.util.Arrays;

public class SelectionSortUtilsTest {

    @Test
    public void sort() {
        int[] arr = new int[]{2,1,3,5,7,2,7,0,8,4};
        SelectionSortUtils.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
package com.latico.commons.algorithm.sort;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MergeSortTest {

    @Test
    public void mergeSort() {
    }
    @Test
    public void test(){
        int[] a = { 49, 38, 65, 97, 76, 13, 27, 50 };
        MergeSort.mergeSort(a, 0, a.length-1);
        System.out.println("排好序的数组：" + Arrays.toString(a));
    }
}
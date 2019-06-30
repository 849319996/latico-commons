package com.latico.commons.algorithm.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BubbleSortUtilsTest {

    @Test
    public void sort() {
        int[] arr = new int[]{2,1,3,5,7,2,7,0,8,4};
        BubbleSortUtils.sortArr(arr);
        System.out.println(Arrays.toString(arr));
    }
    @Test
    public void sortComparable(){
        List<ComparableBean> list = new ArrayList<>();
        list.add(new ComparableBean(2));
        list.add(new ComparableBean(1));
        list.add(new ComparableBean(3));
        list.add(new ComparableBean(5));
        list.add(new ComparableBean(7));
        list.add(new ComparableBean(2));
        list.add(new ComparableBean(7));
        list.add(new ComparableBean(0));
        list.add(new ComparableBean(8));
        list.add(new ComparableBean(4));
        BubbleSortUtils.sortComparable(list);
        System.out.println(list);
    }
}
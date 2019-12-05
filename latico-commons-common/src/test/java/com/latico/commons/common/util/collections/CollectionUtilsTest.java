package com.latico.commons.common.util.collections;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class CollectionUtilsTest {

    @Test
    public void isEqualCollection() {

        List<String> list1 = new ArrayList<String>();

        list1.add("abc");
        list1.add("abc1");
        list1.add("abc2");

        List<String> list2 = new ArrayList<String>();

        list2.add("abc1");
        list2.add("abc2");
        list2.add("abc");

        List<String> list3 = new ArrayList<String>();

        list3.add("abc");
        list3.add("abc1");
        list3.add("abc3");
        System.out.println(CollectionUtils.isEqualCollection(list1, list2));
        System.out.println(CollectionUtils.isEqualCollection(list1, list3));
        System.out.println(ArrayUtils.toString(list1.toArray(new String[list3.size()])));
    }

    @Test
    public void isAllEmpty() {

        List<String> list1 = new ArrayList<String>();

        list1.add("abc");
        list1.add("abc1");
        list1.add("abc2");

        List<String> list2 = new ArrayList<String>();

        list2.add("abc1");
        list2.add("abc2");
        list2.add("abc");

        List<String> list3 = new ArrayList<String>();

        // list3.add("abc");
        // list3.add("abc1");
        // list3.add("abc3");
        System.out.println(CollectionUtils.isAllEmpty(list1, list2, list3));
    }

    @Test
    public void isAllNotEmpty() {

        List<String> list1 = new ArrayList<String>();

        list1.add("abc");
        list1.add("abc1");
        list1.add("abc2");

        List<String> list2 = new ArrayList<String>();

        list2.add("abc1");
        list2.add("abc2");
        list2.add("abc");

        List<String> list3 = new ArrayList<String>();
        list3 = null;
        // list3.add("abc");
        // list3.add("abc1");
        // list3.add("abc3");
        System.out.println(CollectionUtils.isAllNotEmpty(list1, list2, list3));
    }

    @Test
    public void toList() {
        String[] arr = new String[]{"jag", "jgaj"};
        System.out.println(CollectionUtils.toList(arr));
    }

    @Test
    public void toArray() {
        List<String> list3 = new ArrayList<String>();

        list3.add("abc");
        list3.add("abc1");
        list3.add("abc3");

        String[] strings = list3.toArray(new String[list3.size()]);
        System.out.println(Arrays.toString(strings));

    }

    @Test
    public void addAll() {

        List<String> list3 = new ArrayList<String>();

        list3.add("abc");
        list3.add("abc1");
        list3.add("abc3");
        CollectionUtils.add(list3, "jajg", "jagj");
        System.out.println(list3);
    }

    @Test
    public void toArrayObj() {
    }

    @Test
    public void toArrayStr() {
        List<String> list3 = new ArrayList<String>();

        list3.add("abc");
        list3.add("abc1");
        list3.add("abc3");

        System.out.println(Arrays.toString(CollectionUtils.toArrayStr(list3)));
    }

    @Test
    public void toArrayInt() {
        List<Integer> list3 = new ArrayList<Integer>();

        list3.add(1);
        list3.add(2);
        list3.add(3);

        System.out.println(Arrays.toString(CollectionUtils.toArrayInt(list3)));
    }

    @Test
    public void toArrayLong() {
    }

    @Test
    public void toArrayDouble() {
        List<Double> list3 = new ArrayList<Double>();

        list3.add(1D);
        list3.add(2D);
        list3.add(3D);

        System.out.println(Arrays.toString(CollectionUtils.toArrayDouble(list3)));
    }

    @Test
    public void toArrayFloat() {
        List<String> list = new CopyOnWriteArrayList<>();

        list.add("abc");
        list.add("abcd");

        System.out.println(list);

        ConcurrentHashMap map = new ConcurrentHashMap();

        ReentrantLock lock = new ReentrantLock();

    }
}
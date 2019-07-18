package com.latico.commons.common.util.collections;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BloomFilterTest {
    @Before
    public void setUp() throws Exception {
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void test(){
        BloomFilter<String> bloomFilter = new BloomFilter<String>(0.2, 5);
        int i = 1;
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        bloomFilter.add("abc" + i++);
        i = 1;
        System.out.println(bloomFilter.contains("abc" + i++));
        System.out.println(bloomFilter.contains("abc" + i++));
        System.out.println(bloomFilter.contains("abc" + i++));
        System.out.println(bloomFilter.contains("abc" + i++));
        System.out.println(bloomFilter.contains("abc" + i++));
        System.out.println("===========================");
        i = 1;
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));
        System.out.println(bloomFilter.contains("bcd" + i++));

        bloomFilter.close();
    }

    /**
     *
     */
    @Test
    public void test1(){
        long l = 1;

        System.out.println(l << 2);
        System.out.println(l << 64);
        System.out.println(l << 65);
    }
}
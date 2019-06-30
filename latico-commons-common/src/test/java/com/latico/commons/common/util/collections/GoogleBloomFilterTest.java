package com.latico.commons.common.util.collections;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GoogleBloomFilterTest {


    @Test
    public void test1() {
        int size = 100000;

//     创建一个预期要装size个数据的整型布隆过滤器,看源码{@link BloomFilter#create(com.google.common.hash.Funnel, long)}可以知道，默认的误判率是0.03
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);
        for (int i = 0; i < size; i++) {

            bloomFilter.put(i);

        }

        long startTime = System.nanoTime(); // 获取开始时间


        //判断这一百万个数中是否包含29999这个数

        if (bloomFilter.mightContain(29999)) {

            System.out.println("命中了");

        }

        long endTime = System.nanoTime();   // 获取结束时间


        System.out.println("程序运行时间： " + (endTime - startTime) + "纳秒");


    }


    @Test
    public void test2() {
        int size = 100000;

//     创建一个预期要装size个数据的整型布隆过滤器,看源码{@link BloomFilter#create(com.google.common.hash.Funnel, long)}可以知道，默认的误判率是0.03
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);

        for (int i = 0; i < size; i++) {

            bloomFilter.put(i);

        }

        List<Integer> list = new ArrayList<Integer>(1000);


        //故意取1000个不在过滤器里的值，看看有多少个会被认为在过滤器里

        for (int i = size + 1000; i < size + 2000; i++) {

            if (bloomFilter.mightContain(i)) {

                list.add(i);

            }

        }

        System.out.println("误判的数量：" + list.size());


    }

}
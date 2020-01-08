package com.latico.commons.common.util.thread.pool;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadPoolTest {

    /**
     *
     */
    @Test
    public void test(){
        long start = System.currentTimeMillis();
        ThreadPool threadPool = new ThreadPool(8, 8, 30, 8 * 2);
        long end = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis() - start);
        threadPool.shutdown();
        System.out.println(System.currentTimeMillis() - end);

    }
}
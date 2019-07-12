package com.latico.commons.common.util.thread.pool;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BaseThreadPoolTest {

    @Test
    public void execute() {
        int maxWorkThreadSize = 10;
        BaseThreadPool<Object> threadPool = new BaseThreadPool<Object>(maxWorkThreadSize);

        //执行没有返回数据的任务
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程任务");
            }
        });

        //安全关闭线程池，会等待任务执行完毕才关闭
        threadPool.shutdown();

        //等待线程池最大等待10s
        threadPool.awaitTerminationWithIntervalCheck(10000, 1000);

    }

    @Test
    public void submit() {
        BaseThreadPool<Object> threadPool = new BaseThreadPool<Object>(10,10,60, TimeUnit.SECONDS, 10000, new ThreadPoolExecutor.CallerRunsPolicy());

        //执行没有返回数据的任务
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程任务");
            }
        });

        //安全关闭线程池，会等待任务执行完毕才关闭
        threadPool.shutdown();

        //等待线程池最大等待10s
        threadPool.awaitTerminationWithIntervalCheck(10000, 1000);

    }

    @Test
    public void getCurrrentThreadPool() {
        BaseThreadPool<Object> threadPool = new BaseThreadPool<Object>(10,10,60, TimeUnit.SECONDS, 10000, new ThreadPoolExecutor.CallerRunsPolicy());

        ThreadPoolExecutor threadPoolExecutor = threadPool.getCurrrentThreadPool();

        BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();

    }
}
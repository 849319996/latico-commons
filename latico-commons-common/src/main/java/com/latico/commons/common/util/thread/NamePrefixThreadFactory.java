package com.latico.commons.common.util.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <PRE>
 *  统一线程名字的线程工厂实现
 *  生产出来的线程统一线程前缀
 *  可以用于：java.util.concurrent.Executors#newSingleThreadScheduledExecutor(java.util.concurrent.ThreadFactory)
 *  让定时器自动生成的线程统一线程名字，打印日志的时候会更好核查跟踪
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-01-27 22:21:12
 * @Version: 1.0
 */
public class NamePrefixThreadFactory implements ThreadFactory {
    /**
     * 线程名称前缀
     */
    private final String threadNamePrefix;
    /**
     * 线程号
     */
    private final AtomicLong threadIndex;

    /**
     * 是否是守护线程
     */
    private final boolean daemon;

    /**
     * @param threadNamePrefix 要生产出线程的前缀
     */
    public NamePrefixThreadFactory(String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    /**
     * @param threadNamePrefix 要生产出线程的前缀
     * @param daemon 是否守护线程
     */
    public NamePrefixThreadFactory(String threadNamePrefix, boolean daemon) {
        this.threadIndex = new AtomicLong(0L);
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, this.threadNamePrefix + this.threadIndex.incrementAndGet());
        thread.setDaemon(this.daemon);
        return thread;
    }
}
package com.latico.commons.timer.jdkscheduled;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-02-06 22:36
 * @Version: 1.0
 */
public class JdkScheduledManager {
    private static volatile JdkScheduledManager instance;

    public static JdkScheduledManager getInstance() {
        if (instance == null) {
            synchronized (JdkScheduledManager.class) {
                if (instance == null) {
                    instance = new JdkScheduledManager();
                }
            }
        }
        return instance;
    }

    private JdkScheduledManager() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    public void scheduleWithFixedDelay(Runnable command,
                                       long initialDelay,
                                       long delay,
                                       TimeUnit unit) {
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}

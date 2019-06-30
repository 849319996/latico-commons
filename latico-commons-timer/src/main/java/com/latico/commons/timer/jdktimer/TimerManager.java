package com.latico.commons.timer.jdktimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-02-06 22:25
 * @Version: 1.0
 */
public class TimerManager {
    private static volatile TimerManager instance;

    public static TimerManager getInstance() {
        if (instance == null) {
            synchronized (TimerManager.class) {
                if (instance == null) {
                    instance = new TimerManager();
                }
            }
        }
        return instance;
    }

    private TimerManager() {
        timer = new Timer();
    }

    private final Timer timer;

    public Timer getTimer() {
        return timer;
    }

    public boolean schedule(TimerTask task, long delay) {
        timer.schedule(task, delay);
        return true;
    }

    public boolean scheduleAtFixedRate(TimerTask task, long delay, long period) {
        timer.scheduleAtFixedRate(task, delay, period);
        return true;
    }

    public void close() {
        timer.cancel();
    }
}

package com.latico.commons.common.util.time;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

/**
 * <PRE>
 * LocalTime 的操作工具
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-10 16:57
 * @version: 1.0
 */
public class LocalTimeUtils {

    /**
     * @return
     */
    public static LocalTime getCurrentLocalTime() {
        return LocalTime.now();
    }

    /**
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static LocalTime getLocalTime(int hour, int minute, int second, int nanoOfSecond) {
        return LocalTime.of(hour, minute, second, nanoOfSecond);
    }

    public static LocalTime getLocalTimeMilliSecond(int hour, int minute, int second, int milliOfSecond) {
        return LocalTime.of(hour, minute, second, milliOfSecond * 1000000);
    }

    public static int getHour(LocalTime localTime) {
        return localTime.getHour();
    }

    public static int getMinute(LocalTime localTime) {
        return localTime.getMinute();
    }

    public static int getSecond(LocalTime localTime) {
        return localTime.getSecond();
    }

    /**
     * 纳秒
     * @param localTime
     * @return
     */
    public static int getNano(LocalTime localTime) {
        return localTime.getNano();
    }

    /**
     * 毫秒
     * @param localTime
     * @return
     */
    public static int getMilliSecond(LocalTime localTime) {
        int nano = localTime.getNano();
        return nano/1000000;
    }
}

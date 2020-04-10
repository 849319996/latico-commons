package com.latico.commons.common.util.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * <PRE>
 * LocalDateTime 的操作工具
 * 获取年月日时分秒，等于LocalDate+LocalTime
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-10 16:58
 * @version: 1.0
 */
public class LocalDateTimeUtils {
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDateTime getLocalDateTime(int year, Month month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond);
        return localDateTime;
    }

    public static LocalDateTime getLocalDateTimeMilliSecond(int year, Month month, int dayOfMonth, int hour, int minute, int second, int milliOfSecond) {
        LocalDateTime localDateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, milliOfSecond * 1000000);
        return localDateTime;
    }

    public static LocalDateTime getLocalDateTime(LocalDate localDate, LocalTime localTime) {
        LocalDateTime localDateTime = localDate.atTime(localTime);
        return localDateTime;
    }

    public static LocalDate getLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    public static LocalTime getLocalTime(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime();
    }

}

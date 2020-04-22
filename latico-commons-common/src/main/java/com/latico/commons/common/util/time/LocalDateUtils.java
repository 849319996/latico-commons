package com.latico.commons.common.util.time;

import java.time.*;
import java.util.Date;

/**
 * <PRE>
 * LocalDate对象操作工具
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-10 16:55
 * @version: 1.0
 */
public class LocalDateUtils {

    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static LocalDate getLocalDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(2019, 9, 10);
    }

    public static int getYear(LocalDate localDate) {
        return localDate.getYear();
    }

    public static Month getMonth(LocalDate localDate) {
        return localDate.getMonth();
    }

    public static int getDayOfMonth(LocalDate localDate) {
        return localDate.getDayOfMonth();
    }

    public static DayOfWeek getDayOfWeek(LocalDate localDate) {
        return localDate.getDayOfWeek();
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

}

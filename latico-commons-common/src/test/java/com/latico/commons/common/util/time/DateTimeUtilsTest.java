package com.latico.commons.common.util.time;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class DateTimeUtilsTest {

    @Test
    public void countMillisTimeDescribeCn() {
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(5000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(59000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(60000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(61000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(120000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(121000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(360000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3600000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3666000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(3660000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(36600000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(86400000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(86430000));
        System.out.println(DateTimeUtils.countMillisTimeDescribeCn(96430000));
    }
    @Test
    public void countSecondTimeDescribeCn() {
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(5));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(59));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(60));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(61));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(120));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(121));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(360));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3600));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3666));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(3660));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(36600));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(86400));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(86430));
        System.out.println(DateTimeUtils.countSecondTimeDescribeCn(96430));
    }

    @Test
    public void toDate() throws ParseException {
        System.out.println(DateTimeUtils.toDateBy_ymdhms("2012-12-21 12:21:22"));
        System.out.println(DateTimeUtils.parseDate("2012-12-21 12:21:22", "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void parseDateAssignTodayTimeOffset() {
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime());
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.100").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.01").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset("05:02:34.11").getTime()));

    }

    @Test
    public void getCurYear() {
        System.out.println(DateTimeUtils.getCurYear());
    }

    @Test
    public void parseDateAssignTodayTimeOffset1() {
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.100").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.01").getTime());
        System.out.println(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.11").getTime());
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.100").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.01").getTime()));
        System.out.println(new Timestamp(DateTimeUtils.parseDateAssignTodayTimeOffset(new Date(System.currentTimeMillis()), "05:02:34.11").getTime()));

    }
}
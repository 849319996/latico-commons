package com.latico.commons.common.util.time;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.Assert.*;

public class LocalDateTimeUtilsTest {

    /**
     *
     */
    @Test
    public void test(){
        System.out.println(LocalDateTimeUtils.getCurrentLocalDateTime());
    }

    /**
     *
     */
    @Test
    public void test1(){
        LocalDateTime currentLocalDateTime = LocalDateTimeUtils.getCurrentLocalDateTime();

        System.out.println(currentLocalDateTime);
        Date date = LocalDateTimeUtils.asDate(currentLocalDateTime);
        System.out.println(date);
    }

    /**
     *
     */
    @Test
    public void test2(){
        LocalDateTime currentLocalDateTime = LocalDateTimeUtils.getCurrentLocalDateTime();
        System.out.println(currentLocalDateTime);
        LocalDateTime localDateTime = currentLocalDateTime.plusHours(-1);
        System.out.println(localDateTime);
    }
}
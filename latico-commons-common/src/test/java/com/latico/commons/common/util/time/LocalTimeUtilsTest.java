package com.latico.commons.common.util.time;

import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class LocalTimeUtilsTest {

    @Test
    public void getCurrentLocalTime() {
        System.out.println(LocalTimeUtils.getCurrentLocalTime());
    }

    @Test
    public void getNano() {
        LocalTime localTime = LocalTimeUtils.getCurrentLocalTime();
        System.out.println(localTime);
        System.out.println(LocalTimeUtils.getNano(localTime));
    }

    @Test
    public void getMilliSecond() {
        LocalTime localTime = LocalTimeUtils.getCurrentLocalTime();
        System.out.println(localTime);
        System.out.println(LocalTimeUtils.getMilliSecond(localTime));
    }
}
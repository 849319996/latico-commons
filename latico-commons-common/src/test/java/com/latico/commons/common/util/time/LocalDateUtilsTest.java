package com.latico.commons.common.util.time;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class LocalDateUtilsTest {

    @Test
    public void getLocalDate() {
        System.out.println(LocalDateUtils.getCurrentLocalDate());
        LocalDate localDate = LocalDateUtils.getLocalDate(2019, 1, 10);
        System.out.println(localDate);
    }
}
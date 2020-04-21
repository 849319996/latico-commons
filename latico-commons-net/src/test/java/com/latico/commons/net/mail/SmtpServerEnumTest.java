package com.latico.commons.net.mail;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmtpServerEnumTest {

    /**
     *
     */
    @Test
    public void test(){
        System.out.println(SmtpServerEnum.getEnumByMailAddress("123@qq.com"));
        System.out.println(SmtpServerEnum.getEnumByMailAddress("123@163.com"));
        System.out.println(SmtpServerEnum.getEnumByMailAddress("123@gmail.com"));
    }
}
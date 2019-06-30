package com.latico.commons.common.util.codec;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;

public class MD5UtilsTest {

    /**
     *
     */
    @Test
    public void test1() throws NoSuchAlgorithmException {
        System.out.println(MD5Utils.toLowerCaseMd5("fgg你好"));
        System.out.println(MD5Utils.toLowerCaseMd5ByGBK("fgg你好"));
        System.out.println(MD5Utils.toLowerCaseMd5ByUTF8("fgg你好"));
//        7e696161f558ab98fc2e43202516934c
    }
}
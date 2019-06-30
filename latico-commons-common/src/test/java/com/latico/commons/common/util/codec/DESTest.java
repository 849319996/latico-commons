package com.latico.commons.common.util.codec;

import org.junit.Test;

public class DESTest {

    @Test
    public void encrypt() throws Exception {
        DES des = new DES("abc");
        String encrypt = des.encrypt("12345你好");
        System.out.println(encrypt);
        System.out.println(des.decrypt(encrypt));
    }

    @Test
    public void decrypt() {
    }
}
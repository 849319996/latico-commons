package com.latico.commons.common.util.codec;

import org.junit.Test;

public class Base64UtilsTest {

    String str = "abc123abc123abc123abc123abc123abc123";
    String base64 = Base64Utils.encode(str);

    @Test
    public void encode() {
        System.out.println(Base64Utils.encode(str));
    }


    @Test
    public void decode() {
        System.out.println(new String(Base64Utils.decode(base64)));
    }

    @Test
    public void encodeByApache() {
        System.out.println(Base64Utils.encodeByApache(str));
    }

    @Test
    public void decodeByApache() {
        System.out.println(new String(Base64Utils.decodeByApache(base64)));
    }
}
package com.latico.commons.common.util.net;

import org.junit.Test;

import static org.junit.Assert.*;

public class Ipv6UtilsTest {

    @Test
    public void isIPv6LiteralAddress() {
        String ipv6 = "ABCD:EF01:2345:6789:ABCD:EF01:2345:6789";
        System.out.println(Ipv6Utils.isIPv6LiteralAddress(ipv6));
    }

    @Test
    public void isIPv6LiteralAddress2() {
        String ipv6 = "2000::1:2345:6789:abcd";
        System.out.println(Ipv6Utils.isIPv6LiteralAddress(ipv6));
    }

    @Test
    public void isIPv6LiteralAddress3() {
        String ipv6 = "2000:0:0:0:1:2345:6789:abcd";
        System.out.println(Ipv6Utils.isIPv6LiteralAddress(ipv6));
    }
}
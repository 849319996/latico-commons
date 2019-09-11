package com.latico.commons.common.util.net;

import org.junit.Test;

public class PingUtilsTest {

    @Test
    public void isReachableByInetAddress() {
        System.out.println(PingUtils.isReachableByInetAddress("172.168.10.7", 5000));
        System.out.println(PingUtils.isReachableByInetAddress("172.168.10.228", 5000));
        System.out.println(PingUtils.isReachableByInetAddress("172.168.10.156", 3000));
        System.out.println(PingUtils.isReachableByInetAddress("114.114.114.114", 3000));
        System.out.println(PingUtils.isReachableByInetAddress("8.8.8.8", 3000));
    }

    @Test
    public void isReachableByCmd() {

    }
}
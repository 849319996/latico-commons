package com.latico.commons.net.cmdclient.portmaper;

import org.junit.Test;

import java.net.InetSocketAddress;

public class IpPortMaperTest {

    @Test
    public void getIpPortMap() {
        System.out.println(IpPortMaper.getInstance().getIpPortMap());
    }

    public static void main(String[] args) {
//		test1();
//		test2();
        System.out.println(IpPortMaper.getInstance().getIpPortMap());
    }

    private static void test2() {
        InetSocketAddress ipPort = IpPortMaper.getInstance().getConvertedIpPort("10.24.88.27", 23);
        System.out.println(ipPort.getAddress().getHostAddress() + " " + ipPort.getPort());

        System.out.println(IpPortMaper.getInstance().getConvertedIpPort("10.25.7.40", 23));
        System.out.println(IpPortMaper.getInstance().getConvertedIpPort("10.24.89.15", 23));
    }

    /**
     *
     */
    private static void test1() {
        IpPortMaper.getInstance().putIpPortToMap("12.2.2.2", 22, "1.24.2.2", 50);
        IpPortMaper.getInstance().putIpPortToMap("12.2.2.3", 22, "1.24.2.3", 50);
        IpPortMaper.getInstance().putIpPortToMap("12.2.2.4", 22, "1.24.2.4", 50);
        IpPortMaper.getInstance().putIpPortToMap("12.2.2.5", 22, "1.24.2.5", 50);

        InetSocketAddress ipPort = IpPortMaper.getInstance().getConvertedIpPort("12.2.2.3", 22);
        System.out.println(ipPort.getAddress().getHostAddress() + " " + ipPort.getPort());
        System.out.println(IpPortMaper.getInstance().getConvertedIpPort("12.2.2.3", 23));
        System.out.println(IpPortMaper.getInstance().getConvertedIpPort("12.2.2.4", 22));
    }
}
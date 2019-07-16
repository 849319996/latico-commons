package com.latico.commons.common.util.net;

import org.junit.Test;

public class Ipv4UtilsTest {

    @Test
    public void getNetworkIp() {
        System.out.println(Ipv4Utils.getNetworkSegmentIp("172.168.10.10", "255.255.252.0"));
    }

    @Test
    public void getNetworkIp2() {
        System.out.println(Ipv4Utils.getNetworkSegmentIp("4.108.3.5", "255.255.255.252"));
    }

    /**
     *
     */
    @Test
    public void getIpV4GW(){
        System.out.println(Ipv4Utils.getIpV4GW("172.168.10.10", "255.255.252.0"));
    }

    /**
     *
     */
    @Test
    public void getAllSameNetworkIps(){
        System.out.println(Ipv4Utils.getAllSameNetworkIps("172.168.10.1", "255.255.255.252"));
    }
}
package com.latico.commons.common.util.net;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

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
    public void getAllSameNetworkIps() throws UnknownHostException {
        System.out.println(Ipv4Utils.getAllSameNetworkIps("172.168.10.1", "255.255.255.252"));
    }
    
    /**
     * 
     */
    @Test
    public void test(){
        SocketAddress socketAddress = new InetSocketAddress("1.1.1.1", 20);
        String socketStr = socketAddress.toString();
        System.out.println(socketStr);
        int indexOf = socketStr.lastIndexOf(":");
        System.out.println(socketStr.substring(1, indexOf));
        System.out.println(socketStr.substring(indexOf + 1));
    }

    /**
     *
     */
    @Test
    public void test2(){
        SocketAddress socketAddress = new InetSocketAddress("1.1.1.1", 20);
        System.out.println(Ipv4Utils.getHost(socketAddress));
    }
}
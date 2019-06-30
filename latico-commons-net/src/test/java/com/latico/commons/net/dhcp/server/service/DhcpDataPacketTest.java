package com.latico.commons.net.dhcp.server.service;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DhcpDataPacketTest {

    @Test
    public void convertToDhcpPacket() {
    }
    @Test
    public void name(){
        Set<Byte> set = new HashSet<>();
        byte b = 51;
        byte b2 = 52;
        byte b3 = 53;
        set.add(b);
        set.add(b2);
        set.add(b3);

        System.out.println(set.contains(51));
    }
}
package com.latico.commons.net.dhcp.server.service;

import com.latico.commons.common.util.net.Ipv4Utils;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AbstractDhcpServiceHandlerTest {

    @Test
    public void name(){
        try {
            InetAddress add = InetAddress.getByName("10.0.0.0");

            boolean giaddrIsValidIpV4Addr = Ipv4Utils.isValidIpV4Addr(add);
            System.out.println(giaddrIsValidIpV4Addr);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
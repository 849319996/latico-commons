package com.latico.commons.net.dhcp;

import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.net.dhcp.server.DhcpServerThread;
import com.latico.commons.net.dhcp.server.examples.DhcpServiceHandlerImplExample;
import com.latico.commons.net.dhcp.server.exception.DHCPServerInitException;
import org.junit.Test;

public class DhcpUtilsTest {

    @Test
    public void createDhcpServerAndStart() {
        try {
            DhcpServerThread dhcpServer = DhcpUtils.createDhcpServerAndStart(new DhcpServiceHandlerImplExample());

            //停止
//			dhcpServer.stopServer();
        } catch (DHCPServerInitException e) {
            e.printStackTrace();
        }

        ThreadUtils.sleepSecond(100000);
    }
}
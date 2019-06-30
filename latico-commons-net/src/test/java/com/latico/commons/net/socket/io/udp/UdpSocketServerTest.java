package com.latico.commons.net.socket.io.udp;

import com.latico.commons.common.util.net.UdpSocketUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import java.net.DatagramPacket;

public class UdpSocketServerTest {

    @Test
    public void sendData() {
        UdpSocketServer udpSocketServer = new UdpSocketServer("127.0.0.1", -1, 8888, 100, "UTF-8");
        udpSocketServer.startAsThread();

        String charset = "UTF-8";
        while (true) {
            DatagramPacket oneReceiveData = udpSocketServer.getReceivePacket();
            String str = UdpSocketUtils.readDatagramPacketToString(oneReceiveData, charset);
            System.out.println(oneReceiveData.getSocketAddress());
            System.out.println(oneReceiveData.getAddress().getHostAddress());
            System.out.println(oneReceiveData.getAddress().getHostName());
            System.out.println(oneReceiveData.getPort());
            System.out.println("收到" + oneReceiveData.getSocketAddress() + ":" +str);
            udpSocketServer.sendData("服务端处理了:" + str, oneReceiveData.getSocketAddress());
        }
    }
    @Test
    public void sendData2() {
        UdpSocketServer udpSocketServer = new UdpSocketServer(8888, 100);
        new Thread(udpSocketServer).start();

        ThreadUtils.sleepSecond(1);

        String charset = "UTF-8";
        while (true) {
            DatagramPacket oneReceiveData = udpSocketServer.getReceivePacket();
            String str = UdpSocketUtils.readDatagramPacketToString(oneReceiveData, charset);
            System.out.println("收到" + oneReceiveData.getSocketAddress() + ":" +str);
            udpSocketServer.sendData("服务端处理了:" + str, oneReceiveData.getSocketAddress());
        }
    }
}
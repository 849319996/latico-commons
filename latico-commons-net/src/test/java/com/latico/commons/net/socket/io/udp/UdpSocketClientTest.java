package com.latico.commons.net.socket.io.udp;

import org.junit.Test;

public class UdpSocketClientTest {

    @Test
    public void sendData() {
        UdpSocketClient client = new UdpSocketClient("127.0.0.1", 8888, -1, -1, 100, "UTF-8");
        client.startAsThread();

        client.sendData("客户端数据ajgja");
        System.out.println(client.getOneReceiveDataToString());
        client.sendData("客户端数据gsajgja2");
        System.out.println(client.getOneReceiveDataToString());
        client.close();
    }

    @Test
    public void sendData1() {
        UdpSocketClient client = new UdpSocketClient("127.0.0.1", 8888, -1, 100);
        new Thread(client).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.sendData("客户端数据ajgja");
        System.out.println(client.getOneReceiveDataToString());
        client.sendData("客户端数据gsajgja2");
        System.out.println(client.getOneReceiveDataToString());
        client.close();
    }
}
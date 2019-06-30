package com.latico.commons.net.socket.tcp.client;

import com.latico.commons.net.socket.nio.tcp.client.TcpNioSocketClient;
import com.latico.commons.net.socket.nio.tcp.common.SocketChannelHandler;
import org.junit.Test;

public class TcpNioSocketClientTest {

    @Test
    public void connect() {
        TcpNioSocketClient nioSocketClient = new TcpNioSocketClient("localhost", 7878);
        if (!nioSocketClient.isValid()) {
            return;
        }

        SocketChannelHandler socketChannelHandler = new SocketChannelHandler(nioSocketClient.getSocketChannel());

        //启动socketChannelHandler
        socketChannelHandler.startAsThread();

        System.out.println("发送数据：" + "你好啊");
        socketChannelHandler.sendData("你好啊");

        System.out.println("接收到数据:"+socketChannelHandler.getAndRemoveCurrentAllReceiveDataString());

        System.out.println("发送数据：" + "你好啊啊啊啊啊嗯");
        socketChannelHandler.sendData("你好啊啊啊啊啊嗯");

        System.out.println("接收到数据:"+socketChannelHandler.getAndRemoveCurrentAllReceiveDataString());

        System.out.println("发送数据：" + "<<exit>>");
        socketChannelHandler.sendData("<<exit>>");

        socketChannelHandler.close();
        nioSocketClient.close();
        System.out.println("关闭完成");
    }
}
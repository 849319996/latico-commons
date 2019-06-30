package com.latico.commons.net.socket.io.tcp.server.handler;

import com.latico.commons.net.socket.io.tcp.server.TcpSocketServer;
import com.latico.commons.net.socket.io.tcp.server.handler.service.TcpServerServiceTaskExample;
import org.junit.Test;

public class TcpServerHandlerTest {

    @Test
    public void test() {
        TcpSocketServer tcpSocketServer = new TcpSocketServer(7878);
        TcpServerHandler tcpServerHandler = new TcpServerHandler(tcpSocketServer, TcpServerServiceTaskExample.class);
        tcpServerHandler.startAsThread();

        //在测试用例里面，主线程关闭，其他线程也会关闭，所以不能让主线程关闭
        try {Thread.sleep(600000);} catch (InterruptedException e) {}


    }

    @Test
    public void test3() {
//        TcpServerHandlerImpl han = new TcpServerHandlerImpl(1, null, null);
    }
}
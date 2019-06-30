package com.latico.commons.net.socket.tcp.server.handler;

import com.latico.commons.net.socket.nio.tcp.server.TcpNioSocketServer;
import com.latico.commons.net.socket.nio.tcp.server.handler.TcpNioSocketServerHandler;
import com.latico.commons.net.socket.nio.tcp.server.handler.service.TcpNioServerServiceTaskImplExample;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TcpNioSocketServerHandlerTest {

    @Test
    public void run() {
        TcpNioSocketServer nioSocketServer = new TcpNioSocketServer(7878);
        TcpNioSocketServerHandler handler = new TcpNioSocketServerHandler(nioSocketServer, TcpNioServerServiceTaskImplExample.class);
        handler.startAsThread();


        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void startAsThread() {
        TcpNioSocketServer nioSocketServer = new TcpNioSocketServer(7878);
        TcpNioSocketServerHandler handler = new TcpNioSocketServerHandler(nioSocketServer, TcpNioServerServiceTaskImplExample.class);

        handler.startAsThread();

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
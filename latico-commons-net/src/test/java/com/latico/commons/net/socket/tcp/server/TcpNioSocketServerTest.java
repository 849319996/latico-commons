package com.latico.commons.net.socket.tcp.server;

import com.latico.commons.net.socket.nio.tcp.server.TcpNioSocketServer;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TcpNioSocketServerTest {

    @Test
    public void run() {
        TcpNioSocketServer nioSocketServer = new TcpNioSocketServer(7878);

        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
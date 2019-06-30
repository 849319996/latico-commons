package com.latico.commons.net.socket.io.tcp;

import com.latico.commons.common.util.thread.pool.ThreadPool;
import com.latico.commons.net.socket.io.tcp.common.SocketHandler;
import com.latico.commons.net.socket.io.tcp.server.TcpSocketServer;
import org.junit.Test;

import java.net.Socket;

public class TcpSocketServerTest {

    @Test
    public void getClientSocket() {
        TcpSocketServer tcpSocketServer = new TcpSocketServer(7878);
        tcpSocketServer.startAsThread();

        ThreadPool threadPool = new ThreadPool(10, 10, 30000, 1);
        while (true) {
            Socket socketHandler = tcpSocketServer.getAndRemoveSocket();
//            threadPool.execute(new AbstractTcpServerServiceHandler(socketHandler));
        }
    }
    @Test
    public void getClientSocketObject() {
        TcpSocketServer tcpSocketServer = new TcpSocketServer(7878);
        new Thread(tcpSocketServer).start();

        ThreadPool threadPool = new ThreadPool(10, 10, 30000, 1);
        while (true) {
            Socket socket = tcpSocketServer.getAndRemoveSocket();
            SocketHandler socketHandler = new SocketHandler(socket, false);
            threadPool.execute(new TcpServerServiceObjectHandler(socketHandler));
        }
    }
}
package com.latico.commons.net.socket.io.tcp.server.handler.service;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.socket.io.tcp.common.SocketHandler;

import java.net.Socket;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-21 13:42
 * @version: 1.0
 */
public abstract class AbstractTcpServerServiceTask implements TcpServerServiceTask {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTcpServerServiceTask.class);
    protected SocketHandler socketHandler;

    protected boolean status;
    @Override
    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void run() {
        startSocketHandler();

        Socket socket = socketHandler.getSocket();
        if (socket != null) {
            LOG.info("TcpServerServiceHandler处理:{}", socket.getRemoteSocketAddress());
        }else {
            LOG.info("TcpServerServiceHandler处理");
        }


        try {
            status = true;
            dealService();
        } catch (Exception e) {
            LOG.error(e);
        }finally {
            close();
        }
    }

    public void close() {
        status = false;
        socketHandler.close();
    }

    protected abstract void dealService();

    /**
     * 启动SocketHandler
     */
    private void startSocketHandler() {
        this.socketHandler = socketHandler;
        new Thread(socketHandler).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }
}

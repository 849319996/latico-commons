package com.latico.commons.net.socket.nio.tcp.client;

import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.NioSocketUtils;

import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-20 16:48
 * @version: 1.0
 */
public class TcpNioSocketClient {

    private static final Logger LOG = LoggerFactory.getLogger(TcpNioSocketClient.class);

    private String targetHost;
    private int targetPort;

    private AtomicBoolean status = new AtomicBoolean(false);
    private SocketChannel socketChannel;

    public TcpNioSocketClient(String targetHost, int targetPort) {

        this.targetHost = targetHost;
        this.targetPort = targetPort;
        connect();
    }

    public boolean isValid() {
        return status.get();
    }

    /**
     * 启动连接
     *
     * @return
     */
    private boolean connect() {
        try {
            socketChannel = NioSocketUtils.createSocketChannel(targetHost, targetPort);
            status.set(true);
        } catch (Exception e) {
            LOG.error(e);
            status.set(false);
        }
        return status.get();
    }

    public void close() {
        status.set(false);
        IOUtils.close(socketChannel);
    }

    public String getTargetHost() {
        return targetHost;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}

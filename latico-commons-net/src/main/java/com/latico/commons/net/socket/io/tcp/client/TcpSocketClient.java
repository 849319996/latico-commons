package com.latico.commons.net.socket.io.tcp.client;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.TcpSocketUtils;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 创建连接后，创建一个SocketHandler进行流操作
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-16 16:09
 * @version: 1.0
 */
public class TcpSocketClient {
    private static final Logger LOG = LoggerFactory.getLogger(TcpSocketClient.class);
    private String targetHost;
    private int targetPort;
    private int socketTimeout = 300000;

    /**
     * 总状态
     */
    private AtomicBoolean status = new AtomicBoolean(false);

    private Socket socket = null;

    /**
     * @param targetHost
     * @param targetPort
     */
    public TcpSocketClient(String targetHost, int targetPort) {

        this.targetHost = targetHost;
        this.targetPort = targetPort;

        //创建socket连接
        createSocket();
    }

    /**
     * 创建socket
     */
    private void createSocket() {

        try {
            socket = new Socket(targetHost, targetPort);
            socket.setSoTimeout(socketTimeout);
            LOG.info("已经和服务器连接：{}:{}", targetHost, targetPort);
            status.set(true);
        } catch (Exception e) {
            LOG.error(e);
            status.set(false);
        }
    }


    /**
     * 关闭跟服务端的连接，关闭自身线程
     */
    public void close() {
        status.set(false);
        TcpSocketUtils.close(socket);
    }

    /**
     * 获取状态，判断是否在运行
     * @return
     */
    public boolean getStatus() {
        return status.get();
    }

    public Socket getSocket() {
        return socket;
    }
}

package com.latico.commons.net.socket.io.tcp.server.handler.service;

import com.latico.commons.net.socket.io.tcp.common.SocketHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-17 13:54
 * @version: 1.0
 */
public interface TcpServerServiceTask extends Runnable {

    /**
     * 添加SocketHandler
     * @param socketHandler
     */
    public void setSocketHandler(SocketHandler socketHandler);
}

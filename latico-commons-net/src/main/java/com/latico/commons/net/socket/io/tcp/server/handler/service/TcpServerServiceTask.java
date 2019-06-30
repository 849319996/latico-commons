package com.latico.commons.net.socket.io.tcp.server.handler.service;

import com.latico.commons.net.socket.io.tcp.common.SocketHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-17 13:54
 * @Version: 1.0
 */
public interface TcpServerServiceTask extends Runnable {

    /**
     * 添加SocketHandler
     * @param socketHandler
     */
    public void setSocketHandler(SocketHandler socketHandler);
}

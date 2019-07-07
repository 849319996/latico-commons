package com.latico.commons.net.dhcp.server;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.dhcp.server.handler.DhcpServiceHandler;

import java.net.DatagramPacket;

/**
 * Servlet dispatcher
 */
/**
 * <PRE>
 *  DHCP 处理线程
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-05-31 16:11:50
 * @Version: 1.0
 */
public class DhcpHandlerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DhcpHandlerThread.class);

    private final DhcpServerThread server;
    /**
     * 业务处理器
     */
    private final DhcpServiceHandler serviceHandler;
    /**
     * 数据包
     */
    private final DatagramPacket datagramPacket;

    /**
     * @param server
     * @param servlet
     * @param datagramPacket
     */
    public DhcpHandlerThread(DhcpServerThread server, DhcpServiceHandler servlet, DatagramPacket datagramPacket) {
        this.server = server;
        this.serviceHandler = servlet;
        this.datagramPacket = datagramPacket;
    }

    @Override
    public void run() {
        try {
            DatagramPacket response = this.serviceHandler.makeResponseDatagram(this.datagramPacket);

            // invoke callback method
            this.server.sendResponseToClient(response);
        } catch (Exception e) {
            logger.error("Exception in handle 处理异常", e);
        }
    }
}
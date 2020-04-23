package com.latico.commons.netty.nettyserver;


import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import io.netty.channel.group.ChannelGroup;

import java.util.Collection;

/**
 * <PRE>
 * 管理器
 * </PRE>
 *
 * @author: latico
 * @date: 2019-11-08 23:21
 * @version: 1.0
 */
public class NettyServerManager <SEND_DATA, RECEIVE_DATA> {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServerManager.class);
    private final ChannelGroup clientChannelGroup;
    private final NettyServer nettyServer;

    public NettyServerManager(NettyServer nettyServer){

        this.nettyServer = nettyServer;
        this.clientChannelGroup = nettyServer.getClientChannelGroup();
    }
    public void startNettyServer() {
        nettyServer.startNettyServer();
    }

    public void nettyServerSendMsgToClient(SEND_DATA msg) {
        if (clientChannelGroup.size() <= 0) {
            return;
        }
        synchronized (this) {
            nettyServer.sendMsgToAllClient(msg);
        }
    }

    public void nettyServerSendAllMsgToClient(Collection<?> msgs) {
        if (clientChannelGroup.size() <= 0) {
            return;
        }
        synchronized (this) {
            for (Object msg : msgs) {
                if (!nettyServer.sendMsgToAllClient(msg)) {
                    LOG.warn("发送数据过程中,客户端数量变为零");
                    break;
                }
            }
        }
    }

    /**
     * 判断是否有效
     * @return
     */
    public boolean isValid() {
        return nettyServer.isStatusValid();
    }

    public void closeServer() {
        nettyServer.closeNettyServer();
    }
}

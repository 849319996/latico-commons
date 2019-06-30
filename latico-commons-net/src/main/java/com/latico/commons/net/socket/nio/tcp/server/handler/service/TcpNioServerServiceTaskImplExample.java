package com.latico.commons.net.socket.nio.tcp.server.handler.service;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.socket.nio.tcp.common.SocketChannelHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-21 0:27
 * @Version: 1.0
 */
public class TcpNioServerServiceTaskImplExample extends AbstracTcpNioServerServiceTask {
    private static final Logger LOG = LoggerFactory.getLogger(TcpNioServerServiceTaskImplExample.class);

    @Override
    public void handleRead(SelectionKey key) throws Exception {
        SocketChannel sc = (SocketChannel) key.channel();
        LOG.info("{}:读事件,对方:{}", getTaskId(), sc.getRemoteAddress());

        SocketChannelHandler socketChannelHandler = new SocketChannelHandler(sc, receiveQueueSize, this.charset);

        //太耗资源，不要启动线程方式接收
//        socketChannelHandler.startAsThread();

        String readStr = socketChannelHandler.readStrings();
        LOG.info("收到数据:{}", readStr);
        if (!socketChannelHandler.isValid()) {
            close();
        } else {
            socketChannelHandler.sendData("服务器处理了数据:" + readStr);
        }



    }


}

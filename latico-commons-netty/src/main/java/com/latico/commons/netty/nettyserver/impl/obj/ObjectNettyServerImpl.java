package com.latico.commons.netty.nettyserver.impl.obj;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyserver.AbstractNettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;


/**
 * <PRE>
 *
 * </PRE>
 * @author: latico
 * @date: 2019-11-08 23:14:01
 * @version: 1.0
 */
public class ObjectNettyServerImpl extends AbstractNettyServer<Serializable> {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectNettyServerImpl.class);

    @Override
    protected ChannelFuture initNettyServer() {
        SimpleChannelInboundHandler<Serializable> simpleChannelInboundHandler = new ObjectServerChannelInboundHandlerImpl(this);
        this.channelFuture = NettyTcpUtils.createServerByServiceHandlerByObject(this.localPort, simpleChannelInboundHandler);
        return this.channelFuture;
    }

}

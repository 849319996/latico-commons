package com.latico.commons.netty.nettyserver.impl.protobuf;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyserver.AbstractNettyServer;
import com.latico.commons.netty.nettyserver.impl.obj.ObjectServerChannelInboundHandlerImpl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.Serializable;


/**
 * <PRE>
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-11-08 23:14:01
 * @Version: 1.0
 */
public class ProtobufNettyServerImpl extends AbstractNettyServer<Serializable> {
    private static final Logger LOG = LoggerFactory.getLogger(ProtobufNettyServerImpl.class);

    @Override
    protected ChannelFuture initNettyServer() {
        SimpleChannelInboundHandler<Serializable> simpleChannelInboundHandler = new ObjectServerChannelInboundHandlerImpl(this);
        this.channelFuture = NettyTcpUtils.createServerByServiceHandlerByObject(this.localPort, simpleChannelInboundHandler);
        return this.channelFuture;
    }

}

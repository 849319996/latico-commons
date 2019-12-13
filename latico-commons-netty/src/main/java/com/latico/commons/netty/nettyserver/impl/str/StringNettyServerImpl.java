package com.latico.commons.netty.nettyserver.impl.str;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyserver.AbstractNettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * <PRE>
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-11-08 23:14:01
 * @Version: 1.0
 */
public class StringNettyServerImpl extends AbstractNettyServer<String> {
    private static final Logger LOG = LoggerFactory.getLogger(StringNettyServerImpl.class);

    @Override
    protected ChannelFuture initNettyServer() {
        SimpleChannelInboundHandler<String> simpleChannelInboundHandler = new StringServerChannelInboundHandlerImpl(this);
        this.channelFuture = NettyTcpUtils.createServerByServiceHandlerByString(this.localPort, simpleChannelInboundHandler);
        return this.channelFuture;
    }

}

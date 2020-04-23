package com.latico.commons.netty.nettyserver.impl.obj;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.nettyserver.NettyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

import java.io.Serializable;

/**
 * <PRE>
 * 一个客户端连上再断开时，六个事件的触发顺序：加入、(连接上(在SimpleChatServerInitializer中))、在线、异常、掉线、离开
 * 如果这个类注解了Sharable.class, 说明这个类会被多个channel共享
 * </PRE>
 *
 * @author: latico
 * @date: 2019-05-24 17:27
 * @version: 1.0
 */
@ChannelHandler.Sharable
public class ObjectServerChannelInboundHandlerImpl extends SimpleChannelInboundHandler<Serializable> {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectServerChannelInboundHandlerImpl.class);
    private ChannelGroup channelGroup;
    private NettyServer nettyServer;

    public ObjectServerChannelInboundHandlerImpl(NettyServer nettyServer) {
        this.channelGroup = nettyServer.getClientChannelGroup();
        this.nettyServer = nettyServer;
    }

    /**
     * 客户端连接的通道
     */

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("客户端:[{}] 激活", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("客户端:[{}] 失效", ctx.channel().remoteAddress());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("客户端:[{}] 加入", ctx.channel().remoteAddress());
        channelGroup.add(ctx.channel());
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        LOG.info("客户端:[{}] 离开", ctx.channel().remoteAddress());
        channelGroup.remove(ctx.channel());
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("{}发生异常,可能是客户端强制断开导致", cause, ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) {
        Channel channel = ctx.channel();
        LOG.debug("客户端:[{}] 发来:{}", channel.remoteAddress(), msg);

        //如果业务直接在这个方法处理的话，那么这里也可以不加入队列让外界处理
        nettyServer.addReceivedData(msg, channel);
    }


}

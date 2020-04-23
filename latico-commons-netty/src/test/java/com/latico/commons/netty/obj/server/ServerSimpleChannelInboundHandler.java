package com.latico.commons.netty.obj.server;

import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.obj.DemoBean;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-05-24 15:13
 * @version: 1.0
 */
@ChannelHandler.Sharable
public class ServerSimpleChannelInboundHandler extends SimpleChannelInboundHandler<DemoBean> {
    // 一个客户端连上再断开时，六个事件的触发顺序：加入、(连接上(在SimpleChatServerInitializer中))、在线、异常、掉线、离开
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 在ctx加入本Handler时触发，一般在此做初始化工作，如创建buf
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.out.println("client " + incoming.remoteAddress() + " 加入");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端和服务端建立tcp成功之后，Netty的NIO线程会调用channelActive
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.out.println("client " + incoming.remoteAddress() + " 在线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.err.println("client " + incoming.remoteAddress() + " 异常:" + cause.getMessage());
        // 当出现异常就关闭连接
        // cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.out.println("client " + incoming.remoteAddress() + " 掉线");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 从ctx移除本Handler时触发
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.out.println("client " + incoming.remoteAddress() + " 离开");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DemoBean msg) throws Exception {
        System.out.println("收到:" + msg);
        Channel channel = ctx.channel();
        msg.setName("服务器处理后:" + msg.getName());
        channel.writeAndFlush(msg);
    }
}

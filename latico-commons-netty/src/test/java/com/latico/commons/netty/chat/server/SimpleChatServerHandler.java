package com.latico.commons.netty.chat.server;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 一个客户端连上再断开时，六个事件的触发顺序：加入、(连接上(在SimpleChatServerInitializer中))、在线、异常、掉线、离开
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 在ctx加入本Handler时触发，一般在此做初始化工作，如创建buf
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        }
        System.out.println("client " + incoming.remoteAddress() + " 加入");
        channels.add(ctx.channel());
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
        for (Channel channel : channels) {
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        }
        System.out.println("client " + incoming.remoteAddress() + " 离开");
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = NettyTcpUtils.getChannel(ctx);
        System.out.println("**" + incoming.remoteAddress() + " 发来: " + msg);
        // System.out.println("server: " + msg);
        NettyTcpUtils.writeAndFlush(incoming, "服务端的回复: " + msg + "\n");
//        incoming.writeAndFlush("服务端的回复: " + msg + "\n");

//        通知所有的channel有人发东西进来
        for (Channel channel : channels) {
            if (channel != incoming) {
                // System.out.println("[" + incoming.remoteAddress() + "] " + msg);
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + msg + "\n");
                NettyTcpUtils.writeAndFlush(channel, "[" + incoming.remoteAddress() + "] " + msg + "\n");
            }
        }
    }

    // 优先级高于messageReceived方法，有了这个方法就会屏蔽messageReceived方法
    // @Override
    // public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // System.out.println("channelRead");
    // Channel incoming = NettyTcpUtils.getChannel(ctx);
    // for (Channel channel : channels) {
    // if (channel != incoming){
    // channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
    // } else {
    // channel.writeAndFlush("server: " + msg + "\n");
    // }
    // }
    // }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
//        Channel incoming = NettyTcpUtils.getChannel(ctx);
//        System.out.println("**" + incoming.remoteAddress() + " send: " + msg);
//        for (Channel channel : channels) {
//            if (channel != incoming) {
//                // System.out.println("[" + incoming.remoteAddress() + "] " + msg);
//                channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + msg + "\n");
//            } else {
//                // System.out.println("server: " + msg);
//                channel.writeAndFlush("server: " + msg + "\n");
//            }
//        }
//    }

}

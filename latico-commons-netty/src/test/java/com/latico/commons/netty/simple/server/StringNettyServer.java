package com.latico.commons.netty.simple.server;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.*;

public class StringNettyServer {
    public static void main(String[] args) {
        SimpleChannelInboundHandler<String> simpleChannelInboundHandler = new StringServerSimpleChannelInboundHandler();

        ChannelFuture channelFuture = NettyTcpUtils.createServerByServiceHandlerByString(7878, simpleChannelInboundHandler);

        System.out.println("启动完成");
        NettyTcpUtils.waitToShutdownGracefully(channelFuture);
        System.out.println("关闭完成");
//        ThreadUtils.sleepSecond(10);
//        NettyTcpUtils.closeAll(channelFuture);
    }
}

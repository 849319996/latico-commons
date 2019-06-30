package com.latico.commons.netty.obj.server;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.*;

public class ObjectNettyServer {
    public static void main(String[] args) {
        SimpleChannelInboundHandler serviceHandler = new ServerSimpleChannelInboundHandler();

        ChannelFuture channelFuture = NettyTcpUtils.createServerByServiceHandlerByObject(7878, serviceHandler);

        System.out.println("启动完成");

    }
}

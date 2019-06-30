package com.latico.commons.netty.udp.server;

import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.NettyUdpUtils;
import io.netty.channel.ChannelFuture;

public class UdpServer {

    // 相比于TCP而言，UDP不存在客户端和服务端的实际链接，因此
    // 不需要为连接(ChannelPipeline)设置handler
    public static void main(String[] args) throws Exception {
        int port = 7080;
        ChannelFuture channelFuture = NettyUdpUtils.createUdpOperator(port, new UdpServerHandler());

//        等待关闭
        NettyTcpUtils.waitToShutdownGracefully(channelFuture);

    }
}
package com.latico.commons.netty.chat.server;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.ChannelFuture;

public class SimpleChatServer {

    private static int port = 8080;

    public SimpleChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        ChannelFuture channelFuture = NettyTcpUtils.createServerByChannelInitializer(port, new SimpleChatServerInitializer());

        // 绑定端口，开始接收进来的连接
        // b.bind(11122);//可以绑定多个端口
        System.out.println("server 启动了");
        NettyTcpUtils.waitToShutdownGracefully(channelFuture);
    }

    public static void main(String[] args) throws Exception {
        new SimpleChatServer(port).run();
    }
}

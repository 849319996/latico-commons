package com.latico.commons.netty.simple.client;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.*;

import java.util.Date;

public class StringNettyClient {
    public static void main(String[] args) throws InterruptedException {

        SimpleChannelInboundHandler simpleChannelInboundHandler = new StringClientSimpleChannelInboundHandler();

        ChannelFuture channelFuture = NettyTcpUtils.createClientByServiceHandlerByString("127.0.0.1", 7878, simpleChannelInboundHandler);
        Channel channel = channelFuture.channel();
        int i = 0;
        while (true) {
            channel.writeAndFlush(new Date() + ": hello world!");
            Thread.sleep(1000);
            i++;
            if (i == 10) {
                System.out.println("停止");
                break;
            }
        }
        NettyTcpUtils.closeAll(channel);

    }
}
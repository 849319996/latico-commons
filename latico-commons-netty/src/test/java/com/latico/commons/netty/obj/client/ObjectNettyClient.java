package com.latico.commons.netty.obj.client;

import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.obj.DemoBean;
import io.netty.channel.*;

public class ObjectNettyClient {
    public static void main(String[] args) throws InterruptedException {

        ClientSimpleChannelInboundHandler serviceHandler = new ClientSimpleChannelInboundHandler();

        ChannelFuture channelFuture = NettyTcpUtils.createClientByServiceHandlerByObject("127.0.0.1", 7878, serviceHandler);
        Channel channel = channelFuture.channel();
        System.out.println("启动完成");
        int i = 0;
        while (i++ <= 3) {
            DemoBean bean = new DemoBean();
            bean.setId(i);
            bean.setName("name" + i);
            channel.writeAndFlush(bean);
            Thread.sleep(2000);
        }
        NettyTcpUtils.closeAll(channelFuture);
        channel.close();
    }
}
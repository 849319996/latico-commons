package com.latico.commons.netty.chat.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<String> {

    // 优先级高于messageReceived方法，有了这个方法就会屏蔽messageReceived方法
//     @Override
//     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//     System.out.println(msg.toString());
//     }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println(msg);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("客户端收到：" + msg);
    }
}

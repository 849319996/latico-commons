package com.latico.commons.netty.simple.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-24 17:27
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class StringClientSimpleChannelInboundHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("客户端收到：" + msg);
    }
}

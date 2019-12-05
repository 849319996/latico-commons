package com.latico.commons.netty.client.impl.str;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-24 17:27
 * @Version: 1.0
 */
@ChannelHandler.Sharable
public class StringClientSimpleChannelInboundHandler extends SimpleChannelInboundHandler<String> {

    private BlockingQueue<String> receiveQueue;

    public StringClientSimpleChannelInboundHandler(BlockingQueue<String> receiveQueue){
        this.receiveQueue = receiveQueue;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("原始数据:\r\n" + msg);
        this.receiveQueue.add(msg);
    }
}

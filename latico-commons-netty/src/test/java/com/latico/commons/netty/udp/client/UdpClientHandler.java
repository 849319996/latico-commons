package com.latico.commons.netty.udp.client;

import com.latico.commons.netty.NettyUdpUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause)throws Exception{
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String response = NettyUdpUtils.getContentByStrUtf8(msg);
        System.out.println("客户端端收到:" + response);
    }
}
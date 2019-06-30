package com.latico.commons.netty.udp.server;

import com.latico.commons.netty.NettyUdpUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.ThreadLocalRandom;

import java.net.InetSocketAddress;

public class UdpServerHandler
        extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final String[] DICTIONARY = {
            "看我的，hello world",
            "C++",
            "C#",
            "Java",
            "python"
    };

    private String nextQuoto() {
        // 线程安全的随机类：ThreadLocalRandom
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        // 因为Netty对UDP进行了封装，所以接收到的是DatagramPacket对象。
//        获取客户端的Socket连接对象，然后回复内容
        InetSocketAddress sender = msg.sender();
        String req = NettyUdpUtils.getContentByStrUtf8(msg);
        System.out.println("服务端收到:" + sender + "的内容:" + req);


        NettyUdpUtils.writeAndFlushByUdpStrUtf8(sender, ctx.channel(), "结果：" + nextQuoto());
    }
}
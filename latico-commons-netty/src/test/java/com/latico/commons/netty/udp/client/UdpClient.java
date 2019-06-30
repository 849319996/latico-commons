package com.latico.commons.netty.udp.client;

import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.NettyUdpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class UdpClient {

    public static void main(String[] args) throws Exception {
        int serverPort = 7080;
        ChannelFuture channelFuture = NettyUdpUtils.createUdpOperator(0, new UdpClientHandler());
        Channel ch = channelFuture.sync().channel();
        int i = 0;
        while (true) {
            i++;
            if (i == 10) {
                break;
            }

            String str = "UDP报文内容" + i;
            // 向网段类所有机器广播发UDP
            NettyUdpUtils.writeAndFlushByUdpStrUtf8("localhost", serverPort, ch, str);
            System.out.println("发送完成:" + str);
            ThreadUtils.sleepSecond(3);
        }

        NettyTcpUtils.closeAll(channelFuture);

    }
}
package com.latico.commons.netty.chat.client;

import com.latico.commons.netty.NettyTcpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Scanner;

public class SimpleChatClient {

    private String host;
    private int port;

    public SimpleChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = NettyTcpUtils.createClientByChannelInitializer(host, port, new SimpleChatClientInitializer());

            Channel channel = channelFuture.channel();

            Scanner sc = new Scanner(System.in);
            System.out.println("please enter...");
            boolean exit = false;
            // 输入exit，退出系统
            while (!exit) {
                String str = sc.next();
                System.out.println("发送:" + str);
                channel.writeAndFlush(str + "\r\n");
                if (str.equalsIgnoreCase("exit")) {
                    exit = true;
                    channel.close();
                }
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new SimpleChatClient("localhost", 8080).run();
    }
}

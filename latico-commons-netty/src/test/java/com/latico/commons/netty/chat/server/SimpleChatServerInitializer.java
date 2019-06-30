package com.latico.commons.netty.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder StringDecoder = new StringDecoder();
    private static final StringEncoder StringEncoder = new StringEncoder();
    private static final SimpleChatServerHandler handler = new SimpleChatServerHandler();

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // Pipeline里的Handler是从底层开始向上添加的，故流动方向为后添加的输出给先添加的、或先添加的读入给后添加的
        ChannelPipeline pipeline = ch.pipeline();

        // 添加ChannelHandler，顺序是敏感的；名字任意，不冲突即可，也可以不指定名字

        // Netty提供了三种解码器解决粘包拆包问题：LineBasedFrameDecoder、DelimiterBasedFrameDecoder、LengthFieldBasedFrameDecoder。
        // 其中，前二者所读码流到达指定长度或遇到分隔符时认为结束，若读到的数据大于指定长度则抛TooLongFrameException并忽略之前读到的码流；最后一者每次读固定长度码流。
        // 也可以继承ByteToMessageDecoder自己处理
//        pipeline.addLast("FrameDecoder", new ProtobufVarint32FrameDecoder());
        pipeline.addLast("StringDecoder", StringDecoder);

        // 解码只会应用于读数据时、编码只会应用于写数据时，因此解码器与编码器添加的先后顺序在客户端和服务端中可不同，但编码器添加的顺序须桶，解码器亦然。
        pipeline.addLast("StringEncoder", StringEncoder);

        pipeline.addLast("handler", handler);

        System.out.println("client " + ch.remoteAddress() + " 连接上");
    }
}

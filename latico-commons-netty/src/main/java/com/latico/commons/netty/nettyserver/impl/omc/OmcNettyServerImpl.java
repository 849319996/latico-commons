package com.latico.commons.netty.nettyserver.impl.omc;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyclient.impl.omc.OmcByteToMessageDecoder;
import com.latico.commons.netty.nettyclient.impl.omc.OmcMessageToByteEncoder;
import com.latico.commons.netty.nettyclient.impl.omc.OmcProtocol;
import com.latico.commons.netty.nettyserver.AbstractNettyServer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * <PRE>
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-11-08 23:14:01
 * @Version: 1.0
 */
public class OmcNettyServerImpl extends AbstractNettyServer<OmcProtocol> {
    private static final Logger LOG = LoggerFactory.getLogger(OmcNettyServerImpl.class);

    @Override
    protected ChannelFuture initNettyServer() {
        //编码器
        OmcMessageToByteEncoder omcMessageToByteEncoder = new OmcMessageToByteEncoder();

        //业务解码器
        ChannelInboundHandler omcClientSimpleChannelInboundHandler = new OmcServerChannelInboundHandlerImpl(this);

        //初始化通道
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
//                在ChannelPipeline中注册所有的业务处理对象，顺序是传进来的顺序
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(omcMessageToByteEncoder);
                //长度字段解码器
                pipeline.addLast(new LengthFieldBasedFrameDecoder(maxFrameLength, 7, 2, 0, 0));
                //自定义解码器
                pipeline.addLast(new OmcByteToMessageDecoder());
                pipeline.addLast(omcClientSimpleChannelInboundHandler);
            }
        };

        this.channelFuture = NettyTcpUtils.createServerByChannelInitializer(this.localPort, channelInitializer);
        return this.channelFuture;
    }

}

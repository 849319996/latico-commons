package com.latico.commons.netty.nettyclient.impl.omc;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyclient.AbstractNettyClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * <PRE>
 *     华为OMC系统告警
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-05 14:26
 * @version: 1.0
 */
public class OmcNettyClientImpl extends AbstractNettyClient<OmcProtocol> {
    private static final Logger LOG = LoggerFactory.getLogger(OmcNettyClientImpl.class);

    @Override
    public ChannelFuture initNettyClient() {

        try {
            //编码器
            OmcMessageToByteEncoder omcMessageToByteEncoder = new OmcMessageToByteEncoder();

            //业务解码器
            ChannelInboundHandler omcClientSimpleChannelInboundHandler = new OmcClientChannelInboundHandler(this);

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

            this.channelFuture = NettyTcpUtils.createClientByChannelInitializer(remoteHost, remotePort, channelInitializer);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return channelFuture;
    }


}

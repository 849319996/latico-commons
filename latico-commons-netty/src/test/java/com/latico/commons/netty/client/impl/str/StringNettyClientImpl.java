package com.latico.commons.netty.client.impl.str;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.client.AbstractNettyClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * <PRE>
 * TODO 未完成
 * 未完成：报文最大长度控制，分隔符列表
 * 注意事项：netty会对分隔符切割后，会丢掉分隔符，跟split的效果一样。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-12-05 14:26
 * @Version: 1.0
 */
public class StringNettyClientImpl extends AbstractNettyClient<String> {
    private static final Logger LOG = LoggerFactory.getLogger(StringNettyClientImpl.class);

    @Override
    public boolean init(String remoteHost, int remotePort, int receiveQueueSize) {
        try {

            initReceiveQueue(receiveQueueSize);
            SimpleChannelInboundHandler simpleChannelInboundHandler = new StringClientSimpleChannelInboundHandler(this.receiveQueue);
            DelimiterBasedFrameDecoder delimiterBasedFrameDecoder = getDelimiterBasedFrameDecoder();
            this.channelFuture = NettyTcpUtils.createClientByServiceHandlerByStringWithFrameDecoder(remoteHost,  remotePort, delimiterBasedFrameDecoder, simpleChannelInboundHandler);
            this.channel = channelFuture.channel();

            this.status = true;
        } catch (Exception e) {
            this.status = false;
            LOG.error("", e);
        }
        return this.status;
    }

    private DelimiterBasedFrameDecoder getDelimiterBasedFrameDecoder() {
        ByteBuf delimiter1 = Unpooled.copiedBuffer(": ".getBytes());
        ByteBuf delimiter2 = Unpooled.copiedBuffer(">".getBytes());
        ByteBuf delimiter3 = Unpooled.copiedBuffer(":".getBytes());
        ByteBuf delimiter4 = Unpooled.copiedBuffer("> ".getBytes());

        ByteBuf[] delimiters = new ByteBuf[]{delimiter1, delimiter2, delimiter3,delimiter4};

        return new DelimiterBasedFrameDecoder(
                10240, delimiters);
    }


}

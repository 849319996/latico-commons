package com.latico.commons.netty.nettyclient.impl.obj;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.netty.NettyTcpUtils;
import com.latico.commons.netty.nettyclient.AbstractNettyClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * <PRE>
 * 报文最大长度控制，分隔符列表
 * 注意事项：netty会对分隔符切割后，会丢掉分隔符，跟split的效果一样。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-12-05 14:26
 * @Version: 1.0
 */
public class ObjectNettyClientImpl extends AbstractNettyClient<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectNettyClientImpl.class);

    @Override
    public ChannelFuture initNettyClient() {

        try {
            SimpleChannelInboundHandler simpleChannelInboundHandler = new ObjectClientChannelInboundHandlerImpl(this);
            //没有分隔符方式
            this.channelFuture = NettyTcpUtils.createClientByServiceHandlerByObject(remoteHost, remotePort, simpleChannelInboundHandler);
        } catch (Exception e) {
            LOG.error("", e);
        }
        return channelFuture;
    }


}

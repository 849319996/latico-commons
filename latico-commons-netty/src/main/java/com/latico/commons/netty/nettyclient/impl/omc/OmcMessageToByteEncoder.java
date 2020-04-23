package com.latico.commons.netty.nettyclient.impl.omc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <PRE>
 *
 * </PRE>
 * @author: latico
 * @date: 2019-12-13 22:23:25
 * @version: 1.0
 */
@ChannelHandler.Sharable
public class OmcMessageToByteEncoder extends MessageToByteEncoder<OmcProtocol> {
  
    @Override
    protected void encode(ChannelHandlerContext ctx, OmcProtocol msg, ByteBuf out) throws Exception {
        out.writeShort(OmcProtocol.startSign);
        out.writeByte(msg.getMsgType());
        out.writeInt(msg.getTimeStamp());
        out.writeShort(msg.getLenOfBody());
        out.writeBytes(msg.getMsgBody());
    }
}
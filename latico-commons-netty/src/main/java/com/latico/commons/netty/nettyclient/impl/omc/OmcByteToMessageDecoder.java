package com.latico.commons.netty.nettyclient.impl.omc;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-12-13 22:40:06
 * @version: 1.0
 */
public class OmcByteToMessageDecoder extends ByteToMessageDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(OmcByteToMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        int readableBytes = buffer.readableBytes();
        //如果小于9，直接丢去报文
        if (readableBytes < OmcProtocol.BASE_LENGTH) {
            LOG.warn("丢弃报文, 字节数:{}", readableBytes);
            buffer.skipBytes(buffer.readableBytes());
            return;
        }
        OmcProtocol omcProtocol = new OmcProtocol();

        //因为利用切割解码器进行去除开始标志，这里不需要处理，直接从报文类型开始解析
        short startSign = buffer.readShort();
        if (startSign != OmcProtocol.startSign) {
        //    不是起始的，跳过
            buffer.skipBytes(buffer.readableBytes());
            return;
        }

        omcProtocol.setMsgType(buffer.readByte());
        omcProtocol.setTimeStamp(buffer.readInt());
        omcProtocol.setLenOfBody(buffer.readShort());
        byte[] msgBody = new byte[omcProtocol.getLenOfBody()];
        buffer.readBytes(msgBody);
        omcProtocol.setMsgBody(msgBody);

        //添加到转化后的队列中
        out.add(omcProtocol);

        // buffer的释放在ByteToMessageDecoder里面已经帮忙做了，这里不能再释放引用计数器io.netty.util.ReferenceCounted.release()
        // ReferenceCountUtil.release(buffer);

        //前面已经把消息转化了放到out中，下一个处理器会拿到，这里不需要强行传递，因为消息已经被消耗了
        // ctx.fireChannelRead(buffer);
    }

}  
package com.latico.commons.netty;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * <PRE>
 * netty工具类，tcp、udp等
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-30 11:29
 * @Version: 1.0
 */
public class NettyUdpUtils extends NettyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NettyUdpUtils.class);

    /**
     * 创建一个UDP的通道
     * 客户端和服务端都共用，调用此方法进行创建通道
     *
     * @param localInetPort  如果是客户端可以为0，这样就是会随机选一个端口绑定，服务端的话就要指定
     * @param workThreadSize 工作线程大小
     * @param childHandler   业务处理
     * @return
     */
    public static ChannelFuture createUdpOperator(int localInetPort, int workThreadSize, ChannelHandler childHandler) {
        ChannelFuture channelFuture = null;
        try {
            EventLoopGroup group = createNioEventLoopGroup(workThreadSize);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
//                    支持广播
                    .option(ChannelOption.SO_BROADCAST, true);

            bootstrap.handler(childHandler);
            //阻塞到绑定端口成功
            channelFuture = bootstrap.bind(localInetPort).sync();
        } catch (Exception e) {
            LOG.error(e);
            closeAll(channelFuture);
        }
        return channelFuture;
    }

    /**
     * 创建一个UDP的通道
     *
     * @param localInetPort 绑定本地的端口，如果等于0，那么系统就会自动获取一个端口
     * @param childHandler  业务处理
     * @return
     */
    public static ChannelFuture createUdpOperator(int localInetPort, ChannelHandler childHandler) {
        return createUdpOperator(localInetPort, 0, childHandler);
    }

    /**
     * @param receiverHostname 支持广播地址：255.255.255.255
     * @param receiverPort
     * @param channel
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdp(String receiverHostname, int receiverPort, Channel channel, ByteBuf data) {
        InetSocketAddress socketAddress = new InetSocketAddress(receiverHostname, receiverPort);
        return writeAndFlushByUdp(socketAddress, channel, data);
    }

    /**
     * 发送数据到UDP
     *
     * @param receiverSocket 接受者地址，支持广播地址：255.255.255.255
     * @param channel
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdp(InetSocketAddress receiverSocket, Channel channel, ByteBuf data) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(data, receiverSocket);
            channel.writeAndFlush(datagramPacket).sync();
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
        return true;
    }

    /**
     * 写字符串数据到UDP
     *
     * @param sreceiverHostname
     * @param receiverPort
     * @param channel
     * @param charset
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdpStr(String sreceiverHostname, int receiverPort, Channel channel, Charset charset, String data) {
        return writeAndFlushByUdp(sreceiverHostname, receiverPort, channel, Unpooled.copiedBuffer(data, charset));
    }

    /**
     * @param receiverSocket
     * @param channel
     * @param charset
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdpStr(InetSocketAddress receiverSocket, Channel channel, Charset charset, String data) {
        return writeAndFlushByUdp(receiverSocket, channel, Unpooled.copiedBuffer(data, charset));
    }

    /**
     * 写字符串数据到UDP，使用UTF-8字符集
     *
     * @param receiverHostname
     * @param receiverPort
     * @param channel
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdpStrUtf8(String receiverHostname, int receiverPort, Channel channel, String data) {
        return writeAndFlushByUdpStr(receiverHostname, receiverPort, channel, CharsetUtil.UTF_8, data);
    }

    /**
     * @param receiverSocket
     * @param channel
     * @param data
     * @return
     */
    public static boolean writeAndFlushByUdpStrUtf8(InetSocketAddress receiverSocket, Channel channel, String data) {
        return writeAndFlushByUdpStr(receiverSocket, channel, CharsetUtil.UTF_8, data);
    }

    /**
     * 获取字符串内容，UTF-8字符集
     *
     * @param datagramPacket
     * @return
     */
    public static String getContentByStrUtf8(DatagramPacket datagramPacket) {
        ByteBuf content = datagramPacket.content();
        return content.toString(CharsetUtil.UTF_8);
    }

}

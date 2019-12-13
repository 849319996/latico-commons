package com.latico.commons.netty;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * <PRE>
 * netty工具类，tcp、udp等
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-30 11:29
 * @Version: 1.0
 */
public class NettyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NettyUtils.class);

    /**
     * 创建事件处理线程组
     *
     * @param threadSize
     * @return
     */
    public static NioEventLoopGroup createNioEventLoopGroup(int threadSize) {
        //            处理IO事件的线程池，也就是工作线程池，这里我们使用默认，netty里面默认是处理器数量乘于2
        NioEventLoopGroup childGroup = null;

        if (threadSize <= 0) {
            childGroup = new NioEventLoopGroup();
        } else {
            childGroup = new NioEventLoopGroup(threadSize);
        }
        return childGroup;
    }

    /**
     * 关闭服务端的通道
     *
     * @param channelFuture
     */
    public static void closeAll(ChannelFuture channelFuture) {
        if (channelFuture != null) {
            Channel channel = channelFuture.channel();
            closeAll(channel);
        }
    }

    /**
     * 关闭一个Channel的所有资源，服务端跟客户端的IO通道
     * 包括线程池
     *
     * @param channel
     */
    public static void closeAll(Channel channel) {
        if (channel != null) {
            try {

//                只关闭通道
                channel.close();

                // 关闭父的通道（如果有）
                Channel parent = channel.parent();
                if (parent != null) {
                    parent.close();
//                    关闭父的线程池
                    EventLoop eventExecutors = parent.eventLoop();
                    if (eventExecutors != null) {
                        eventExecutors.shutdownGracefully();
                    }
                }

                channel.closeFuture();

//                关闭线程池，如果不关闭线程池，会一直运行
                channel.eventLoop().shutdownGracefully();
            } catch (Exception e) {
                LOG.error(e);
            }
        }

    }

    /**
     * 等待去关闭
     *
     * @param channelFuture
     */
    public static void waitToShutdownGracefully(ChannelFuture channelFuture) {
        if (channelFuture == null) {
            return;
        }
        try {
            //阻塞服务端通道线程，直到关闭，最后在finally释放资源
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            //finally释放资源
            closeAll(channelFuture);
        }
    }

}

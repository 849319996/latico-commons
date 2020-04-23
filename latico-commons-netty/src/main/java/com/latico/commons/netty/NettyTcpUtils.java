package com.latico.commons.netty;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * <PRE>
 * netty工具类，tcp、udp等
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-30 11:29
 * @version: 1.0
 */
public class NettyTcpUtils extends NettyUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NettyTcpUtils.class);

    /**
     * 创建和启动服务端
     * 使用默认的线程数量
     * 创建完，已自动启动
     *
     * @param inetPort
     * @param childHandler
     * @return
     */
    public static ChannelFuture createServerByChannelInitializer(int inetPort, ChannelHandler childHandler) {
        return createServerByChannelInitializer(inetPort, 0, 0, 0, childHandler);
    }

    /**
     * 创建和启动服务端
     * 创建完，已自动启动
     *
     * @param inetPort       绑定的端口
     * @param connThreadSize 连接事件处理线程数量
     * @param workThreadSize 工作线程数量
     * @param backlog        ChannelOption.SO_BACKLOG对应的是tcp/ip协议listen函数中的backlog参数，
     *                       函数listen(int socketfd,int backlog)用来初始化服务端可连接队列，
     *                       服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，
     *                       多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，
     *                       backlog参数指定了队列的大小
     * @param childHandler   IO事件处理
     * @return 服务端的通道，用于接收客户端连接的
     */
    public static ChannelFuture createServerByChannelInitializer(int inetPort, int connThreadSize, int workThreadSize, int backlog, ChannelHandler childHandler) {
        ChannelFuture channelFuture = null;
        try {

//            专门处理连接事件的线程池，正常的话，线程应该设置为1（new NioEventLoopGroup(1);），这里我们使用默认
            NioEventLoopGroup parentGroup = createNioEventLoopGroup(connThreadSize);
            NioEventLoopGroup childGroup = createNioEventLoopGroup(workThreadSize);

            ServerBootstrap serverBootstrap = new ServerBootstrap();

//            绑定连接处理类为NioServerSocketChannel
            serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class);

//            如果大于等于1的时候才设置
            if (backlog >= 1) {
                serverBootstrap.option(ChannelOption.SO_BACKLOG, backlog);
            }

//            绑定IO处理类channelHandler
            serverBootstrap.childHandler(childHandler);
//            绑定端口，阻塞知道绑定完成
            channelFuture = serverBootstrap.bind(inetPort).sync();
        } catch (Exception e) {
            LOG.error("创建Netty服务端异常", e);

//            关闭服务器通道
            closeAll(channelFuture);
        }

        return channelFuture;
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     *
     * @param inetPort
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandler(int inetPort, ChannelHandler... serviceHandlers) {
        return createServerByServiceHandler(inetPort, 0, 0, 0, serviceHandlers);
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     *
     * @param inetPort
     * @param connThreadSize
     * @param workThreadSize
     * @param backlog
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandler(int inetPort, int connThreadSize, int workThreadSize, int backlog, ChannelHandler... serviceHandlers) {
//        通过业务Handler创建一个ChannelInitializer
        ChannelInitializer channelInitializer = makeChannelInitializer(serviceHandlers);

//        利用ChannelInitializer来初始化ChannelFuture
        ChannelFuture channelFuture = createServerByChannelInitializer(inetPort, connThreadSize, workThreadSize, backlog, channelInitializer);
        return channelFuture;
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     * 自动带上Object编码器和解码器
     *
     * @param inetPort
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandlerByObject(int inetPort, ChannelHandler... serviceHandlers) {
        return createServerByServiceHandlerByObject(inetPort, 0, 0, 0, serviceHandlers);
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     * 自动带上Object编码器和解码器
     *
     * @param inetPort
     * @param connThreadSize
     * @param workThreadSize
     * @param backlog
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandlerByObject(int inetPort, int connThreadSize, int workThreadSize, int backlog, ChannelHandler... serviceHandlers) {
//        通过业务Handler创建一个Object的ChannelInitializer
        ChannelInitializer channelInitializer = makeChannelInitializerByObject(serviceHandlers);

//        利用ChannelInitializer来初始化ChannelFuture
        ChannelFuture channelFuture = createServerByChannelInitializer(inetPort, connThreadSize, workThreadSize, backlog, channelInitializer);
        return channelFuture;
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     * 自动带上String编码器和解码器
     *
     * @param inetPort
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandlerByString(int inetPort, ChannelHandler... serviceHandlers) {
        return createServerByServiceHandlerByString(inetPort, 0, 0, 0, serviceHandlers);
    }

    /**
     * 利用业务ChannelHandler来初始化ChannelFuture
     * 自动带上String编码器和解码器
     *
     * @param inetPort
     * @param connThreadSize
     * @param workThreadSize
     * @param backlog
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createServerByServiceHandlerByString(int inetPort, int connThreadSize, int workThreadSize, int backlog, ChannelHandler... serviceHandlers) {
//        通过业务Handler创建一个Object的ChannelInitializer
        ChannelInitializer channelInitializer = makeChannelInitializerByString(serviceHandlers);

//        利用ChannelInitializer来初始化ChannelFuture
        ChannelFuture channelFuture = createServerByChannelInitializer(inetPort, connThreadSize, workThreadSize, backlog, channelInitializer);
        return channelFuture;
    }


    /**
     * 创建和启动客户端
     * 创建完，已自动启动
     *
     * @param inetHost           服务端地址
     * @param inetPort           服务端端口
     * @param channelInitializer IO通道业务处理
     * @return
     */
    public static ChannelFuture createClientByChannelInitializer(String inetHost, int inetPort, int workThreadSize, ChannelHandler channelInitializer) {
        ChannelFuture channelFuture = null;
        try {
            Bootstrap bootstrap = new Bootstrap();
//        客户端使用一个线程即可
            NioEventLoopGroup worker = createNioEventLoopGroup(workThreadSize);

//            客户端是使用NioSocketChannel作为通信通道
            bootstrap.group(worker).channel(NioSocketChannel.class);

//            IO通道业务处理器
            bootstrap.handler(channelInitializer);

//            连接服务端，阻塞到连接完成
            channelFuture = bootstrap.connect(inetHost, inetPort).sync();

        } catch (Exception e) {
            LOG.error("创建Netty客户端异常", e);

//            关闭服务器通道
            closeAll(channelFuture);
        }

        return channelFuture;
    }

    /**
     * 创建和启动客户端
     * 创建完，已自动启动
     *
     * @param inetHost
     * @param inetPort
     * @param channelInitializer
     * @return
     */
    public static ChannelFuture createClientByChannelInitializer(String inetHost, int inetPort, ChannelHandler channelInitializer) {
        return createClientByChannelInitializer(inetHost, inetPort, 0, channelInitializer);
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     *
     * @param inetHost
     * @param inetPort
     * @param workThreadSize
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createClientByServiceHandler(String inetHost, int inetPort, int workThreadSize, ChannelHandler... serviceHandlers) {
        ChannelInitializer channelInitializer = makeChannelInitializer(serviceHandlers);
        ChannelFuture channelFuture = createClientByChannelInitializer(inetHost, inetPort, workThreadSize, channelInitializer);
        return channelFuture;
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     *
     * @param inetHost
     * @param inetPort
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createClientByServiceHandler(String inetHost, int inetPort, ChannelHandler... serviceHandlers) {
        return createClientByServiceHandler(inetHost, inetPort, 0, serviceHandlers);
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     * 自动带上了Object的编码器和解码器
     *
     * @param inetHost
     * @param inetPort
     * @param workThreadSize
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByObject(String inetHost, int inetPort, int workThreadSize, ChannelHandler... serviceHandlers) {
        ChannelInitializer channelInitializer = makeChannelInitializerByObject(serviceHandlers);
        ChannelFuture channelFuture = createClientByChannelInitializer(inetHost, inetPort, workThreadSize, channelInitializer);
        return channelFuture;
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     * 自动带上了Object的编码器和解码器
     *
     * @param inetHost
     * @param inetPort
     * @param serviceHandlers
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByObject(String inetHost, int inetPort, ChannelHandler... serviceHandlers) {
        return createClientByServiceHandlerByObject(inetHost, inetPort, 0, serviceHandlers);
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     * 自动带上了String的编码器和解码器
     *
     * @param inetHost
     * @param inetPort
     * @param workThreadSize
     * @param serviceHandlers 业务处理器
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByString(String inetHost, int inetPort, int workThreadSize, ChannelHandler... serviceHandlers) {
        ChannelInitializer channelInitializer = makeChannelInitializerByString(serviceHandlers);
        ChannelFuture channelFuture = createClientByChannelInitializer(inetHost, inetPort, workThreadSize, channelInitializer);
        return channelFuture;
    }

    /**
     * @param inetHost
     * @param inetPort
     * @param workThreadSize
     * @param frameDecoderChannelHandler 分隔符处理器
     * @param serviceHandlers 业务处理器
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByStringWithFrameDecoder(String inetHost, int inetPort, int workThreadSize, ChannelHandler frameDecoderChannelHandler, ChannelHandler... serviceHandlers) {
        ChannelInitializer channelInitializer = makeChannelInitializerByStringWithFrameDecoder(frameDecoderChannelHandler, serviceHandlers);
        ChannelFuture channelFuture = createClientByChannelInitializer(inetHost, inetPort, workThreadSize, channelInitializer);
        return channelFuture;
    }

    /**
     * 通过业务handler的方式创建客户端的ChannelFuture
     * 自动带上了String的编码器和解码器
     *
     * @param inetHost
     * @param inetPort
     * @param serviceHandlers 业务处理器
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByString(String inetHost, int inetPort, ChannelHandler... serviceHandlers) {
        return createClientByServiceHandlerByString(inetHost, inetPort, 0, serviceHandlers);
    }

    /**
     * 带分隔符的方式
     * @param inetHost
     * @param inetPort
     * @param frameDecoderChannelHandler 分隔符处理器
     * @param serviceHandlers 业务处理器
     * @return
     */
    public static ChannelFuture createClientByServiceHandlerByStringWithFrameDecoder(String inetHost, int inetPort, ChannelHandler frameDecoderChannelHandler, ChannelHandler... serviceHandlers) {
        return createClientByServiceHandlerByStringWithFrameDecoder(inetHost, inetPort, 0, frameDecoderChannelHandler, serviceHandlers);
    }


    /**
     * 从ChannelHandlerContext中拿到Channel
     *
     * @param ctx
     * @return
     */
    public static Channel getChannel(ChannelHandlerContext ctx) {
        if (ctx == null) {
            return null;
        }
        return ctx.channel();
    }

    /**
     * 写数据
     *
     * @param channel
     * @param data    数据
     * @return true:执行了，false：没有执行
     */
    public static boolean writeAndFlush(Channel channel, Object data) {
        if (channel.isActive()) {
            channel.writeAndFlush(data);
            return true;
        }

        return false;
    }

    /**
     * 根据传进来的业务处理器，创建的channel初始化类
     * <p>
     * <p>
     * 那么ChannelPipeline是什么呢？我觉得可以理解为ChannelHandler的容器：
     * 一个Channel包含一个ChannelPipeline，所有ChannelHandler都会注册到ChannelPipeline中，
     * 并按顺序组织起来。
     * <p>
     * 在Netty中，ChannelEvent是数据或者状态的载体，例如传输的数据对应MessageEvent，
     * 状态的改变对应ChannelStateEvent。当对Channel进行操作时，会产生一个ChannelEvent，
     * 并发送到ChannelPipeline。ChannelPipeline会选择一个ChannelHandler进行处理。
     * 这个ChannelHandler处理之后，可能会产生新的ChannelEvent，并流转到下一个ChannelHandler
     * 例如，一个数据最开始是一个MessageEvent，它附带了一个未解码的原始二进制消息ChannelBuffer，
     * 然后某个Handler将其解码成了一个数据对象，并生成了一个新的MessageEvent，并传递给下一步进行处理。
     *
     * @param pipelineHandlers 注意：对顺序是敏感的，对于业务处理的应该放到后面，并且建议客户端和服务端的顺序一致。
     *                         数据流经管道时，用于对数据进行业务处理的对象，实际业务逻辑也就是在这些对象完成
     * @return
     */
    public static ChannelInitializer makeChannelInitializer(ChannelHandler... pipelineHandlers) {

        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
//                在ChannelPipeline中注册所有的业务处理对象，顺序是传进来的顺序
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(pipelineHandlers);
            }
        };

        return channelInitializer;
    }

    /**
     * 自动添加 ObjectEncoder 和 ObjectDecoder
     *
     * @param pipelineHandlers
     * @return
     */
    public static ChannelInitializer makeChannelInitializerByObject(ChannelHandler... pipelineHandlers) {
        //                Object解析器需要放在前面
        ObjectEncoder objectEncoder = new ObjectEncoder();

        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
//                在ChannelPipeline中注册所有的业务处理对象，顺序是传进来的顺序
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(objectEncoder);
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                pipeline.addLast(pipelineHandlers);
            }
        };

        return channelInitializer;
    }

    /**
     * 自动添加 StringEncoder 和 StringDecoder
     *
     * @param pipelineHandlers
     * @return
     */
    public static ChannelInitializer makeChannelInitializerByString(ChannelHandler... pipelineHandlers) {

        ChannelHandler[] arr = new ChannelHandler[pipelineHandlers.length + 2];
        arr[0] = new StringEncoder();
        arr[1] = new StringDecoder();

        for (int i = 2; i < arr.length; i++) {
            arr[i] = pipelineHandlers[i - 2];
        }

        return makeChannelInitializer(arr);
    }

    /**
     * @param frameDecoderChannelHandler 分隔符处理器
     * @param pipelineHandlers 业务处理器
     * @return
     */
    public static ChannelInitializer makeChannelInitializerByStringWithFrameDecoder(ChannelHandler frameDecoderChannelHandler, ChannelHandler... pipelineHandlers) {

        ChannelHandler[] arr = new ChannelHandler[pipelineHandlers.length + 3];
        arr[0] = frameDecoderChannelHandler;
        arr[1] = new StringEncoder();
        arr[2] = new StringDecoder();

        for (int i = 3; i < arr.length; i++) {
            arr[i] = pipelineHandlers[i - 3];
        }

        return makeChannelInitializer(arr);
    }

}

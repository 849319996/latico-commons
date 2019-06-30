package com.latico.commons.common.util.net;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;

/**
 * <PRE>
 * NIO的socket工具类
 *
 SocketChannel
 这里使用SocketChannel来继续探讨NIO。NIO的强大功能部分来自于Channel的非阻塞特性，
 套接字的某些操作可能会无限期地阻塞。
     例如，对accept()方法的调用可能会因为等待一个客户端连接而阻塞；
 对read()方法的调用可能会因为没有数据可读而阻塞，直到连接的另一端传来新的数据。
 总的来说，创建/接收连接或读写数据等I/O调用，都可能无限期地阻塞等待，
 直到底层的网络实现发生了什么。慢速的，有损耗的网络，或仅仅是简单的网络故障都可能导致任意时间的延迟。
 然而不幸的是，在调用一个方法之前无法知道其是否阻塞。
     NIO的channel抽象的一个重要特征就是可以通过配置它的阻塞行为，以实现非阻塞式的信道,
 channel.configureBlocking(false);

 在非阻塞式信道上调用一个方法总是会立即返回。这种调用的返回值指示了所请求的操作完成的程度。
 例如，在一个非阻塞式ServerSocketChannel上调用accept()方法，如果有连接请求来了，
 则返回客户端SocketChannel，否则返回null。

 客户端采用NIO实现，而服务端依旧使用BIO实现,或者客户端使用了BIO，服务端使用了NIO都是不受影响。
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-20 13:45
 * @Version: 1.0
 */
public class NioSocketUtils {
    private static final Logger LOG = LoggerFactory.getLogger(NioSocketUtils.class);
    /**
     * 创建一个SocketChannel
     * @param hostname IP或者主机名
     * @param port 端口
     * @return
     * @throws Exception
     */
    public static SocketChannel createSocketChannel(String hostname, int port) throws Exception {
        SocketChannel socketChannel = null;
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.connect(new InetSocketAddress(hostname,port));
        socketChannel.configureBlocking(false);

        return socketChannel;
    }

    /**
     * 创建一个SocketChannel
     * @param hostname IP或者主机名
     * @param port 端口
     * @return
     * @throws Exception
     */
    public static ServerSocketChannel createServerSocketChannel(String hostname, int port) throws Exception {

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(true);
        if (hostname == null || "".equals(hostname.trim())) {
            ssc.socket().bind(new InetSocketAddress(port));
        } else {
            ssc.socket().bind(new InetSocketAddress(hostname, port));
        }

        ssc.configureBlocking(false);

        return ssc;
    }

    public static void close(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * UDP的使用
     * @param localHost
     * @param localPort
     * @return
     * @throws Exception
     */
    public static DatagramChannel createDatagramChannel(String localHost, int localPort) throws Exception {
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(true);
            if (localHost == null || "".equals(localHost.trim())) {
                channel.socket().bind(new InetSocketAddress(localPort));
            } else {
                channel.socket().bind(new InetSocketAddress(localHost, localPort));
            }
            channel.configureBlocking(false);
        } catch (Exception e) {
            throw e;
        }

        return channel;
    }
    /**
     * 测试此键的通道是否已准备好接受新的套接字连接
     * @param key
     * @return
     */
    public static boolean isAcceptable(SelectionKey key) {
        try {
            return key != null && key.isValid() && key.isAcceptable();
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 测试此键的通道是否已准备好进行读取。
     * @param key
     * @return
     */
    public static boolean isReadable(SelectionKey key) {
        try {
            return key != null && key.isValid() && key.isReadable();
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }


    /**
     * 测试此键的通道是否已准备好进行写入。
     * @param key
     * @return
     */
    public static boolean isWritable(SelectionKey key) {
        try {
            return key != null && key.isValid() && key.isWritable();
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }
    /**
     * 测试此键的通道是否已完成其套接字连接操作。
     * @param key
     * @return
     */
    public static boolean isConnectable(SelectionKey key) {
        try {
            return key != null && key.isValid() && key.isConnectable();
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }
}

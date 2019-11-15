package com.latico.commons.common.util.net;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.net.HostAndPort;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 组播工具类
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-07-19 21:45:39
 * @Version: 1.0
 */
public class MultiCastUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MultiCastUtils.class);

    /**
     * 创建一个组播Socket
     *
     * @param ttl
     * @return
     * @throws IOException
     */
    public static MulticastSocket createMulticastSocket(Integer ttl) throws IOException {
        MulticastSocket ms = new MulticastSocket();
        if (ttl != null && ttl >= 1) {
            ms.setTimeToLive(ttl);
        }
        return ms;
    }

    /**
     * 关闭MulticastSocket
     *
     * @param multicastSocket
     */
    public static void close(MulticastSocket multicastSocket) {
        if (multicastSocket != null) {
            multicastSocket.close();
        }
    }

    /**
     * 关闭MulticastSocket
     *
     * @param multicastSocket
     */
    public static void leaveGroup(MulticastSocket multicastSocket, InetAddress mcastaddr) {
        if (multicastSocket != null) {
            try {
                multicastSocket.leaveGroup(mcastaddr);
            } catch (Exception e) {
                LOG.error("", e);
            }
        }
    }


    /**
     * 向指定的组播地址和端口发送组播数据
     *
     * @param multicastSocket 组播socket
     * @param groupIp         组播的IP
     * @param port            端口
     * @param message         报文
     * @throws IOException
     */
    public static void send(MulticastSocket multicastSocket, String groupIp, int port, byte[] message) throws IOException {

        InetSocketAddress inetSocketAddress = new InetSocketAddress(groupIp, port);
        DatagramPacket datagramPacket = new DatagramPacket(message, message.length, inetSocketAddress);
        multicastSocket.send(datagramPacket);
    }

    /**
     * 向指定的组播地址和端口发送组播数据
     *
     * @param multicastSocket 组播socket
     * @param groupIp         组播的IP
     * @param port            端口
     * @param message         报文
     * @throws IOException
     */
    public static void send(MulticastSocket multicastSocket, String groupIp, int port, String message, String charset) throws IOException {
        send(multicastSocket, groupIp, port, message.getBytes(charset));
    }

    /**
     * 启动监听接收
     * 该方法会阻塞，如果不想阻塞，调用者可以使用一个线程进行启动
     *
     * @param groupIp      组播IP
     * @param port         端口
     * @param bufferSize   接收的报文的字节大小
     * @param switchStatus 开关，true：监听，stop：关闭，外界传入，并由外界控制
     * @param receiveQueue 接收队列，外界传入，并由外界控制从里面读取数据
     * @throws Exception
     */
    public static void startReceive(String groupIp, int port, int bufferSize, AtomicBoolean switchStatus, Queue<DatagramPacket> receiveQueue) throws Exception {
        MulticastSocket ms = null;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(groupIp, port);
        try {

            ms = new MulticastSocket(port);
            ms.joinGroup(inetSocketAddress.getAddress());
            ms.setSoTimeout(5000);

            while (switchStatus.get()) {

                try {
                    byte[] message = new byte[bufferSize];
                    DatagramPacket packet = new DatagramPacket(message, message.length);
                    ms.receive(packet);
                    receiveQueue.add(packet);
                } catch (IOException e) {
                    LOG.error("", e);
                }
            }
        } finally {
            leaveGroup(ms, inetSocketAddress.getAddress());
            close(ms);
        }
    }

}
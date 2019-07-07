package com.latico.commons.common.util.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <PRE>
 * TCP协议的socket工具
 *
 * 一、TCP是长连接socket
 * 1、服务端：监听本地端口，看是否有客户端连接进来，成功连接进来，会获得一个跟客户端保持连接的socket对象；
 * 2、客户端：指定服务端的IP和端口，创建连接，会获得一个跟服务端连接的socket。
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-15 10:12
 * @Version: 1.0
 */
public class TcpSocketUtils {

    /**
     * 创建socket
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    public static Socket createSocket(String ip, int port) throws Exception {
        return createSocket(ip, port, -1, -1);
    }

    /**
     * @param ip 服务端IP
     * @param port 服务端端口
     * @param socketTimeout 空闲会话超时，单位毫秒，传入大于等于1时才有效
     * @param readBufferSize 读取缓冲区大小，单位字节，传入大于等于1时才有效
     * @return
     * @throws IOException
     */
    public static Socket createSocket(String ip, int port, int socketTimeout, int readBufferSize) throws Exception {
        Socket socket = null;
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("端口不合法:" + port);
        }
        socket = new Socket(ip, port);
        if (socketTimeout >= 1) {
            socket.setSoTimeout(socketTimeout);
        }

        if (readBufferSize >= 1) {
            socket.setReceiveBufferSize(readBufferSize);
        }

        return socket;
    }

    /**
     * 创建ServerSocket,监听所有IP
     * @param port 指定监听的端口
     * @return
     * @throws IOException
     */
    public static ServerSocket createServerSocket(int port) throws Exception {
        return createServerSocket(null, port);
    }

    /**
     * 创建ServerSocket
     * @param ip 指定IP，可以为空，为空就监听本机所有IP，一般都是为空
     * @param port 指定监听的端口
     * @return
     * @throws IOException
     */
    public static ServerSocket createServerSocket(String ip, int port) throws Exception {
        boolean listenAllIP = false;
        if (ip == null || "".equals(ip.trim())) {
            listenAllIP = true;
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("端口不合法:" + port);
        }
        InetSocketAddress socket = (listenAllIP ? new InetSocketAddress(port) : new InetSocketAddress(ip, port));
        ServerSocket socketServer = new ServerSocket();
        socketServer.bind(socket);

        return socketServer;
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(ServerSocket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}

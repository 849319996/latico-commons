package com.latico.commons.net.socket.io.tcp.server;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.TcpSocketUtils;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 接收到客户端连接后，创建一个SocketHandler放进队列，使用者通过get方法获取SocketHandler
 *
 * 使用的时候，假如客户端关闭了连接，但是服务端是感知不到的，所以服务端要定时发送心跳信息给客户端，
 * 字符串和对象形式都可以,可以使用一个线程定时发送。
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-16 17:16
 * @Version: 1.0
 */
public class TcpSocketServer implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(TcpSocketServer.class);
    /**
     * 总状态
     */
    private AtomicBoolean status = new AtomicBoolean(false);

    private String localHost;
    private int localPort;
    private int serverQueueSize;
    private ServerSocket serverSocket;
    private int maxConnQueueSize = 50;
    /**
     * 客户端连接socket超时，单位毫秒
     */
    private int clientSocketTimeout = 300000;

    /**
     * 跟客户端连接的缓存队列，让外界取任务
     */
    private BlockingQueue<Socket> clientSocketQueue;

    public TcpSocketServer(int localPort) {
        this(null, localPort, 1000, 300000);
    }
    public TcpSocketServer(String localHost, int localPort) {
        this(localHost, localPort, 1000, 300000);
    }
    /**
     *
     * @param localHost 指定只监听本端的某个IP
     * @param localPort 指定监听本端的端口
     * @param serverQueueSize       服务端的队列大小,装跟客户端连接请求
     * @param clientSocketTimeout
     */
    public TcpSocketServer(String localHost, int localPort, int serverQueueSize, int clientSocketTimeout) {
        this.localHost = localHost;
        this.localPort = localPort;
        this.serverQueueSize = serverQueueSize;
        this.clientSocketTimeout = clientSocketTimeout;

        clientSocketQueue = new ArrayBlockingQueue<>(this.serverQueueSize);

    }

    /**
     * 创建服务端socket
     */
    private void createServerSocket(){
        try {
            if (localHost == null || "".equals(localHost)) {
                serverSocket = new ServerSocket(this.localPort, maxConnQueueSize);
            }else{
                serverSocket = new ServerSocket(this.localPort, maxConnQueueSize, InetAddress.getByName(localHost));

            }
            LOG.info("创建ServerSocket成功");
            status.set(true);
        } catch (Exception e) {
            LOG.error(e);
            status.set(false);
        }

    }

    @Override
    public void run() {
        try {
            //创建ServerSocket
            createServerSocket();
            startListen();
        } catch (Exception e) {
            LOG.error(e);
            status.set(false);
        }finally {
            close();
        }
        LOG.info("线程停止");
    }

    /**
     * 启动监听客户端连接，然后把跟客户端连接的Socket对象包装成TcpSocketHandler放进队列
     *
     * @throws Exception
     */
    private void startListen() throws Exception {

        while (isValid()) {
            LOG.info("ServerSocket监听客户端连接...");
            Socket socketConnClient = serverSocket.accept();
            socketConnClient.setSoTimeout(clientSocketTimeout);

            if (socketConnClient != null) {
                clientSocketQueue.add(socketConnClient);
                LOG.info("有新的客户端进来:[{}], 当前未处理Socket数量[{}]", socketConnClient.getRemoteSocketAddress(), clientSocketQueue.size());
            }
        }
    }

    public boolean isValid() {
        return status.get() && !serverSocket.isClosed() && serverSocket.isBound();
    }

    /**
     * 获取一个与客户端连接的Socket
     * 无限等待获取一个数据为止
     * @return
     */
    public Socket getAndRemoveSocket() {

        try {
            while (isValid()) {
                Socket handler = this.clientSocketQueue.poll(3, TimeUnit.SECONDS);
                if (handler != null) {
                    return handler;
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 获取一个与客户端连接的SocketHandler
     * @param timeout 超时，毫秒
     * @return
     */
    public Socket getAndRemoveSocket(long timeout) {

        try {
            return this.clientSocketQueue.poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 关闭服务端，同时关闭跟客户端的连接
     */
    public void close() {
        status.set(false);
        TcpSocketUtils.close(serverSocket);

        //关闭跟客户端的剩余连接
        while (clientSocketQueue.size() >= 1) {
            Socket tcpSocketHandler = clientSocketQueue.poll();
            if (tcpSocketHandler != null) {
                TcpSocketUtils.close(tcpSocketHandler);
            }
        }
    }

    public void startAsThread() {
        if (isValid()) {
            new Thread(this).start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
    }
}

package com.latico.commons.net.socket.io.tcp.server.handler;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.pool.ThreadPool;
import com.latico.commons.net.socket.io.tcp.common.SocketHandler;
import com.latico.commons.net.socket.io.tcp.server.TcpSocketServer;
import com.latico.commons.net.socket.io.tcp.server.handler.service.TcpServerServiceTask;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 服务端处理器，该类非必须，只是为了方便使用
 * 主要功能，启动服务端，然后轮询服务端队列拿任务，然后放进线程池中执行任务
 * </PRE>
 使用示例：
 TcpSocketServer tcpSocketServer = new TcpSocketServer(7878, 100, 100);
 TcpServerHandler tcpServerHandler = new TcpServerHandler(10, tcpSocketServer, TcpServerServiceTaskExample.class);
 tcpServerHandler.start();
 try {Thread.sleep(5000000);} catch (InterruptedException e) {}

 *
 * @Author: latico
 * @Date: 2019-01-17 9:57
 * @Version: 1.0
 */
public class TcpServerHandler implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerHandler.class);
    private AtomicBoolean status = new AtomicBoolean(false);
    private ThreadPool threadPool;
    private final TcpSocketServer tcpSocketServer;

    private Class<? extends TcpServerServiceTask> tcpServerServiceTaskClass;
    private final int threadPoolSize;
    private final int threadPoolWorkQueueSize;
    private int receiveQueueSize;
    private boolean isTransferObject;
    private String charset;

    public TcpServerHandler(TcpSocketServer tcpSocketServer, Class<? extends TcpServerServiceTask> tcpServerServiceTaskClass) {
        this(tcpSocketServer, tcpServerServiceTaskClass, 10, 1, 10000, false, "UTF-8");
    }
    public TcpServerHandler(TcpSocketServer tcpSocketServer,
                            Class<? extends TcpServerServiceTask> tcpServerServiceTaskClass,
                            int threadPoolSize, int threadPoolWorkQueueSize,
                            int receiveQueueSize, boolean isTransferObject, String charset) {
        this.threadPoolSize = threadPoolSize;
        this.threadPoolWorkQueueSize = threadPoolWorkQueueSize;

        this.receiveQueueSize = receiveQueueSize;
        this.isTransferObject = isTransferObject;
        this.charset = charset;
        this.tcpSocketServer = tcpSocketServer;
        this.tcpServerServiceTaskClass = tcpServerServiceTaskClass;
    }
    @Override
    public void run() {

        LOG.info("启动TcpServerHandler");
        if (!tcpSocketServer.isValid()) {
            LOG.error("服务器启动失败，TcpServerHandler线程结束");
            close();
            return;
        }

        this.threadPool = new ThreadPool(threadPoolSize, threadPoolSize, 30000, threadPoolWorkQueueSize);

        try {
            //启动服务器
            tcpSocketServer.startAsThread();

            status.set(true);

            //一分钟打印一次监控线程日志
            final long printLogTime = 60000;
            final long printLogInterval = 5000;
            final long printLogCount = printLogTime/printLogInterval - 1;
            long count = 0;

            while (isValid()) {

                Socket socket = tcpSocketServer.getAndRemoveSocket(5000);

                if (socket != null) {
                    SocketHandler socketHandler = new SocketHandler(socket, false, receiveQueueSize, isTransferObject, charset);
                    TcpServerServiceTask serviceHandler = createServiceTask(socketHandler);
                    if (serviceHandler != null) {
                        LOG.info("TCP服务端任务线程池添加任务:{}", socketHandler.getSocket().getRemoteSocketAddress());
                        threadPool.execute(serviceHandler);
                    }

                }else{
                    //没有取到任务就打印日志证明还线程在执行
                    if (count == 0) {
                        LOG.info("TcpServerHandler监听TcpSocketServer队列,获取TcpSocketHandler");
                    }

                    //判断是否打印日志
                    if (count >= printLogCount) {
                        count = 0;
                    }else{
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e);
        } finally {
            close();
        }

        LOG.info("TcpServerHandler线程结束");
    }

    public boolean isValid() {
        return status.get() && tcpSocketServer.isValid();
    }

    /**
     * 创建任务
     * @param socketHandler
     * @return
     */
    private TcpServerServiceTask createServiceTask(SocketHandler socketHandler) throws IllegalAccessException, InstantiationException {
        TcpServerServiceTask tcpServerServiceTask = tcpServerServiceTaskClass.newInstance();
        tcpServerServiceTask.setSocketHandler(socketHandler);
        return tcpServerServiceTask;
    }

    /**
     * 关闭线程池，关闭服务端
     */
    public void close() {
        status.set(false);
        threadPool.shutdownNow();
        tcpSocketServer.close();
    }

    /**
     * 启动线程
     *
     * @return
     */
    public boolean startAsThread() {
        if (status.get()) {
            LOG.warn("线程已经启动");
            return false;
        } else {
            new Thread(this).start();
            return true;
        }
    }
}

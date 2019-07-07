package com.latico.commons.net.socket.nio.tcp.server.handler.service;

import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.NioSocketUtils;
import com.latico.commons.net.socket.nio.tcp.common.SelectionKeyHandler;
import com.latico.commons.net.socket.nio.tcp.server.handler.TcpNioSocketServerHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-21 13:34
 * @Version: 1.0
 */
public abstract class AbstracTcpNioServerServiceTask implements TcpNioServerServiceTask {
    private static final Logger LOG = LoggerFactory.getLogger(AbstracTcpNioServerServiceTask.class);
    protected AtomicBoolean status = new AtomicBoolean(false);
    protected SelectionKeyHandler selectionKeyHandler;
    protected TcpNioSocketServerHandler nioSocketServerHandler;
    /**
     * 1KB
     */
    protected static final int buffSize = 1024;
    protected String charset = "UTF-8";
    protected int receiveQueueSize = 100000;

    /**
     * 完成标志
     */
    protected AtomicBoolean finished = new AtomicBoolean(false);

    @Override
    public void setSelectionKeyHandler(SelectionKeyHandler selectionKeyHandler) {

        this.selectionKeyHandler = selectionKeyHandler;
    }

    @Override
    public void setCharset(String charset) {

        this.charset = charset;
    }

    @Override
    public boolean isFinished() {

        return finished.get();
    }

    @Override
    public void run() {

        status.set(true);
        //启动接收数据和处理
        LOG.info("启动TcpNioServerServiceTask接收数据进行处理");

        try {
            dealService();
        } catch (Exception e) {
            LOG.error("处理发送异常，直接关闭跟客户端的连接", e);
            //处理发送异常，直接关闭跟客户端的连接
            close();
        }
        status.set(false);
        LOG.debug("{}:完成一次交互", getTaskId());
        finished.set(true);
    }

    /**
     * 处理业务
     */
    protected void dealService() throws Exception {
        SelectionKey key = selectionKeyHandler.getSelectionKey();
        if (isAcceptable(key)) {
            handleAccept(key);
        }
        if (isConnectable(key)) {
            LOG.info("连接事件处理完成:{}", getTaskId());
        }
        if (isReadable(key)) {
            handleRead(key);
        }
        if (isWritable(key)) {
            handleWrite(key);
        }
    }

    /**
     * 读取任务事件
     *
     * @param key
     */
    protected abstract void handleRead(SelectionKey key) throws Exception;

    /**
     * 如果调用了该方法，会端口跟客户端的连接
     */
    @Override
    public void close() {
        status.set(false);
        try {
            IOUtils.close(this.selectionKeyHandler.getSelectionKey().channel());
        } catch (Exception e) {
        }
        LOG.info("{}关闭跟客户端的连接", getTaskId());
        finished.set(true);
    }

    @Override
    public String getTaskId() {
        return selectionKeyHandler.getSelectionKey().hashCode() + ":" + this.hashCode() + "";
    }

    protected void handleAccept(SelectionKey key) throws Exception {
        LOG.info("{}:连接事件", getTaskId());
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);

//        接收成功，把状态修改成读状态
        sc.register(key.selector(), SelectionKey.OP_READ);
        LOG.info("{}:连接完成,对方{}", getTaskId(), sc.getRemoteAddress());
    }

    protected void handleWrite(SelectionKey key) throws IOException {

        SocketChannel sc = (SocketChannel) key.channel();

        LOG.info("{}:写事件,对方:{}", getTaskId(), sc.getRemoteAddress());

        ByteBuffer buf = Charset.forName(charset).encode("响应数据");
        while (buf.hasRemaining()) {
            sc.write(buf);
        }
    }

    protected boolean isWritable(SelectionKey key) {
        return NioSocketUtils.isWritable(key);
    }

    protected boolean isReadable(SelectionKey key) {
        return NioSocketUtils.isReadable(key);
    }

    protected boolean isConnectable(SelectionKey key) {
        return NioSocketUtils.isConnectable(key);
    }

    protected boolean isAcceptable(SelectionKey key) {
        return NioSocketUtils.isAcceptable(key);
    }

    @Override
    public void setReceiveQueueSize(int receiveQueueSize) {

        this.receiveQueueSize = receiveQueueSize;
    }
}

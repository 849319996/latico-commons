package com.latico.commons.net.socket.nio.tcp.server;

import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.NioSocketUtils;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <PRE>
 * 只负责本地端口绑定监听和向外提供SelectionKey，
 * SelectionKey：选择键，跟一个客户端连接的保持者
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-20 20:08
 * @Version: 1.0
 */
public class TcpNioSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(TcpNioSocketServer.class);

    private final String localHost;
    private final int localPort;
    private final String charset;
    private AtomicBoolean status = new AtomicBoolean(false);

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    public TcpNioSocketServer(int localPort) {
        this(null, localPort);
    }

    public TcpNioSocketServer(String localHost, int localPort) {
        this(localHost, localPort, "UTF-8");
    }

    public TcpNioSocketServer(String localHost, int localPort, String charset) {

        this.localHost = localHost;
        this.localPort = localPort;
        this.charset = charset;

        try {
            createSocket();
            status.set(true);
        } catch (Exception e) {
            LOG.error(e);
            close();
        }

    }

    private void createSocket() throws Exception {
        selector = Selector.open();
        serverSocketChannel = NioSocketUtils.createServerSocketChannel(localHost, localPort);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public boolean isValid() {
        return status.get();
    }

    /**
     * 无限等待从选择器中获取所有的SelectionKey
     *
     * @return
     */
    public Set<SelectionKey> getAllSelectionKey() {
        if (!isValid()) {
            return null;
        }
        try {
            if (selector.select() >= 1) {
                return selector.selectedKeys();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 指定超时时间从选择器中获取所有的SelectionKey
     *
     * @param timeout
     * @return
     */
    public Set<SelectionKey> getAllSelectionKey(long timeout) {
        if (!isValid()) {
            return null;
        }
        try {
            if (selector.select(timeout) >= 1) {
                return selector.selectedKeys();
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    public void close() {
        status.set(false);
        IOUtils.close(selector);
        IOUtils.close(serverSocketChannel);
    }

    public boolean getStatus() {
        return status.get();
    }
}


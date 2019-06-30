/*
 *	This file is part of dhcp4java, a DHCP API for the Java language.
 *	(c) 2006 Stephan Hadinger
 *
 *	This library is free software; you can redistribute it and/or
 *	modify it under the terms of the GNU Lesser General Public
 *	License as published by the Free Software Foundation; either
 *	version 2.1 of the License, or (at your option) any later version.
 *
 *	This library is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *	Lesser General Public License for more details.
 *
 *	You should have received a copy of the GNU Lesser General Public
 *	License along with this library; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.latico.commons.net.dhcp.server;

import com.latico.commons.net.dhcp.server.common.DhcpConstants;
import com.latico.commons.net.dhcp.server.exception.DHCPServerInitException;
import com.latico.commons.net.dhcp.server.handler.DhcpServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * DhcpServerThread server = DhcpServerThread.createDhcpServer(new DhcpServiceHandlerImplExample());
 * server.startAsThread();
 * //停止
 * server.stopServer();
 *
 * </pre>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @version <B>V1.0 2018年7月31日</B>
 * @since <B>JDK1.6</B>
 */
public class DhcpServerThread implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DhcpServerThread.class);

    /**
     * the dhcpServiceHandler it must run
     */
    protected DhcpServiceHandler dhcpServiceHandler;
    /**
     * working threads pool.
     */
    protected ThreadPoolExecutor threadPool;
    /**
     * Consolidated parameters of the server.
     */
    protected Properties properties;
    /**
     * Reference of user-provided parameters
     */
    protected Properties userProps;
    /**
     * IP address and port for the server
     */
    private InetSocketAddress sockAddress = null;
    /**
     * The codec for receiving and sending.
     */
    private DatagramSocket serverSocket;
    /**
     * do we need to stop the server?
     */
    private boolean stopped = false;

    /**
     * Constructor
     *
     * <p>Constructor shall not be called directly. New servers are created through
     * <tt>initServer()</tt> factory.
     *
     * @throws DHCPServerInitException
     */
    public DhcpServerThread(DhcpServiceHandler dhcpServiceHandler) throws DHCPServerInitException {
        this.dhcpServiceHandler = dhcpServiceHandler;

        LOG.info("Server init");
        if (dhcpServiceHandler == null) {
            throw new IllegalArgumentException("初始化异常，dhcpServiceHandler must not be null");
        }
        init();
    }

    /**
     * Initialize the server context from the Properties, and open codec.
     */
    protected void init() throws DHCPServerInitException {
        if (this.serverSocket != null) {
            throw new IllegalStateException("Server already initialized");
        }

        try {

            // load codec address, this method may be overriden
            sockAddress = this.getInetSocketAddress(DhcpdConfig.getInstance().getServerListenSocket());
            if (sockAddress == null) {
                throw new DHCPServerInitException("Cannot find which SockAddress to open");
            }

            // open codec for listening and sending
            if (sockAddress.toString().contains("127.0.0.1")) {
                this.serverSocket = new DatagramSocket(DhcpConstants.BOOTP_REQUEST_PORT);

            } else {
                this.serverSocket = new DatagramSocket(null);
                this.serverSocket.bind(sockAddress);
            }
            serverSocket.setReuseAddress(true);
            // initialize Thread Pool
            int numThreads = Integer.valueOf(DhcpdConfig.getInstance().getThreadPoolSize());
            int maxThreads = Integer.valueOf(DhcpdConfig.getInstance().getThreadPoolMaxSize());
            int keepaliveThreads = Integer.valueOf(DhcpdConfig.getInstance().getThreadPoolKeepAlive());
            int requestQueueSize = Integer.valueOf(DhcpdConfig.getInstance().getThreadPoolQueueSize());
            this.threadPool = new ThreadPoolExecutor(numThreads, maxThreads,
                    keepaliveThreads, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(requestQueueSize),
                    new ServerThreadFactory());
            this.threadPool.prestartAllCoreThreads();

            // now intialize the dhcpServiceHandler
            this.dhcpServiceHandler.setServer(this);
            this.dhcpServiceHandler.init(this.properties);
        } catch (DHCPServerInitException e) {
            throw e;        // transparently re-throw
        } catch (Exception e) {
            this.serverSocket = null;
            LOG.error("Cannot open codec", e);
            throw new DHCPServerInitException("Unable to init server", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    protected void dispatch() {
        LOG.debug("服务器监听等待DHCP数据包...");
        try {
            DatagramPacket requestDatagram = new DatagramPacket(
                    new byte[DhcpdConfig.PACKET_SIZE], DhcpdConfig.PACKET_SIZE);

            // receive datagram
            this.serverSocket.receive(requestDatagram);

            LOG.info("收到DHCP请求报文,来源:({}:{})", requestDatagram.getAddress(), requestDatagram.getPort());

//            把报文进行处理
            // send work to thread pool
            DhcpHandlerThread dispatcher = new DhcpHandlerThread(this, dhcpServiceHandler, requestDatagram);
            threadPool.execute(dispatcher);
        } catch (Exception e) {
            LOG.error("等待接收报文时发生异常", e);
        }
    }

    /**
     * 服务器发送一个包出去
     * Send back response packet to client.
     *
     * <p>This is a callback method used by dhcpServiceHandler dispatchers to send back responses.
     */
    public void sendResponseToClient(DatagramPacket responseDatagram) {
        if (responseDatagram == null) {
            return;
        }
        String hostAddress = responseDatagram.getAddress().getHostAddress();
        LOG.info("DHCP发送数据包到:[{}:{}]", hostAddress, responseDatagram.getPort());
        try {
            this.serverSocket.send(responseDatagram);
        } catch (IOException e) {
            LOG.error("发包给客户端的时候发生异常", e);
        }
    }

    /**
     * Returns the <tt>InetSocketAddress</tt> for the server (client-side).
     *
     * <pre>
     *
     *  serverAddress (default 127.0.0.1)
     *  serverPort (default 67)
     *
     * </pre>
     *
     * <p>
     * This method can be overriden to specify an non default codec behaviour
     *
     * @return the codec address, null if there was a problem
     */
    protected InetSocketAddress getInetSocketAddress(String serverAddress) {
        if (serverAddress == null) {
            throw new IllegalStateException("Cannot load SERVER_ADDRESS property");
        }
        return parseSocketAddress(serverAddress);
    }

    /**
     * Parse a string of the form 'server:port' or '192.168.1.10:67'.
     *
     * @param address string to parse
     * @return InetSocketAddress newly created
     * @throws IllegalArgumentException if unable to parse string
     */
    protected static InetSocketAddress parseSocketAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException("Null address not allowed");
        }
        int index = address.indexOf(':');
        if (index <= 0) {
            throw new IllegalArgumentException("semicolon missing for port number");
        }

        String serverStr = address.substring(0, index);
        String portStr = address.substring(index + 1, address.length());
        int port = Integer.parseInt(portStr);

        return new InetSocketAddress(serverStr, port);
    }

    /**
     * This is the main loop for accepting new request and delegating work to
     * servlets in different threads.
     */
    @Override
    public void run() {
        if (this.serverSocket == null) {
            throw new IllegalStateException("Listening codec is not open - terminating");
        }
        LOG.info("dhcp server start...");
        while (!this.stopped) {
            try {
                this.dispatch();        // do the stuff
            } catch (Exception e) {
                LOG.error("Unexpected Exception", e);
            }
        }
        LOG.info("dhcp server stop");
    }

    /**
     * This method stops the server and closes the codec.
     */
    public void stopServer() {
        this.stopped = true;
        this.serverSocket.close();        // this generates an exception when trying to receive
    }

    private static class ServerThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        ServerThreadFactory() {
            this.namePrefix = "DhcpServerThread-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, this.namePrefix + this.threadNumber.getAndIncrement());
        }
    }

    /**
     * @return Returns the codec address.
     */
    protected InetSocketAddress getSockAddress() {
        return sockAddress;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * 作为一个线程的方式启动
     */
    public void startAsThread() {
        new Thread(this).start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}


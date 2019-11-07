package com.latico.commons.net.syslog.server;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.thread.AbstractThread;
import com.latico.commons.net.syslog.SyslogUtils;
import org.graylog2.syslog4j.server.*;
import org.graylog2.syslog4j.server.impl.AbstractSyslogServer;
import org.graylog2.syslog4j.server.impl.event.printstream.PrintStreamSyslogServerEventHandler;
import org.graylog2.syslog4j.util.SyslogUtility;

import java.io.IOException;
import java.net.SocketAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 继承该类，复写方法自定义处理事件{@link SyslogServerThread#eventHandle(org.graylog2.syslog4j.server.SyslogServerIF, java.net.SocketAddress, org.graylog2.syslog4j.server.SyslogServerEventIF)}
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-31 18:11:00
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
public class SyslogServerThread extends AbstractThread {
    private static final Logger LOG = LoggerFactory.getLogger(SyslogServerThread.class);
    private String host = "127.0.0.1";
    private int port = 514;
    /**
     * udp/tcp
     */
    private String protocol = "udp";

    /**
     * 开关状态
     */
    private SyslogServerIF syslogServer;


    public SyslogServerThread(AtomicBoolean appStatus) {
        super("SyslogServer", appStatus);
    }

    public SyslogServerThread(String host, int port, AtomicBoolean appStatus) {
        super("SyslogServer", appStatus);
        this.host = host;
        this.port = port;
    }

    @Override
    protected void dealProcess() throws Exception {

        this.syslogServer = org.graylog2.syslog4j.server.SyslogServer.getInstance(protocol);
        SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();
        syslogServerConfig.setHost(host);
        syslogServerConfig.setPort(port);

        SyslogServerSessionlessEventHandlerIF eventHandler = getEventHandler();

        syslogServerConfig.addEventHandler(eventHandler);
        org.graylog2.syslog4j.server.SyslogServer.getThreadedInstance(protocol);

        //线程不能停，可以加判断逻辑，什么时候让线程停止
        while (isRunning()) {
            SyslogUtility.sleep(3000);
        }
    }


    /**
	 * TODO
	 * 复写该方法
     * @param syslogServer
     * @param socketAddress
     * @param event
     */
    protected void eventHandle(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
		Date date = (event.getDate() == null ? new Timestamp(System.currentTimeMillis()) : new Timestamp(event.getDate().getTime()));
		String facility = SyslogUtility.getFacilityString(event.getFacility());;
		String level = SyslogUtility.getLevelString(event.getLevel());

		LOG.debug("收到Syslog消息,客户端地址:[{}] 主机:[{}] facility:[{}] 时间:[{}] 等级:[{}] 报文:[{}]", socketAddress, event.getHost(), facility, date, level, event.getMessage());
    }

    private SyslogServerSessionlessEventHandlerIF getEventHandler() {
        return new SyslogServerSessionlessEventHandlerIF() {

            @Override
            public void initialize(SyslogServerIF syslogServer) {

            }

            @Override
            public void destroy(SyslogServerIF syslogServer) {

            }

            @Override
            public void event(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
                eventHandle(syslogServer, socketAddress, event);
            }

            @Override
            public void exception(SyslogServerIF syslogServer, SocketAddress socketAddress, Exception exception) {

            }
        };
    }
}

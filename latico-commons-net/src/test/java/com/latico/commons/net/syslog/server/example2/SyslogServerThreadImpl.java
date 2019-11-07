package com.latico.commons.net.syslog.server.example2;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.net.syslog.server.SyslogServerThread;
import org.graylog2.syslog4j.server.SyslogServerEventIF;
import org.graylog2.syslog4j.server.SyslogServerIF;

import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-07 10:50
 * @Version: 1.0
 */
public class SyslogServerThreadImpl extends SyslogServerThread {

    private static final Logger LOG = LoggerFactory.getLogger(SyslogServerThreadImpl.class);
    public SyslogServerThreadImpl() {
        super(new AtomicBoolean(true));
    }

    @Override
    protected void eventHandle(SyslogServerIF syslogServer, SocketAddress socketAddress, SyslogServerEventIF event) {
        super.eventHandle(syslogServer, socketAddress, event);

        LOG.info("处理了报文:{}", event.getMessage());
    }
}

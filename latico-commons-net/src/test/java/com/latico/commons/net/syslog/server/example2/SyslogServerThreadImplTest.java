package com.latico.commons.net.syslog.server.example2;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

public class SyslogServerThreadImplTest {

    /**
     *
     */
    @Test
    public void test(){
        SyslogServerThreadImpl syslogServerThread = new SyslogServerThreadImpl();
        syslogServerThread.startThread();

        ThreadUtils.sleepSecond(100000);
    }
}
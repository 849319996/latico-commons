package com.latico.commons.net.syslog.server;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class SyslogServerThreadTest {

    /**
     *
     */
    @Test
    public void test(){
        SyslogServerThread syslogServerThread = new SyslogServerThread(new AtomicBoolean(true));
        syslogServerThread.startThread();

        ThreadUtils.sleepSecond(100000);
    }
}
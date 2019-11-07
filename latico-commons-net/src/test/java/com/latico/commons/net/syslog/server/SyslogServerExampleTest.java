package com.latico.commons.net.syslog.server;

import com.latico.commons.net.syslog.server.example.SyslogParamOptions;
import com.latico.commons.net.syslog.server.example.SyslogServerExample;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyslogServerExampleTest {

    @Test
    public void dealProcess() throws InterruptedException {
        SyslogParamOptions options = new SyslogParamOptions();
        options.setProtocol("udp");
        options.setHost("127.0.0.1");
        options.setPort("514");
        AtomicBoolean status = new AtomicBoolean(true);
        new SyslogServerExample(options, status).startThread();

        Thread.sleep(600000);

    }
    @Test
    public void start() {

        SyslogParamOptions options = new SyslogParamOptions();
        options.setFileName(".\\src\\test\\resources\\syslog\\log.log");
        options.setProtocol("udp");
        options.setPort("514");
        options.setAppend(true);
        SyslogServerExample syslogServer = new SyslogServerExample(options, new AtomicBoolean(true));
        try {
            syslogServer.startThread();

            //如果测试方法线程挂了，那就全部线程关闭，所以要睡眠
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        syslogServer.stopThread();
    }
}
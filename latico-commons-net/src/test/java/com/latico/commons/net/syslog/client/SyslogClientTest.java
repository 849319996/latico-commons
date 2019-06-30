package com.latico.commons.net.syslog.client;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SyslogClientTest {
    static SyslogClient syslogClient;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        syslogClient = new SyslogClient("127.0.0.1", 514, "UTF-8");
    }
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void debug() {
        syslogClient.debug("debug 信息");
    }

    @Test
    public void info() {
        syslogClient.info("info 信息");
    }

    @Test
    public void warn() {
        syslogClient.warn("warn 信息");
    }

    @Test
    public void error() {
        syslogClient.error("error 信息");
    }

    @Test
    public void notice() {
        syslogClient.notice("notice 信息");
    }

    @Test
    public void crit() {
        syslogClient.crit("crit 信息");
    }

    @Test
    public void alert() {
        syslogClient.alert("alert 信息");
    }

    @Test
    public void emerg() {
        syslogClient.emerg("emerg 信息");
    }
}
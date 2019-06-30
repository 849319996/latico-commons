package com.latico.commons.common.util.logging;

import org.junit.Test;

public class LogFactoryTest {

    @Test
    public void getLog() {
        LogUtils.loadLogBackConfigDefault();
        Log log = LogFactory.getLog(LogExample.class);
        log.info("abc{}", "223");
        log.info("abc{}", "223");
        log.info("abc{}", "223");
        log.info("abc{}", "223");

        log.warn("abc{}", "223");
        log.warn("abc{}", "223");
        log.warn("abc{}", "223");
        log.debug("abc{}", "223");
        log.debug("abc{}", "223");
        log.debug("abc{}", "223");
        log.error("abc{}", "223");
        log.error("abc{}", "223");
        log.error("abc{}", "223");
        System.out.println(log.getInfoCount());
        System.out.println(log.getErrorCount());
        System.out.println(log.getDebugCount());
        System.out.println(log.getWarnCount());

        log.toString();
    }

    @Test
    public void getLog1() {
        System.out.println(LogFactory.getLog(LogFactoryTest.class));
    }

    @Test
    public void selectLog4JLogging() {
    }

    @Test
    public void selectJavaLogging() {
    }
}
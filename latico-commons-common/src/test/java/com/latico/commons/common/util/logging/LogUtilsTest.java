package com.latico.commons.common.util.logging;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class LogUtilsTest {

    @Test
    public void loadLogBackConfigDefault() {
        LogUtils.loadLogBackConfigDefault();

    }

    @Test
    public void loadLogBackConfig() {
    }

    @Test
    public void loadLogBackConfigFromResources() {
    }

    @Test
    public void loadLogBackConfig1() {
    }

    @Test
    public void loadSunLoggerFromResources() {

    }

    @Test
    public void loadSunLoggerFromFile() throws Exception {
        ResourcesLoader.getAllImplClassUnderPackage(StringUtils.class, StringUtils.class.getPackage().getName());
    }

    @Test
    public void getLog() {
        LogUtils.loadLogBackConfigDefault();
        Log log = LogUtils.getLog(LogUtilsTest.class);
        log.info("jagj");
    }

    @Test
    public void getLogger() {

//        System.setProperty(LogUtils.LogTypeKey, LogTypeEnum.log4j.getLogClassName());
        LogUtils.loadLogBackConfigDefault();
        Log log = LoggerFactory.getLog(this.getClass().getName());
        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
//
//        log = LogUtils.getLogger(MainExample.class);
//        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
//
//        log = LogUtils.getLogger(ConfigExample.class);
//        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
    }

    @Test
    public void test() {
        System.setProperty(LogUtils.LogTypeKey, LogTypeEnum.logback.getLogClassName());
        //用法
        Log LOG1 = LoggerFactory.getLog(LogUtilsTest.class);
        Logger LOG2 = LoggerFactory.getLogger(LogUtilsTest.class);

        //用法2
        Log LOG3 = LogUtils.getLog(LogUtilsTest.class);
        Logger LOG4 = LogUtils.getLogger(LogUtilsTest.class);

        //用法3
        Log LOG5 = LogFactory.getLog(LogUtilsTest.class);
        Logger LOG6 = LogFactory.getLogger(LogUtilsTest.class);
    }
    @Test
    public void getLogger2() {
        LogUtils.loadLogBackConfigDefault();
        LoggerFactory.switchToLogback();
        Log log = LoggerFactory.getLog(this.getClass().getName());
        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
//
        System.out.println("===============");
        LoggerFactory.switchToCommonsLogging();
        log = LoggerFactory.getLog(this.getClass().getName());
        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
        System.out.println("===============");
        LoggerFactory.switchToJdkLog();
        log = LoggerFactory.getLog(this.getClass().getName());
        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
        System.out.println("===============");
        LoggerFactory.switchToLog4J();
        log = LoggerFactory.getLog(this.getClass().getName());
        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
//        log = LogUtils.getLogger(MainExample.class);
//        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
//
//        log = LogUtils.getLogger(ConfigExample.class);
//        log.info("{}ja{}gj{}", "2345", "uwh8", "jag286");
    }
}
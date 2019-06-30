package com.latico.commons.common.util.logging;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-27 11:47:40
 * @Version: 1.0
 */
public enum LogTypeEnum {
    console("console", -1, "console", "控制台"),
    logback("ch.qos.logback.classic.Logger", 1, "logback", "logback"),
    log4j2("org.apache.logging.log4j.Logger", 2, "log4j2", "log4j2"),
    log4j("org.apache.log4j.Logger", 3, "log4j", "log4j"),
    commonsLogging("org.apache.commons.logging.Log", 4, "commonsLogging", "apache公共包的"),
    jdkLogger("java.util.logging.Logger", 5, "jdkLogger", "JDK自带"),
    jbossLogger("org.jboss.logging.Logger", 6, "jbossLogger", "JBOSS"),
  ;
    /**
     * 日志工具的类名
     */
    private String logClassName;
    private String logTypeNameEn;
    private String logTypeNameCn;
    /**
     * 优先级，数字越低，优先级越高，建议从1开始算
     */
    private int priority;

    LogTypeEnum(String logClassName, int priority, String logTypeNameEn, String logTypeNameCn){
        this.logClassName = logClassName;
        this.priority = priority;
        this.logTypeNameEn = logTypeNameEn;
        this.logTypeNameCn = logTypeNameCn;
    }

    public String getLogClassName() {
        return logClassName;
    }

    public String getLogTypeNameEn() {
        return logTypeNameEn;
    }

    public String getLogTypeNameCn() {
        return logTypeNameCn;
    }

    public int getPriority() {
        return priority;
    }
}

package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * <PRE>
 *  logback日志实现类
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:49:59
 * @version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.logback)
public class LogbackLogImpl extends AbstractLogger {

    private static final String callerFQCN = LogbackLogImpl.class.getName();
    private static final Logger testLogger = LoggerFactory.getLogger(LogbackLogImpl.class);

    static {
        // if the logger is not a LocationAwareLogger instance, it can not get correct stack StackTraceElement
        // so ignore this implementation.
        if (!(testLogger instanceof LocationAwareLogger)) {
            throw new UnsupportedOperationException(testLogger.getClass() + " is not a suitable logger");
        }
    }

    /**
     * logback框架的具体日志对象
     */
    private LocationAwareLogger log;

    public LogbackLogImpl(LocationAwareLogger log) {
        this.log = log;
        this.loggerName = this.log.getName();
    }

    public LogbackLogImpl(String loggerName) {
        this.loggerName = loggerName;
        this.log = (LocationAwareLogger) LoggerFactory.getLogger(loggerName);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void error(Object msg, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, getString(msg), null, e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, msg, argArray, e);
        incrementErrorCount();

    }

    @Override
    public void error(Object msg) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, getString(msg), null, null);
        incrementErrorCount();
    }

    @Override
    public void error(Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, "", null, e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object... argArray) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, msg, argArray, null);
        incrementErrorCount();

    }

    @Override
    public void error(String msg, Object argArray, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.ERROR_INT, concatArgsToStr(msg, argArray), null, e);
        incrementErrorCount();
    }

    @Override
    public void info(Object msg) {
        incrementInfoCount();
        log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, getString(msg), null, null);
    }

    @Override
    public void info(Throwable e) {
        incrementInfoCount();
        log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, "", null, e);
    }

    /**
     * 参数数组的形式，用{}作为替代符号
     *
     * @param msg
     * @param argArray
     */
    @Override
    public void info(String msg, Object... argArray) {
        incrementInfoCount();
        log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, msg, argArray, null);
    }

    @Override
    public void info(String msg, Object argArray, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, concatArgsToStr(msg, argArray), null, e);
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        incrementInfoCount();
        log.log(null, callerFQCN, LocationAwareLogger.INFO_INT, msg, argArray, e);
    }

    @Override
    public void debug(Object msg) {
        incrementDebugCount();
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, getString(msg), null, null);
    }

    @Override
    public void debug(Throwable e) {
        incrementDebugCount();
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, "", null, e);
    }

    @Override
    public void debug(String msg, Object... argArray) {
        incrementDebugCount();
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, msg, argArray, null);
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, getString(msg), null, e);
    }

    @Override
    public void debug(String msg, Object argArray, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, concatArgsToStr(msg, argArray), null, e);
        incrementDebugCount();
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        log.log(null, callerFQCN, LocationAwareLogger.DEBUG_INT, msg, argArray, e);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void warn(Object msg) {
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, getString(msg), null, null);
        incrementWarnCount();
    }

    @Override
    public void warn(Throwable e) {
        incrementWarnCount();
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, "", null, e);
    }

    @Override
    public void warn(String msg, Object... argArray) {
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, msg, argArray, null);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object argArray, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, concatArgsToStr(msg, argArray), null, e);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, msg, argArray, e);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.log(null, callerFQCN, LocationAwareLogger.WARN_INT, getString(msg), null, e);
        incrementWarnCount();
    }

}

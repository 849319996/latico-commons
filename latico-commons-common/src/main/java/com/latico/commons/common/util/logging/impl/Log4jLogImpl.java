package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-27 11:50:09
 * @Version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.log4j)
public class Log4jLogImpl extends AbstractLogger {

    private static final String callerFQCN = Log4jLogImpl.class.getName();

    private Logger log;

    /**
     * @param log
     * @since 0.2.21
     */
    public Log4jLogImpl(Logger log) {
        this.log = log;
        this.loggerName = log.getName();
    }

    public Log4jLogImpl(String loggerName) {
        log = Logger.getLogger(loggerName);
        this.loggerName = loggerName;
    }

    public Logger getLog() {
        return log;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void error(Object msg, Throwable e) {
        incrementErrorCount();
        log.log(callerFQCN, Level.ERROR, msg, e);
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        incrementErrorCount();
        log.log(callerFQCN, Level.ERROR, msg, e);
    }

    @Override
    public void error(Object msg) {
        incrementErrorCount();
        log.log(callerFQCN, Level.ERROR, msg, null);
    }

    @Override
    public void error(Throwable e) {
        incrementErrorCount();
        log.log(callerFQCN, Level.ERROR, "", e);
    }

    @Override
    public void error(String msg, Object... argArray) {
        incrementErrorCount();
        log.log(callerFQCN, Level.ERROR, concatArgs(msg, argArray), null);
    }

    @Override
    public void debug(Object msg) {
        incrementDebugCount();
        log.log(callerFQCN, Level.DEBUG, msg, null);
    }

    @Override
    public void debug(Throwable e) {
        incrementDebugCount();
        log.log(callerFQCN, Level.DEBUG, "", e);
    }

    @Override
    public void debug(String msg, Object... argArray) {
        incrementDebugCount();
        log.log(callerFQCN, Level.DEBUG, concatArgs(msg, argArray), null);
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.log(callerFQCN, Level.DEBUG, msg, e);
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        log.log(callerFQCN, Level.DEBUG, concatArgs(msg, argArray), e);
    }

    @Override
    public void warn(Object msg) {
        incrementWarnCount();
        log.log(callerFQCN, Level.WARN, msg, null);
    }

    @Override
    public void warn(Throwable e) {
        incrementWarnCount();
        log.log(callerFQCN, Level.WARN, "", e);
    }

    @Override
    public void warn(String msg, Object... argArray) {
        log.log(callerFQCN, Level.WARN, concatArgs(msg, argArray), null);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.log(callerFQCN, Level.WARN, concatArgs(msg, argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.log(callerFQCN, Level.WARN, msg, e);
        incrementWarnCount();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(Object msg) {
        incrementInfoCount();
        log.log(callerFQCN, Level.INFO, msg, null);
    }

    @Override
    public void info(Throwable e) {
        incrementInfoCount();
        log.log(callerFQCN, Level.INFO, "", e);
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
        log.log(callerFQCN, Level.INFO, concatArgs(msg, argArray), null);
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        incrementInfoCount();
        log.log(callerFQCN, Level.INFO, concatArgs(msg, argArray), e);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabledFor(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isEnabledFor(Level.ERROR);
    }

    @Override
    public String toString() {
        return log.toString();
    }
}

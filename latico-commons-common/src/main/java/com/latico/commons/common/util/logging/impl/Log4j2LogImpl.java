package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <PRE>
 *
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:50:19
 * @version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.log4j2)
public class Log4j2LogImpl extends AbstractLogger {

    private Logger log;

    /**
     * @param log
     * @since 0.2.21
     */
    public Log4j2LogImpl(Logger log) {
        this.log = log;
        this.loggerName = this.log.getName();
    }

    public Log4j2LogImpl(String loggerName) {
        log = LogManager.getLogger(loggerName);
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
        log.error(msg == null ? null:msg.toString(), e);
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        incrementErrorCount();
        log.error(concatArgs(msg, argArray), e);
    }

    @Override
    public void error(Object msg) {
        incrementErrorCount();
        log.error(msg);
    }

    @Override
    public void error(Throwable e) {
        incrementErrorCount();
        log.error("", e);
    }

    @Override
    public void error(String msg, Object... argArray) {
        incrementErrorCount();
        log.error(msg == null ? null:msg.toString(), argArray);
    }

    @Override
    public void error(String msg, Object argArray, Throwable e) {

    }

    @Override
    public void debug(Object msg) {
        incrementDebugCount();
        log.debug(msg);
    }

    @Override
    public void debug(Throwable e) {
        incrementDebugCount();
        log.debug("", e);
    }

    @Override
    public void debug(String msg, Object... argArray) {
        incrementDebugCount();
        log.debug(msg == null ? null:msg.toString(), argArray);
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.debug(msg == null ? null:msg.toString(), e);
    }

    @Override
    public void debug(String msg, Object argArray, Throwable e) {
        incrementDebugCount();
        log.debug(concatArgs(msg == null ? null:msg.toString(), argArray), e);
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        log.debug(concatArgs(msg == null ? null:msg.toString(), argArray), e);
    }

    @Override
    public void warn(Object msg) {
        log.warn(msg);
        incrementWarnCount();
    }

    @Override
    public void warn(Throwable e) {
        log.warn("", e);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object... argArray) {
        log.warn(msg == null ? null:msg.toString(), argArray);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object argArray, Throwable e) {
        log.warn(concatArgs(msg == null ? null:msg.toString(), argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.warn(concatArgs(msg == null ? null:msg.toString(), argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.warn(msg == null ? null:msg.toString(), e);
        incrementWarnCount();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(Object msg) {
        incrementInfoCount();
        log.info(msg);
    }

    @Override
    public void info(Throwable e) {
        incrementInfoCount();
        log.info("", e);
    }

    /**
     * 参数数组的形式，用{}作为替代符号
     *
     * @param msg
     * @param argArray
     */
    @Override
    public void info(String msg, Object... argArray) {
        log.info(msg == null ? null:msg.toString(), argArray);
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Object argArray, Throwable e) {
        log.info(concatArgs(msg == null ? null:msg.toString(), argArray), e);
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        log.info(concatArgs(msg == null ? null:msg.toString(), argArray), e);
        incrementInfoCount();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabled(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public String toString() {
        return log.toString();
    }

}

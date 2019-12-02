package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;

import java.util.logging.Level;

/**
 * <PRE>
 *  JDK日志实现类
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:49:29
 * @Version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.jdkLogger)
public class JdkLogImpl extends AbstractLogger {

    private java.util.logging.Logger log;

    public JdkLogImpl(String loggerName) {
        this.loggerName = loggerName;
        log = java.util.logging.Logger.getLogger(loggerName);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    @Override
    public void error(Object msg, Throwable e) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), msg == null ? null:msg.toString(), e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementErrorCount();
    }

    @Override
    public void error(Object msg) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), getString(msg));
        incrementErrorCount();
    }

    @Override
    public void error(Throwable e) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), "", e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object... argArray) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray));
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object argArray, Throwable e) {
        log.logp(Level.SEVERE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementErrorCount();
    }

    @Override
    public void debug(Object msg) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), getString(msg));
    }

    @Override
    public void debug(Throwable e) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), "", e);
    }

    @Override
    public void debug(String msg, Object... argArray) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray));
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), msg == null ? null:msg.toString(), e);
    }

    @Override
    public void debug(String msg, Object argArray, Throwable e) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);

    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        log.logp(Level.FINE, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);

    }

    @Override
    public void warn(Object msg) {
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), getString(msg));
        incrementWarnCount();
    }

    @Override
    public void warn(Throwable e) {
        incrementWarnCount();
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), "", e);
    }

    @Override
    public void warn(String msg, Object... argArray) {
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray));
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object argArray, Throwable e) {
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.logp(Level.WARNING, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), getString(msg), e);
        incrementWarnCount();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isLoggable(Level.INFO);
    }

    @Override
    public void info(Object msg) {
        log.logp(Level.INFO, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), getString(msg));
        incrementInfoCount();
    }

    @Override
    public void info(Throwable e) {
        log.logp(Level.INFO, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), "", e);
        incrementInfoCount();
    }

    /**
     * 参数数组的形式，用{}作为替代符号
     *
     * @param msg
     * @param argArray
     */
    @Override
    public void info(String msg, Object... argArray) {
        log.logp(Level.INFO, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray));
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Object argArray, Throwable e) {
        log.logp(Level.INFO, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        log.logp(Level.INFO, loggerName, Thread.currentThread().getStackTrace()[1].getMethodName(), concatArgsToStr(msg, argArray), e);
        incrementInfoCount();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isLoggable(Level.WARNING);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isLoggable(Level.SEVERE);
    }

    @Override
    public String toString() {
        return log.toString();
    }
}

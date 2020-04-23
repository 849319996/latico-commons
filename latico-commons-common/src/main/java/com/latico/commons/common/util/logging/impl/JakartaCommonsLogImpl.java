package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;

/**
 * <PRE>
 * apache的日志实现类
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:48:50
 * @version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.commonsLogging)
public class JakartaCommonsLogImpl extends AbstractLogger {

    private org.apache.commons.logging.Log log;

    /**
     * @param log
     * @since 0.2.1
     */
    public JakartaCommonsLogImpl(org.apache.commons.logging.Log log) {
        this.log = log;
    }

    public JakartaCommonsLogImpl(String loggerName) {
        log = org.apache.commons.logging.LogFactory.getLog(loggerName);
        this.loggerName = loggerName;
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void error(Object msg, Throwable e) {
        log.error(msg, e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        log.error(concatArgs(msg, argArray), e);
        incrementErrorCount();
    }

    @Override
    public void error(Object msg) {
        log.error(msg);
        incrementErrorCount();
    }

    @Override
    public void error(Throwable e) {
        incrementErrorCount();
        log.error("", e);
    }

    @Override
    public void error(String msg, Object... argArray) {
        log.error(concatArgs(msg, argArray));
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object argArray, Throwable e) {
        log.error(concatArgs(msg, argArray), e);
        incrementErrorCount();
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
        log.debug(concatArgs(msg, argArray));
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.debug(msg, e);
    }

    @Override
    public void debug(String msg, Object argArray, Throwable e) {
        incrementDebugCount();
        log.debug(concatArgs(msg, argArray), e);
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        log.debug(concatArgs(msg, argArray), e);
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
        log.warn(concatArgs(msg, argArray));
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object argArray, Throwable e) {
        log.warn(concatArgs(msg, argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.warn(concatArgs(msg, argArray), e);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.warn(msg, e);
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
        log.info(concatArgs(msg, argArray));
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Object argArray, Throwable e) {
        log.info(concatArgs(msg, argArray), e);
        incrementInfoCount();
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        log.info(concatArgs(msg, argArray), e);
        incrementInfoCount();
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
    public String toString() {
        return log.toString();
    }
}

package com.latico.commons.common.util.logging.impl;


import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;
import org.jboss.logging.Logger;

/**
 * <PRE>
 * JBOSS日志实现类
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-09 17:20
 * @version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.jbossLogger)
public class JbossLogImpl extends AbstractLogger {

    private Logger log;

    public JbossLogImpl(Logger log) {
        this.log = log;
    }
    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
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
        log.debugv(msg, argArray);
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        log.debug(msg, e);
    }

    @Override
    public void debug(String msg, Object argArray, Throwable e) {
        log.debugv(e, msg, argArray);
        incrementDebugCount();
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        log.debugv(e, msg, argArray);
        incrementDebugCount();
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

    @Override
    public void info(String msg, Object... argArray) {
        incrementInfoCount();
        log.infov(msg, argArray);
    }

    @Override
    public void info(String msg, Object argArray, Throwable e) {
        incrementInfoCount();
        log.infov(e, msg, argArray);
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        incrementInfoCount();
        log.infov(e, msg, argArray);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabled(Logger.Level.WARN);
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
        log.warnv(msg, argArray);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Object argArray, Throwable e) {
        log.warnv(e, msg, argArray);
        incrementWarnCount();
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        log.warnv(e, msg, argArray);
        incrementWarnCount();
    }

    @Override
    public void warn(Object msg, Throwable e) {
        log.warn(msg, e);
        incrementWarnCount();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isEnabled(Logger.Level.ERROR);
    }

    @Override
    public void error(Object msg) {
        log.error(msg);
        incrementErrorCount();
    }

    @Override
    public void error(Throwable e) {
        log.error("", e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object... argArray) {
        log.errorv(msg, argArray);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object argArray, Throwable e) {
        log.errorv(e, msg, argArray);
        incrementErrorCount();
    }

    @Override
    public void error(Object msg, Throwable e) {
        log.error(msg, e);
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        log.errorv(e, msg, argArray);
        incrementErrorCount();
    }
}

package com.latico.commons.common.util.logging.impl;

import com.latico.commons.common.util.logging.AbstractLogger;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.LogTypeEnum;

/**
 * <PRE>
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:50:14
 * @Version: 1.0
 */
@LogImplAnnotation(LogTypeEnum.console)
public class NoLoggingLogImpl extends AbstractLogger {

    private boolean debugEnable = false;
    private boolean infoEnable = true;
    private boolean warnEnable = true;
    private boolean errorEnable = true;

    public NoLoggingLogImpl(String loggerName) {
        this.loggerName = loggerName;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnable;
    }

    @Override
    public void error(Object msg, Throwable e) {
        if (!errorEnable) {
            return;
        }
        incrementErrorCount();
        System.err.println(loggerName + " : " + msg);

        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(String msg, Throwable e, Object... argArray) {
        if (!errorEnable) {
            return;
        }
        incrementErrorCount();
        System.err.println(loggerName + " : " + concatArgs(msg, argArray));

        printThrowable(e);
    }

    private void printThrowable(Throwable e) {
        if (e != null) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void error(Object msg) {
        incrementErrorCount();
        System.err.println(loggerName + " : " + msg);
    }

    @Override
    public void error(Throwable e) {
        if (e != null) {
            e.printStackTrace(System.err);
        }
        incrementErrorCount();
    }

    @Override
    public void error(String msg, Object... argArray) {
        if (!errorEnable) {
            return;
        }
        incrementErrorCount();
        System.err.println(loggerName + " : " + concatArgs(msg, argArray));
    }

    @Override
    public void debug(Object msg) {
        incrementDebugCount();
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public void debug(Throwable e) {
        incrementDebugCount();
        e.printStackTrace(System.out);
    }

    @Override
    public void debug(String msg, Object... argArray) {
        incrementDebugCount();
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
    }

    @Override
    public void debug(Object msg, Throwable e) {
        incrementDebugCount();
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public void debug(String msg, Throwable e, Object... argArray) {
        incrementDebugCount();
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
        printThrowable(e);
    }

    @Override
    public void warn(Object msg) {
        incrementWarnCount();
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public void warn(Throwable e) {
        incrementWarnCount();
        e.printStackTrace(System.out);
    }

    @Override
    public void warn(String msg, Object... argArray) {
        incrementWarnCount();
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
    }

    @Override
    public void warn(String msg, Throwable e, Object... argArray) {
        incrementWarnCount();
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
        printThrowable(e);
    }

    @Override
    public void warn(Object msg, Throwable e) {
        incrementWarnCount();
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnable;
    }

    @Override
    public void info(Object msg) {
        incrementInfoCount();
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public void info(Throwable e) {
        incrementInfoCount();
        e.printStackTrace(System.out);
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
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
    }

    @Override
    public void info(String msg, Throwable e, Object... argArray) {
        incrementInfoCount();
        System.out.println(loggerName + " : " + concatArgs(msg, argArray));
        printThrowable(e);

    }


    @Override
    public boolean isWarnEnabled() {
        return warnEnable;
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnable;
    }

    public void setErrorEnabled(boolean value) {
        this.errorEnable = value;
    }
}

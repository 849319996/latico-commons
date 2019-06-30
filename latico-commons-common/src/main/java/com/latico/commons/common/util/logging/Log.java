package com.latico.commons.common.util.logging;

/**
 * <PRE>
 * 日志接口
 *
 1、参考以sl4j为标准，所有日志方法支持占位符，解决部分日志不支持占位符问题；
 2、使用枚举类，定义了每种日志的优先级；
 3、使用系统变量，如果在系统变量里面指定了日志类型，最优先使用；
 4、使用动态代理，进行日志方法执行的统一拦截，包括日志打印的次数，统一日志发送网络位置；
 5、使用包扫描实现类和注解标志日志具体实现类；
 6、每新增一种日志类型，只需要添加枚举和实现类，其他代码不需要改动；
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-27 11:46:56
 * @Version: 1.0
 */
public interface Log {

    /**
     * @return 是否打印debug级别
     */
    boolean isDebugEnabled();

    void debug(Object msg);

    void debug(Throwable e);

    void debug(String msg, Object... argArray);

    void debug(Object msg, Throwable e);

    void debug(String msg, Throwable e, Object... argArray);

    /**
     * @return 是否打印info级别
     */
    boolean isInfoEnabled();

    void info(Object msg);

    void info(Throwable e);

    /**
     * 参数数组的形式，用{}作为替代符号
     * @param msg
     * @param argArray
     */
    void info(String msg, Object... argArray);

    void info(String msg, Throwable e, Object... argArray);

    boolean isWarnEnabled();

    void warn(Object msg);

    void warn(Throwable e);

    void warn(String msg, Object... argArray);

    void warn(String msg, Throwable e, Object... argArray);

    void warn(Object msg, Throwable e);

    /**
     * @return
     */
    boolean isErrorEnabled();

    void error(Object msg);

    void error(Throwable e);

    void error(String msg, Object... argArray);

    void error(Object msg, Throwable e);

    void error(String msg, Throwable e, Object... argArray);


    /**
     * @return 错误计数
     */
    long getErrorCount();

    /**
     * @return 警告日志计数
     */
    long getWarnCount();

    /**
     * @return 信息日志计数
     */
    long getInfoCount();

    /**
     * @return 调试等级日志计数
     */
    long getDebugCount();

    /**
     * 重置所有计数器
     */
    void resetCount();

    /**
     * 获取日志名称
     *
     * @return
     */
    String getLoggerName();

    /**
     * @return 错误计数
     */
    long incrementErrorCount();

    /**
     * @return 警告日志计数
     */
    long incrementWarnCount();

    /**
     * @return 信息日志计数
     */
    long incrementInfoCount();

    /**
     * @return 调试等级日志计数
     */
    long incrementDebugCount();
}

package com.latico.commons.common.util.logging;

import com.latico.commons.common.util.logging.impl.NoLoggingLogImpl;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <PRE>
 *     优先使用系统属性中直接指定的日志类型，如果没有指定，那就按照优先级进行初始化
 *
 * 多功能日志检测实现工厂,
 1、先在配置类进行日志初始化：LogUtils.loadLogBackConfigDefault();
 2、初始化日志对象：private final static Log LOG = LogFactory.getLog(VersionExample.class);
 3、使用日志对象打印日志：LOG.info("123");

 //用法1
 Log LOG1 = LoggerFactory.getLog(Version.class);
 Logger LOG2 = LoggerFactory.getLogger(Version.class);

 //用法2
 Log LOG3 = LogUtils.getLog(Version.class);
 Logger LOG4 = LogUtils.getLogger(Version.class);

 //用法3
 Log LOG5 = LogFactory.getLog(Version.class);
 Logger LOG6 = LogFactory.getLogger(Version.class);
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:47:08
 * @version: 1.0
 */
public class LogFactory {

    /**
     * 日志类构造器
     */
    private static volatile Constructor logConstructor;
    /**
     * log工具的类名和我们自定义实现类映射
     */
    private static final Map<String, Class> logClassNameAndImplClassMap = new ConcurrentHashMap<>();
    /**
     * 优先级和实现类映射，需要根据键进行自然排序，对Log的初始化进行优先级排序
     */
    private static final Map<Integer, Class> logPriorityAndImplClassMap = new ConcurrentSkipListMap<>();
    static {
        //初始化
        initLogClassConstructor();
    }

    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param clazz
     * @return
     */
    public static Log getLog(Class clazz) {
        return getLog(clazz.getName());
    }

    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * 实例化日志对象,使用静态代理包装
     *
     * @param loggerName
     * @return
     */
    public synchronized static Log getLog(String loggerName) {
        return getLogger(loggerName);
    }

    /**
     * @param loggerName
     * @return
     */
    public synchronized static Logger getLogger(String loggerName) {
        if (logConstructor == null) {
            System.out.println("日志构造器未初始化，返回默认日志, 日志对象:" + loggerName);
            try {
                //初始化
                initLogClassConstructor();

                if(logConstructor == null){
                    System.out.println("日志构造器未能正常初始化, 日志对象:" + loggerName);
                }else{
                    //不能用代理类，根据日志工具搜索判断逻辑，会导致打印的行号不正确
//                    return ResourcesLoader.getLogByDynamicProxy((Logger) logConstructor.newInstance(loggerName));
//                    return new LogStaticProxyImpl((Logger)logConstructor.newInstance(loggerName));
                    return (Logger) logConstructor.newInstance(loggerName);

                }

            } catch (Throwable e) {
                e.printStackTrace(System.err);
            }finally {
                System.out.println("因为构造器尚未进行正常初始化，使用完毕，就清除日志构造器赋值状态, 日志对象:" + loggerName);
                logConstructor = null;
            }

        }else{
            try {
                //不能用代理类，根据日志工具搜索判断逻辑，会导致打印的行号不正确
//                return ResourcesLoader.getLogByDynamicProxy((Logger) logConstructor.newInstance(loggerName));
//                return new LogStaticProxyImpl((Logger)logConstructor.newInstance(loggerName));
                return (Logger) logConstructor.newInstance(loggerName);
            } catch (Throwable t) {
                throw new RuntimeException("Error creating logger for logger '" + loggerName + "'.  Cause: " + t, t);
            }
        }

        return null;
    }

    /**
     * 初始化日志类构造器
     */
    private static void initLogClassConstructor() {

        try {
            //如果已经扫描过就不扫描了
            if (logClassNameAndImplClassMap.isEmpty() || logPriorityAndImplClassMap.isEmpty()) {
                //扫描所有相关的日志实现类
                List<Class> allImplClassUnderPackage = ResourcesLoader.getAllImplClassUnderPackage(Logger.class, Logger.class.getPackage().getName());
                if (allImplClassUnderPackage != null) {
                    for (Class implClass : allImplClassUnderPackage) {
                        LogImplAnnotation annotation = ResourcesLoader.getAnnotationPresentOnClass(implClass, LogImplAnnotation.class);
                        if (annotation == null) {
                            continue;
                        }
                        logClassNameAndImplClassMap.put(annotation.value().getLogClassName(), implClass);
                        logPriorityAndImplClassMap.put(annotation.value().getPriority(), implClass);
                    }
                }
            }

            //如果系统变量中有指定日志，先使用
            String logType = System.getProperty(LogUtils.LogTypeKey);
            if (logType != null && !"".equals(logType.trim())) {
                Class implClass = logClassNameAndImplClassMap.get(logType);
                if (implClass != null) {
                    tryImplementation(logType, implClass.getName());
                }
            }

        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }

        if (logConstructor == null) {
        //使用优先级进行日志初始化
            for (Map.Entry<Integer, Class> entry : logPriorityAndImplClassMap.entrySet()) {
                try {
                    tryImplementation(ResourcesLoader.getAnnotationPresentOnClass(entry.getValue(), LogImplAnnotation.class).value().getLogClassName(), entry.getValue().getName());
                } catch (Throwable e) {

                }
            }
        }

        //如果还是没有初始化成功，那就使用控制台日志
        if (logConstructor == null) {
            try {
                logConstructor = NoLoggingLogImpl.class.getConstructor(String.class);
            } catch (Throwable e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

        System.out.println("日志构造器完成,日志实现类型:" + logConstructor.toString());
    }

    /**
     * 检测是否存在类，然后尝试去实现日志接口
     *
     * @param testClassName 要被检测的日志类
     * @param implClassName 要实现的类
     */
    private static void tryImplementation(String testClassName, String implClassName) {
        if (logConstructor != null) {
            return;
        }

        try {

            Thread.currentThread().getContextClassLoader().loadClass(testClassName);
            Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
            logConstructor = implClass.getConstructor(new Class[]{String.class});

            Class<?> declareClass = logConstructor.getDeclaringClass();
            if (!Log.class.isAssignableFrom(declareClass)) {
                logConstructor = null;
            }

            try {
                if (null != logConstructor) {
                    logConstructor.newInstance(LogFactory.class.getName());
                }
            } catch (Throwable t) {
                logConstructor = null;
            }

        } catch (Throwable t) {
            //t.printStackTrace(System.err);
        }
    }

    /**
     * 选择log4j
     */
    public static synchronized void switchToLog4J() {
        switchToLogType(LogTypeEnum.log4j);
    }

    /**
     * 选择log4j2
     */
    public static synchronized void switchToLog4J2() {
        switchToLogType(LogTypeEnum.log4j2);
    }
    /**
     * 选择Slf4j
     */
    public static synchronized void switchToLogback() {
        switchToLogType(LogTypeEnum.logback);
    }
    /**
     * 选择commonsLogging
     */
    public static synchronized void switchToCommonsLogging() {
        switchToLogType(LogTypeEnum.commonsLogging);
    }
    /**
     * 选择console,控制台打印模式
     */
    public static synchronized void switchToConsole() {
        switchToLogType(LogTypeEnum.console);
    }

    /**
     * 选择使用java自带日志
     */
    public static synchronized void switchToJdkLog() {
        switchToLogType(LogTypeEnum.jdkLogger);
    }

    /**
     * @param logEnum 要切换到这种日志模式
     */
    public static synchronized void switchToLogType(LogTypeEnum logEnum) {
        if (logEnum == null) {
            return;
        }
        try {
            //先测试是否存在
            Thread.currentThread().getContextClassLoader().loadClass(logEnum.getLogClassName());
            Class implClass = logClassNameAndImplClassMap.get(logEnum.getLogClassName());
            if (implClass != null) {
                logConstructor = implClass.getConstructor(new Class[]{String.class});
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

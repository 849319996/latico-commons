package com.latico.commons.common.util.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.latico.commons.common.config.Config;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * <PRE>
 *     可以通过启动文件添加系统属性或者下面方式设置Log
 *     System.setProperty(LogUtils.LogTypeKey, LogTypeEnum.log4j.getLogClassName());
 *
 * 日志工具
 * 1、logback的配置文件，需要利用该工具进行加载；
 * 2、jdk自带的sun的log配置也需要使用工具加载；
 * 3、log4j的只需要在资源目录的根目录下添加log4j.properties文件，会自动加载；
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:47:46
 * @version: 1.0
 */
public class LogUtils {
    /**
     * 日志类型的系统key
     */
    public static final String LogTypeKey = "Catt.LogType";

    /**
     * 配置文件根目录
     */
    public static final String CONFIG_FILE_ROOT_DIR = Config.CONFIG_FILE_ROOT_DIR;

    /**
     * 资源配置文件根目录
     */
    public static final String RESOURCES_CONFIG_FILE_ROOT_DIR = Config.RESOURCES_CONFIG_FILE_ROOT_DIR;
    /**
     * 默认的logback日志配置文件
     */
    private final static String LOGBACK_PATH_DEFAULT = CONFIG_FILE_ROOT_DIR + "logback.xml";
    /**
     * 资源路径下的默认logback配置文件
     */
    private final static String LOGBACK_RESOURCES_PATH_DEFAULT = RESOURCES_CONFIG_FILE_ROOT_DIR + "logback.xml";
    private final static String SunLoggerResourcesFile = RESOURCES_CONFIG_FILE_ROOT_DIR + "logger.properties";
    private final static String SunLoggerFile = CONFIG_FILE_ROOT_DIR + "logger.properties";

    /**
     * 私有化构造函数
     */
    protected LogUtils() {
    }

    /**
     * 添加日志类型信息到系统
     * @param logType
     */
    public static void addLogTypeInfoToSystemProperty(LogTypeEnum logType) {
        System.setProperty(LogUtils.LogTypeKey, logType.getLogClassName());
    }

    /**
     * 加载logback日志配置文件使用默认的路径(默认路径为./confconfig/logback.xml)
     * 1、先查找./config/logback.xml，如果没有找到，那就找资源目录下面的/logback.xml；
     */
    public static void loadLogBackConfigDefault() {
        File file = new File(PathUtils.adapterFilePathSupportWebContainer(LOGBACK_PATH_DEFAULT));
        if (file.exists()) {
            loadLogBackConfig(LOGBACK_PATH_DEFAULT);
        }else{
            loadLogBackConfigFromResources(LOGBACK_RESOURCES_PATH_DEFAULT);
        }
    }

    /**
     * 加载logback日志配置文件
     *
     * @param logbackConfPath 日志配置文件路径
     */
    public static void loadLogBackConfig(String logbackConfPath) {
        //添加系统属性
        System.setProperty(LogTypeKey, LogTypeEnum.logback.getLogClassName());
        if (StringUtils.isEmpty(logbackConfPath)) {
            logbackConfPath = LOGBACK_PATH_DEFAULT;
        }

        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            configurator.doConfigure(logbackConfPath);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("Fail to load logBack configure file: " + logbackConfPath);
        }


    }

    /**
     * 从资源目录加载logback配置
     *
     * @param logbackConfPath 资源目录的logback配置文件
     */
    public static void loadLogBackConfigFromResources(String logbackConfPath) {
        //添加系统属性
        System.setProperty(LogTypeKey, LogTypeEnum.logback.getLogClassName());

        if (StringUtils.isEmpty(logbackConfPath)) {
            logbackConfPath = LOGBACK_RESOURCES_PATH_DEFAULT;
        }

        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            logbackConfPath = PathUtils.formatResourcesPathForClassLoader(logbackConfPath);
            InputStream resourceAsStream = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(logbackConfPath);
            if (resourceAsStream == null) {
                System.out.println("资源目录logback日志文件不存在:" + logbackConfPath);
                return;
            }
            loadLogBackConfig(resourceAsStream);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("Fail to load logBack configure file: " + logbackConfPath);
        }

    }

    /**
     * 使用流的方式加载日志文件
     *
     * @param is 输入流
     */
    public static void loadLogBackConfig(InputStream is) {
        if (is == null) {
            return;
        }

        //添加系统属性
        System.setProperty(LogTypeKey, LogTypeEnum.logback.getLogClassName());
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();

            configurator.doConfigure(is);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("加载loopback配置异常");
        }

    }

    /**
     * 日志{@link java.util.logging.Logger}获取器
     * 默认是资源路径/logger.properties
     *
     * @param resourcePath 资源目录
     */
    public static void loadSunLoggerFromResources(String resourcePath) {
        //添加系统属性
        System.setProperty(LogTypeKey, LogTypeEnum.jdkLogger.getLogClassName());
        InputStream is = null;
        try {
            if (StringUtils.isBlank(resourcePath)) {
                resourcePath = SunLoggerResourcesFile;
            }
            resourcePath = PathUtils.formatResourcesPathForClassLoader(resourcePath);
            is = ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("日志文件不存在:" + resourcePath);
                return;
            }
            LogManager.getLogManager().readConfiguration(is);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("input properties file is error.\n" + e.toString());
        } finally {
            IOUtils.close(is);
        }

    }

    /**
     * 日志{@link java.util.logging.Logger}获取器
     * 默认是资源路径/logger.properties
     *
     * @param filePath 文件目录
     */
    public static void loadSunLoggerFromFile(String filePath) {
        //添加系统属性
        System.setProperty(LogTypeKey, LogTypeEnum.jdkLogger.getLogClassName());
        InputStream is = null;
        try {
            if (StringUtils.isBlank(SunLoggerFile)) {
                filePath = SunLoggerFile;
            }
            is = FileUtils.openInputStream(filePath);
            if (is == null) {
                System.out.println("日志文件不存在:" + filePath);
                return;
            }
            LogManager.getLogManager().readConfiguration(is);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("input properties file is error.\n" + e.toString());
        } finally {
            IOUtils.close(is);
        }

    }

    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param clazz
     * @return
     */
    public static Log getLog(Class clazz) {
        return com.latico.commons.common.util.logging.LoggerFactory.getLog(clazz.getName());
    }
    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        return com.latico.commons.common.util.logging.LoggerFactory.getLogger(clazz.getName());
    }

    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param name
     * @return
     */
    public static Log getLog(String name) {
        return com.latico.commons.common.util.logging.LoggerFactory.getLog(name);
    }
    /**
     * 获取日志对象,使用静态代理包装
     *
     * @param name
     * @return
     */
    public static Logger getLogger(String name) {
        return com.latico.commons.common.util.logging.LoggerFactory.getLogger(name);
    }
}

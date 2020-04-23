package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

/**
 * <PRE>
 * 类加载器工具
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-12 14:42
 * @version: 1.0
 */
public class ClassLoaderUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderUtils.class);

    /**
     * 获取启动类的类加载器，最顶层类加载器
     * 1、方式一：通过rt.jar中的类的类加载器就是BootStrap类加载器
     * @return 正常返回null,该加载器使用C/C++编写，所以不是一个对象，此方法只是为了测试效果，不作实际使用
     */
    public static ClassLoader getBootStrapClassLoader() {
        return Thread.class.getClassLoader();
    }

    /**
     * 获取系统类加载器，启动类用到的类加载器
     * @return
     */
    public static ClassLoader getExtClassLoader() {
        return ClassLoader.getSystemClassLoader().getParent();
    }

    /**
     * 获取系统类加载器，也就是AppClassLoader
     * @return
     */
    public static ClassLoader getAppClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 获取默认类加载器
     * 1、优先线程上下文类加载器；
     * 2、使用类的类加载器；
     * 3、使用系统类加载器
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaderUtils.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * 获取当前线程上下文类加载器
     * @return
     */
    public static ClassLoader getCurrentThreadContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取当前线程上下文类加载器
     * @return
     */
    public static void setCurrentThreadContextClassLoader(ClassLoader classLoader) {
        Thread.currentThread().setContextClassLoader(classLoader);
    }

    /**
     * 指定类获取类加载器
     * @return
     */
    public static ClassLoader getClassLoaderByClass(Class clazz) {
        return clazz.getClassLoader();
    }

    /**
     * 获取类加载器的父类加载器
     * @return
     */
    public static ClassLoader getParentClassLoader(ClassLoader classLoader) {
        return classLoader.getParent();
    }


    /**
     * 通过AppClassLoader来加载一个类
     * @param className
     * @return
     */
    public static Class<?> getClassByAppClassLoader(String className){
        try {
            return getAppClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
        }
        return null;
    }


    /**
     * 通过指定类加载器来加载一个类
     * @param classLoader
     * @param className
     * @return
     */
    public static Class<?> getClassByClassLoader(ClassLoader classLoader, String className){
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
        }
        return null;
    }

}

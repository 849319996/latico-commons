package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.system.SystemUtils;

/**
 * <PRE>
 * 类加载器工具
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-12 14:42
 * @Version: 1.0
 */
public class ClassLoaderUtils {

    /**
     * 获取启动类的类加载器，最顶层类加载器
     * 1、方式一：通过rt.jar中的类的类加载器就是BootStrap类加载器
     * 2、方式二：通过getExtClassLoader扩展类加载器的父类加载器获取
     * @return 正常返回null,该加载器使用C/C++编写，所以不是一个对象，此方法
     */
    public static ClassLoader getBootStrapClassLoader() {
        return getExtClassLoader().getParent();
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
     * @return BootStrap ClassLoader加载的文件列表信息
     */
    public static String getBootStrapClassLoaderLoadFileInfo() {
        return SystemUtils.getBootStrapClassLoaderLoadFileInfo();
    }


    /**
     * @return BootStrap ClassLoader加载的文件列表信息
     */
    public static String getExtClassLoaderLoadFileInfo() {
        return SystemUtils.getExtClassLoaderLoadFileInfo();
    }

    /**
     * @return 所有java类文件路径信息
     */
    public static String getJavaClassPath() {
        return SystemUtils.getJavaClassPath();
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
        }
        return null;
    }


    /**
     * 通过AppClassLoader来加载一个类
     * @param className
     * @return
     */
    public static Class<?> getClassByClassLoader(ClassLoader classLoader, String className){
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
        }
        return null;
    }
}

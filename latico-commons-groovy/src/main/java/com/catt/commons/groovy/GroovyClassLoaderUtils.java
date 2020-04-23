package com.catt.commons.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.junit.Test;

import java.util.Map;

/**
 * <PRE>
 * GroovyClassLoader 工具
 *
 * groovy官方提供GroovyClassLoader从文件，url或字符串中加载解析Groovy class
 * </PRE>
 *
 * @author: latico
 * @date: 2020-04-23 13:55
 * @version: 1.0
 */
public class GroovyClassLoaderUtils {

    /**
     * 类加载器
     */
    private static final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();

    /**
     * 执行
     * @param groovyClassScript 脚本，是一个class的groovy脚本
     * @param methodName        方法名称
     * @param methodParam 方法参数
     * @param <T>
     * @return 执行结果
     * @throws Exception
     */
    public static <T> T evaluate(String groovyClassScript, String methodName, Object methodParam) throws Exception {
        Class clazz = groovyClassLoader.parseClass(groovyClassScript);
        GroovyObject object = (GroovyObject) clazz.newInstance();
        return (T) object.invokeMethod(methodName, methodParam);
    }


}

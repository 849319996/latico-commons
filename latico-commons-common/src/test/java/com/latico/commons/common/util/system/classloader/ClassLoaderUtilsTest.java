package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.logging.LogUtilsTest;
import com.latico.commons.common.util.system.SystemUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class ClassLoaderUtilsTest {

    @Test
    public void getClassLoaderDefault() {
        System.out.println(ClassLoaderUtils.getDefaultClassLoader());
        System.out.println("===================");
    }

    @Test
    public void getCurrentThreadContextClassLoader() {
        System.out.println(ClassLoaderUtils.getCurrentThreadContextClassLoader());
        System.out.println("===================");
    }

    @Test
    public void setCurrentThreadContextClassLoader() {
        System.out.println(ClassLoaderUtils.getCurrentThreadContextClassLoader());
        ClassLoaderUtils.setCurrentThreadContextClassLoader(ClassLoaderUtils.getAppClassLoader());
        System.out.println(ClassLoaderUtils.getCurrentThreadContextClassLoader());
        System.out.println("===================");
    }

    @Test
    public void getClassLoaderByClass() {
        System.out.println(ClassLoaderUtils.getClassLoaderByClass(ClassLoaderUtilsTest.class));
        System.out.println(ClassLoaderUtils.getClassLoaderByClass(LogUtilsTest.class));
        System.out.println(ClassLoaderUtils.getClassLoaderByClass(IOUtils.class));
        System.out.println("===================");
    }

    @Test
    public void getParentClassLoader() {
        System.out.println(ClassLoaderUtils.getParentClassLoader(ClassLoaderUtils.getAppClassLoader()));
        System.out.println("===================");
    }


    @Test
    public void getClassLoader() {
        System.out.println(ClassLoaderUtils.getClassLoaderByClass(System.class));
        System.out.println("===================");
    }

    @Test
    public void getBootStrapClassLoader() {
        System.out.println(ClassLoaderUtils.getBootStrapClassLoader());
    }

    @Test
    public void getAppClassLoader() {
        System.out.println(ClassLoaderUtils.getAppClassLoader());
    }

    @Test
    public void getExtClassLoader() {
        System.out.println(ClassLoaderUtils.getExtClassLoader());
    }


    @Test
    public void testExtClassLoader() {
        try {
            System.out.println(ClassLoaderUtils.getExtClassLoader().loadClass(LogUtilsTest.class.getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAppClassLoader() {
        try {
            System.out.println(ClassLoaderUtils.getAppClassLoader().loadClass(LogUtilsTest.class.getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getBootStrapClassLoaderLoadFileInfo() {
        System.out.println(SystemUtils.getBootStrapClassLoaderLoadFileInfo());
    }

    @Test
    public void getExtClassLoaderLoadFileInfo() {
        for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
//        System.out.println(ClassLoaderUtils.getExtClassLoaderLoadFileInfo());
    }
    @Test
    public void test2() {
        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("com/latico/commons/common/util/system/classloader/abc3.txt"));
//        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("abc4.txt"));
//        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("log4j.properties"));

        System.out.println(ClassLoaderUtils.getAppClassLoader().getResourceAsStream("/com/latico/commons/common/util/system/classloader/abc3.txt"));
        System.out.println(ClassLoaderUtilsTest.class.getClassLoader().getResourceAsStream("com/latico/commons/common/util/system/classloader/abc4.txt"));
        System.out.println(ClassLoaderUtilsTest.class.getClassLoader().getResourceAsStream("com/latico/commons/common/util/system/classloader/log4j.properties"));
    }


    /**
     *
     */
    @Test
    public void test3(){
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/latico/test.properties");


    }

    /**
     *
     */
    @Test
    public void test(){

        System.out.println(ClassLoaderUtils.class.getResourceAsStream("abc3.txt"));
        System.out.println(ClassLoaderUtils.getAppClassLoader().getResourceAsStream("com/latico/commons/common/util/system/classloader/abc3.txt"));
    }
    /**
     *
     */
    @Test
    public void testURLClassLoader() throws Exception {
        File file = new File("./doc/latico.jar");
        URL uri = file.toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{uri});
        Class c = urlClassLoader.loadClass("com.latico.web.Main");
        System.out.println(c);
        if (c != null) {
            System.out.println(c.getClassLoader().toString());
        }
    }

    /**
     *
     */
    @Test
    public void test4(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    }

}
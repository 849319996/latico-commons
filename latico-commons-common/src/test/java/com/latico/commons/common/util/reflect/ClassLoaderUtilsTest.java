package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.logging.LogUtilsTest;
import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

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
        System.out.println(ClassLoaderUtils.getBootStrapClassLoaderLoadFileInfo());
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
        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("com/latico/commons/abc3.txt"));
//        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("abc4.txt"));
//        System.out.println(ClassLoaderUtilsTest.class.getResourceAsStream("log4j.properties"));

        System.out.println(ClassLoaderUtils.getAppClassLoader().getResourceAsStream("/com/latico/commons/abc3.txt"));
        System.out.println(ClassLoaderUtilsTest.class.getClassLoader().getResourceAsStream("com/latico/commons/abc4.txt"));
        System.out.println(ClassLoaderUtilsTest.class.getClassLoader().getResourceAsStream("com/latico/commons/log4j.properties"));
    }
}
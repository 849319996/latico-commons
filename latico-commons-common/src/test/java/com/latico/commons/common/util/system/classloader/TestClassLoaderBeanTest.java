package com.latico.commons.common.util.system.classloader;

import org.junit.Test;

public class TestClassLoaderBeanTest {

    /**
     * 使用Class.forName，会执行静态代码块
     */
    @Test
    public void test1() throws ClassNotFoundException {
        Class.forName(TestClassLoaderBean.class.getName());
    }
    /**
     * 使用ClassLoader.loadClass不会初始化静态代码块
     */
    @Test
    public void test2() throws ClassNotFoundException {
        Thread.currentThread().getContextClassLoader().loadClass(TestClassLoaderBean.class.getName());
    }

    /**
     * 访问静态变量，不会初始化类
     */
    @Test
    public void test3(){
        System.out.println(TestClassLoaderBean.finalIntData);
        System.out.println(TestClassLoaderBean.finalStrData);
    }

    /**
     * 访问静态变量，会初始化类
     */
    @Test
    public void test4(){
        System.out.println(TestClassLoaderBean.intData);
        System.out.println(TestClassLoaderBean.strData);
    }

    /**
     * 先用ClassLoader.loadClass，后访问静态变量，会初始化类
     */
    @Test
    public void test5() throws ClassNotFoundException {
        Thread.currentThread().getContextClassLoader().loadClass(TestClassLoaderBean.class.getName());
        System.out.println(TestClassLoaderBean.intData);
        System.out.println(TestClassLoaderBean.strData);
    }
}
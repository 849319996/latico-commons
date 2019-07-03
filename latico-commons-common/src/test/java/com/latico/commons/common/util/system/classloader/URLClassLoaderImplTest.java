package com.latico.commons.common.util.system.classloader;

import org.junit.Test;

import java.net.URL;

public class URLClassLoaderImplTest {

    @Test
    public void test() throws Exception {
        URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath(".\\doc\\latico.jar");
        Class c = myClassLoader1.loadClass("com.latico.web.Main");

        if (c != null) {
            Object obj = c.newInstance();
            System.out.println(c.getClassLoader().toString());
        }
    }

    @Test
    public void findClass() throws Exception {
        URLClassLoaderImpl myClassLoader = URLClassLoaderImpl.getInstance();

        //自定义类加载器的加载路径
        myClassLoader.addResourcesByClassFilePath(".\\doc\\Test.class", ".\\doc\\Test2.class");
        Class clazz = null;

        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test");
        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
//            Class<?> aClass = Class.forName("com.latico.commons.common.util.system.classloader.Test");
//            System.out.println(aClass);

        }

        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test2");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
//            Class<?> aClass = Class.forName("com.latico.commons.common.util.system.classloader.Test2");
//            System.out.println(aClass);
        }
    }
    @Test
    public void test2() throws Exception {
//        URL url = PathUtils.convertFilePathToUrl("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
//        System.out.println(url.getPath());
//        System.out.println(url.getFile());
//        System.out.println(url.toString());
        URL url = new URL("jar:file:/.\\doc\\latico.jar!/config.properties");
        System.out.println(url);
    }

    @Test
    public void test3() throws Exception {
        URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath(".\\doc\\latico.jar");
        URL c = myClassLoader1.getResource("config.properties");
        System.out.println(c);

    }

}
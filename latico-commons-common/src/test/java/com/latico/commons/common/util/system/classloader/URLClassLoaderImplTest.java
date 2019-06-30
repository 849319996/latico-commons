package com.latico.commons.common.util.system.classloader;

import org.junit.Test;

import java.net.URL;

public class URLClassLoaderImplTest {

    @Test
    public void test() throws Exception {
        URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
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
        myClassLoader.addResourcesByClassFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\com.GoogleBloomFilterTest.class", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.class", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\TestBean.class");

        //包名+类名
        Class clazz = myClassLoader.loadClass("com.GoogleBloomFilterTest");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
        }
        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.GoogleBloomFilterTest");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
        }
    }
    @Test
    public void test2() throws Exception {
//        URL url = PathUtils.convertFilePathToUrl("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
//        System.out.println(url.getPath());
//        System.out.println(url.getFile());
//        System.out.println(url.toString());
        URL url = new URL("jar:file:/C:/Users/Administrator/Desktop/新建文件夹2/latico.jar!/config.properties");
        System.out.println(url);
    }

    @Test
    public void test3() throws Exception {
        URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
        URL c = myClassLoader1.getResource("config.properties");
        System.out.println(c);

    }

}
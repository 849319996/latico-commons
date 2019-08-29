package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.system.classloader.impl.URLClassLoaderImpl;
import org.junit.Test;

import java.net.URL;

public class URLClassLoaderImplTest {
    /**
     * 测试从jar包里面加载类
     * @throws Exception
     */
    @Test
    public void testLoadByJar() throws Exception {
        URLClassLoaderImpl myClassLoader1 = new URLClassLoaderImpl();
        myClassLoader1.addResourcesByJarFilePath(".\\doc\\latico.jar");
        Class c = myClassLoader1.loadClass("com.latico.web.Main");
        System.out.println(c);
        if (c != null) {
            System.out.println(c.getClassLoader().toString());
        }
    }
    /**
     * 测试从class文件读取类
     * @throws Exception
     */
    @Test
    public void testLoadByClassFile() throws Exception {
        URLClassLoaderImpl myClassLoader = new URLClassLoaderImpl();

        //自定义类加载器的加载路径
        myClassLoader.addResourcesByClassFilePath(".\\doc\\Test.class", ".\\doc\\Test2.class");
        Class clazz = null;

        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test");
        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());

        }

        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test2");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
        }
    }

    /**
     * 测试从jar包加载资源文件读
     * @throws Exception
     */
    @Test
    public void testLoadResourceByJar() throws Exception {
        URLClassLoaderImpl myClassLoader1 = new URLClassLoaderImpl();
        myClassLoader1.addResourcesByJarFilePath(".\\doc\\latico.jar");
        URL c = myClassLoader1.getResource("config.properties");
        System.out.println(c);

    }

    @Test
    public void testJarUrl() throws Exception {
//        URL url = PathUtils.convertFilePathToUrl(".\\doc\\latico.jar");
//        System.out.println(url.getPath());
//        System.out.println(url.getFile());
//        System.out.println(url.toString());
        URL url = new URL("jar:file:/.\\doc\\latico.jar!/config.properties");
        System.out.println(url);
    }

}
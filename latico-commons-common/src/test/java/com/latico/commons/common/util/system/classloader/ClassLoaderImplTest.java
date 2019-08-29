package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.system.classloader.impl.ClassLoaderImpl;
import org.junit.Test;

public class ClassLoaderImplTest {
    /**
     * 测试从jar包里面加载类
     * @throws Exception
     */
    @Test
    public void testLoadByJar() throws Exception {
        ClassLoaderImpl myClassLoader1 = new ClassLoaderImpl();
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
        LogUtils.loadLogBackConfigDefault();
        ClassLoaderImpl myClassLoader = new ClassLoaderImpl();

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
        ClassLoaderImpl myClassLoader1 = new ClassLoaderImpl();
        myClassLoader1.addResourcesByJarFilePath(".\\doc\\latico.jar");
        System.out.println(IOUtils.resourceToString("config.properties", myClassLoader1));
    }

}
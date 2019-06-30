package com.latico.commons.common.util.system.classloader;

import com.latico.commons.common.util.io.IOUtils;
import org.junit.Test;

public class ClassLoaderImplTest {
    @Test
    public void test() throws Exception {
        ClassLoaderImpl myClassLoader1 = ClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
        Class c = myClassLoader1.loadClass("com.latico.web.Main");

        if (c != null) {
            Object obj = c.newInstance();
            System.out.println(c.getClassLoader().toString());
        }

        c = myClassLoader1.loadClass("com.latico.web.Main");

        if (c != null) {
            Object obj = c.newInstance();
            System.out.println(c.getClassLoader().toString());
        }
    }
    @Test
    public void findClass() throws Exception {
        ClassLoaderImpl myClassLoader = ClassLoaderImpl.getInstance();

        //自定义类加载器的加载路径
        myClassLoader.addResourcesByClassFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\GoogleBloomFilterTest.class", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.class");

        //包名+类名
        Class clazz = ClassLoaderImpl.getInstance().loadClass("com.GoogleBloomFilterTest");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
        }
        clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test2");

        if (clazz != null) {
            System.out.println(clazz.getClassLoader().toString());
        }

    }

    @Test
    public void test3() throws Exception {
        System.out.println("ajjg.class".matches(".+?(?<!\\.class)$"));
        System.out.println("ajjg.class1".matches(".+?(?<!\\.class)$"));
        System.out.println("ajjg.2class".matches(".+?(?<!\\.class)$"));
    }

    @Test
    public void test4() throws Exception {
        ClassLoaderImpl myClassLoader1 = ClassLoaderImpl.getInstance();
        myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
        System.out.println(IOUtils.resourceToString("config.properties", myClassLoader1));

    }
}
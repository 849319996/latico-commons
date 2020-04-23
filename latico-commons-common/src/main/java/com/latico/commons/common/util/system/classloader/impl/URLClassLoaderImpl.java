package com.latico.commons.common.util.system.classloader.impl;

import com.latico.commons.common.util.codec.MD5Utils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.UriUtils;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <PRE>
 *     继承URLClassLoader进行实现的类加载器
 *     因为AppClassLoader都是通过继承URLClassLoader进行实现，所以我们也通过继承它，
 *     以获得更多默认实现的功能。
 *
 * 平时我们可以直接使用URLClassLoader来加载class或者资源，或者继承URLClassLoader进行自定义增强,
 * URLClassLoader的URL不支持加载单个class文件，但是支持加载jar文件，所以该类增加URLClassLoader，已达到支持加载单个class文件
 *
 //URLClassLoader的使用示例，根据url1创建类装载器
 URL url1 = new URL("file:d:/test.jar");
 URLClassLoader myClassLoader1 = new URLClassLoader(
 new URL[] { url1 }, Thread.currentThread().getContextClassLoader());
 Class myClass1 = myClassLoader1.loadClass("uRLClassLoaderTest1.TestAction");
 ActionInterface action1 = (ActionInterface) myClass1.newInstance();


//URLClassLoaderImpl的使用示例

 URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
 myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
 Class c = myClassLoader1.loadClass("com.latico.web.Main");

 if (c != null) {
 Object obj = c.newInstance();
 System.out.println(c.getClassLoader().toString());
 }

 URLClassLoaderImpl myClassLoader = URLClassLoaderImpl.getInstance();

 //自定义类加载器的加载路径
 myClassLoader.addResourcesByClassFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test.class", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.class");

 //包名+类名
 Class clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test");

 if (clazz != null) {
 System.out.println(clazz.getClassLoader().toString());
 }
 clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test2");

 if (clazz != null) {
 System.out.println(clazz.getClassLoader().toString());
 }

 URLClassLoaderImpl myClassLoader1 = URLClassLoaderImpl.getInstance();
 myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
 URL c = myClassLoader1.getResource("config.properties");
 System.out.println(c);
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-12 18:30
 * @version: 1.0
 */
public class URLClassLoaderImpl extends URLClassLoader {
    public static final Class clazz = URLClassLoaderImpl.class;
    private static final Logger LOG = LoggerFactory.getLogger(clazz);

    /**
     * class文件的字节码内容，用字节数组装起来,key是类内容的MD5值，用于去重
     */
    private ConcurrentSkipListMap<String, byte[]> classByteCodes = new ConcurrentSkipListMap<>();

    /**
     * URL的唯一值，用于判断是否加载过
     */
    private Set<String> urlKeys = new ConcurrentSkipListSet<>();

    public URLClassLoaderImpl() {
        //指定父类加载器
        super(new URL[]{}, Thread.currentThread().getContextClassLoader());
    }


    /**
     * @param jarFilePaths jar文件系统路径，可以多个
     */
    public void addResourcesByJarFilePath(String... jarFilePaths) {
        if (jarFilePaths == null) {
            return;
        }
        for (String filePath : jarFilePaths) {
            if (filePath == null) {
                continue;
            }
            try {
                URL url = UriUtils.convertFilePathToUrl(filePath);
                addURL(url);
            } catch (Throwable e) {
                LOG.error(e);
            }
        }
    }

    /**
     * @param url jar文件的URL
     */
    @Override
    public void addURL(URL url) {
        if (url == null) {
            return;
        }
        if(urlKeys.contains(url.toString())){
            return;
        }else{
            urlKeys.add(url.toString());
        }
        LOG.info("开始添加URL到类加载器,URL:{}", url);
        super.addURL(url);
        LOG.info("添加URL到类加载器成功,URL:{}", url);
    }

    /**
     * 添加字节码内容到类加载器
     * @param ClassCodes 字节码内容，可多个
     */
    public void addResourcesByClassCode(byte[]... ClassCodes) {
        if (ClassCodes == null) {
            return;
        }

        for (byte[] ClassCode : ClassCodes) {
            if (ClassCode == null) {
                continue;
            }
            try {
                String md5 = getResourcesKey(ClassCode);
                if (classByteCodes.containsKey(md5)) {
                    continue;
                }
                classByteCodes.put(md5, ClassCode);
                LOG.info("添加字节码到类加载器成功,字节码内容MD5:{}", md5);
            } catch (Throwable e) {
                LOG.error(e);
            }
        }
    }
    /**
     * 增加class文件进来,可以多个
     * @param classFilePaths class文件的系统文件路径，可以多个
     * @return
     */
    public void addResourcesByClassFilePath(String... classFilePaths) {
        if (classFilePaths == null) {
            return;
        }

        for (String classFilePath : classFilePaths) {
            if (classFilePath == null) {
                continue;
            }
            try {
                byte[] bytes = FileUtils.readFileToByteArray(classFilePath);
                String md5 = getResourcesKey(bytes);
                if (classByteCodes.containsKey(md5)) {
                    continue;
                }
                classByteCodes.put(md5, bytes);
                LOG.info("添加字节码文件到类加载器成功,字节码内容MD5:{}, 字节码文件路径:{}", md5, classFilePath);
            } catch (Throwable e) {
                LOG.error(e);
            }
        }
    }
    /**
     * 计算资源key
     * @param bytes
     * @return
     */
    private String getResourcesKey(byte[] bytes) {
        return MD5Utils.toLowerCaseMd5(bytes);
    }

    /**
     * 先从单个class文件列表中加载，获取不到就从URLClassLoader中的扫描jar包加载
     * @param name java类路径
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        LOG.info("开始查找类:{}", name);

        //逐个遍历classs查找
        for (byte[] classByte : classByteCodes.values()) {
            try {
                Class<?> defineClass = defineClass(name, classByte, 0, classByte.length);
                if (defineClass != null) {
                    //如果在classs列表中找到，直接返回
                    return defineClass;
                }
            } catch (Throwable t) {
            }
        }

        //URLClassLoader中的扫描jar包加载
        return super.findClass(name);
    }

    /**
     * 清除类加载器中的资源
     */
    public void clearResources() {
        classByteCodes.clear();
    }

    /**
     * ClassLoader是个抽象类，而ClassLoader.defineClass 方法是protected的
     * 所以我们需要定义一个子类将这个方法暴露出来
     * @param className
     * @param classByte
     * @return
     */
    public Class<?> defineClass(String className, byte[] classByte) {
        return super.defineClass(className, classByte, 0, classByte.length);
    }

}

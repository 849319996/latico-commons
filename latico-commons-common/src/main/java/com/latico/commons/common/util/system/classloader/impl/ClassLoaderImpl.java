package com.latico.commons.common.util.system.classloader.impl;

import com.latico.commons.common.util.codec.MD5Utils;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.net.UriUtils;
import com.latico.commons.common.util.reflect.ResourcesUtils;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <PRE>
 *  通过继承ClassLoader实现的类加载器，目前支持加载单个class文件和jar包方式，还不支持http的URL方式，
 *  建议使用实现功能更强大的{@link URLClassLoaderImpl}
 *
 *  自定义类加载器步骤：
 * （1）继承ClassLoader
 * （2）重写findClass()和findResource()方法
 * （3）调用defineClass()方法传入字节码文件内容的二进制进行类对象创建
 *
 *
 ClassLoaderImpl myClassLoader1 = ClassLoaderImpl.getInstance();
 myClassLoader1.addResourcesByJarFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\latico.jar");
 System.out.println(IOUtils.resourceToString("config.properties", myClassLoader1));

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

 ClassLoaderImpl myClassLoader = ClassLoaderImpl.getInstance();

 //自定义类加载器的加载路径
 myClassLoader.addResourcesByClassFilePath("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test.class", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.class");

 //包名+类名
 Class clazz = ClassLoaderImpl.getInstance().loadClass("com.latico.commons.common.util.system.classloader.Test");

 if (clazz != null) {
 System.out.println(clazz.getClassLoader().toString());
 }
 clazz = myClassLoader.loadClass("com.latico.commons.common.util.system.classloader.Test2");

 if (clazz != null) {
 System.out.println(clazz.getClassLoader().toString());
 }
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-12 18:47
 * @Version: 1.0
 */
public class ClassLoaderImpl extends ClassLoader {
    public static final Class clazz = ClassLoaderImpl.class;
    private static final Logger LOG = LoggerFactory.getLogger(clazz);

    private static volatile ClassLoaderImpl INSTANCE;

    /**
     * class文件字节码内容用字节数组装起来,key是类内容的MD5值
     */
    private ConcurrentSkipListMap<String, byte[]> classByteCodes = new ConcurrentSkipListMap<>();

    /**
     * jar文件URL
     */
    private ConcurrentSkipListMap<String, URL> urls = new ConcurrentSkipListMap<>();

    private ClassLoaderImpl() {
        //指定父类加载器
        super(Thread.currentThread().getContextClassLoader());
    }

    /**
     * @return 单例
     */
    public static ClassLoaderImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (clazz) {
                if (INSTANCE == null) {
                    INSTANCE = new ClassLoaderImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        LOG.info("开始查找类:{}", name);

        //逐个classByteCodes遍历查找
        for (byte[] classByte : classByteCodes.values()) {
            try {
                Class<?> defineClass = defineClass(name, classByte, 0, classByte.length);
                if (defineClass != null) {
                    return defineClass;
                }
            } catch (Throwable t) {
            }
        }

    //从URL的jar中查找
        for (URL url : urls.values()) {
            try {
                List<String> resourcesNames = ResourcesUtils.getResourcesNameByUrL(url, null, name + "\\.class", true);
                System.out.println(resourcesNames);
                if (resourcesNames != null) {
                    for (String resourcesName : resourcesNames) {
                        if (resourcesName == null) {
                            continue;
                        }

                        //把资源名称转换成类名
                        resourcesName = ResourcesUtils.convertResourcesNameToClassName(resourcesName);
                        //匹配类名
                        if (resourcesName.equals(name)) {
                            //读取jar中的类作为字节数组
                            URL newUrl = new URL(url.toString() + name.replace(".", "/") + ".class");
                            byte[] bytes = IOUtils.readUrlToByteArray(newUrl);

                            //然后调用defineClass生成类
                            Class<?> defineClass = defineClass(name, bytes, 0, bytes.length);
                            if (defineClass != null) {
                                return defineClass;
                            }
                        }
                    }
                }

            } catch (Throwable e) {
            }
        }

        //找不到使用父类
        return super.findClass(name);
    }

    @Override
    protected URL findResource(final String name) {

        LOG.info("开始查找资源:{}", name);

        //因为jar的URL中已经有以/结尾，所以这里要去掉
        String newName = name;
        if (name.startsWith("/")) {
            newName = newName.substring(1, name.length());
        }

        //为了方便匹配，把/转换成.已达到跟ResourcesUtils.getResourcesNameByUrL检索的结果进行匹配
        final String nameLikeClass = newName.replace("/", ".");

        for (URL url : urls.values()) {
            try {
                //查找不以.class结尾是所有资源
                List<String> resourcesNames = ResourcesUtils.getResourcesNameByUrL(url, null, ".+?(?<!\\.class)$", true);
                if (resourcesNames != null) {
                    for (String resourcesName : resourcesNames) {
                        if (resourcesName == null) {
                            continue;
                        }
                        if (nameLikeClass.equals(resourcesName)) {

                            URL newUrl = new URL(url.toString() + newName);
                            return newUrl;
                        }
                    }
                }

            } catch (Throwable e) {
            }
        }
        return super.findResource(name);
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
     * @param url jar文件的URL
     */
    public void addResourcesByJarURL(URL url) {
        if (url == null) {
            return;
        }
        try {
            url = UriUtils.convertFileUrlToJarUrl(url);
            if(urls.containsKey(url.toString())){
                return;
            }
            LOG.info("开始添加URL到类加载器,URL:{}", url);
            urls.put(url.toString(), url);
            LOG.info("添加URL到类加载器成功,URL:{}", url);
        } catch (Exception e) {
            LOG.error(e);
        }

    }

    /**
     * 增加资源文件进来,可以多个
     * @param jarFilePaths 资源文件的系统文件路径，可以多个
     * @return
     */
    public void addResourcesByJarFilePath(String... jarFilePaths) {
        if (jarFilePaths == null) {
            return;
        }

        for (String jarFilePath : jarFilePaths) {
            if (jarFilePath == null) {
                continue;
            }
            try {
                URL url = UriUtils.convertFilePathToJarUrl(jarFilePath);
                addResourcesByJarURL(url);
            } catch (Throwable e) {
                LOG.error(e);
            }
        }
    }
}

package com.latico.commons.common.util.reflect;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 资源工具，加载资源文件、资源搜索等

 常见的有以下两种获取资源文件的方法：

 方法一: App.class.getClassLoader().getResourceAsStream(String name)

 Returns an input stream for reading the specified resource.

 The search order is described in the documentation for getResource(String).

 默认从classpath中找文件(文件放在resources目录下)，name不能带“/”，否则会抛空指针

 方法二: App.class.getResourceAsStream(String name)

 查找资源通过给定名称，查询资源的规则与给定的类的class load来实现，这个方法由类的loader来执行，如果这个类由bootstrap加载，那么方法由ClassLoader.getSystemResourceAsStream代理执行。

 代理之前，绝对的资源名称通过传入的name参数以下算法进行构造：

 如果name以"/"开头，那么绝对路径是/后边跟的名字

 如果name不是以"/"开头，那么绝对路径是package名"."换成“/”以后再加name，例如：com.abc.App就是/com/abc/App/name 或者写作 :

 对资源路径进行了处理
 {@link Class# resolveName(java.lang.String)}

 * @Author: latico
 * @Date: 2018/12/11 1:34
 * @Version: 1.0
 */
public class ResourcesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ResourcesUtils.class);
    /**
     * 取得某个接口下所有实现这个接口的类,扫描路径是当前接口类的包之下的目录，
     * 该方法支持IDE调试的时候读取文件形式的class和假如是已经打进了jar里面。
     * 同时，排除<code>clazz</code>自身。
     * @param fatherClass       指定接口或者超类，找出实现它或者集成它的所有子类
     * @throws Exception
     * @return
     */
    public static List<Class> getAllImplClass(Class fatherClass) throws Exception {
        return getAllImplClassUnderPackage(fatherClass, null);
    }
    /**
     * 取得某个接口下所有实现这个接口的类,扫描路径是当前接口类的包之下的目录，
     * 该方法支持IDE调试的时候读取文件形式的class和假如是已经打进了jar里面。
     * 同时，排除<code>clazz</code>自身。
     * @param fatherClass       指定接口或者超类，找出实现它或者集成它的所有子类
     * @param packageName 指定扫描的包目录
     * @throws Exception
     * @return
     */
    public static List<Class> getAllImplClassUnderPackage(Class fatherClass, String packageName) throws Exception {
        List<Class> returnClassList = null;

        // 获取当前包下以及子包下所以的类
        List<Class<?>> allClass = getAllClassesUnderPackage(packageName);
        if (allClass != null) {
            returnClassList = new ArrayList<Class>();

            for (Class clazz : allClass) {
                // 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。
                if (fatherClass.isAssignableFrom(clazz)) {
                    // 本身不加入进去
                    if (fatherClass.equals(clazz)) {
                        continue;
                    }

                    returnClassList.add(clazz);
                }
            }
        }

        return returnClassList;
    }

    /**
     * 取得某个接口下所有实现这个接口的类,扫描路径是当前接口类的包之下的目录，
     * 该方法支持IDE调试的时候读取文件形式的class和假如是已经打进了jar里面。
     * 同时，排除<code>clazz</code>自身。
     * @param fatherClass       指定接口或者超类，找出实现它或者集成它的所有子类
     * @param packageName 指定扫描的包目录
     * @throws Exception
     * @return
     */
    public static <T> List<Class<T>> getAllImplClassByFatherClass(Class<T> fatherClass, String packageName) throws Exception {
        List<Class<T>> returnClassList = null;

        // 获取当前包下以及子包下所以的类
        List<Class<?>> allClass = getAllClassesUnderPackage(packageName);
        if (allClass != null) {
            returnClassList = new ArrayList<>();

            for (Class clazz : allClass) {
                // 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。
                if (fatherClass.isAssignableFrom(clazz)) {
                    // 本身不加入进去
                    if (fatherClass.equals(clazz)) {
                        continue;
                    }

                    returnClassList.add(clazz);
                }
            }
        }

        return returnClassList;
    }

    /**
     * 从包package中获取所有的Class，同时支持文件方式和jar包方式
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getAllClassesUnderPackage(final String packageName) throws Exception {

        List<Class<?>> classes = new ArrayList<>();
        List<String> resourcesNames = getAllResourcesNameUnderPackage(packageName, ".+?\\.class", true);
        if (resourcesNames != null) {
            for (String resourcesName : resourcesNames) {
//                要用当前线程的类加载器，不要使用Class.forName() 20190509
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(convertResourcesNameToClassName(resourcesName)));
            }
        }
        return classes;
    }

    /**
     * 把资源名称转换成java类路径名称
     * @param resourcesName 如:com.latico.commons.common.util.reflect.ResourcesUtils.class得到com.latico.commons.util.reflect.ResourcesUtils
     * @return
     */
    public static String convertResourcesNameToClassName(String resourcesName) {
        return resourcesName.replaceAll("\\.class$", "");
    }

    /**
     * 从包package中获取所有的Class，同时支持文件方式和jar包方式
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<String> getAllXmlResourcesFileNameUnderPackage(final String packageName) throws Exception {
        return getAllResourcesNameUnderPackage(packageName, ".+?\\.(?i)xml", true);
    }

    /**
     * 从包package中获取所有的资源，同时支持IDE调试的文件方式和jar包方式
     *
     * @param packageName        包路径
     * @param resourcesNameRegex 指定资源名字匹配正则
     * @param recursive      是否循环迭代，如果否的话，就只在当前包目录下找
     * @return 资源文件名字，开头没有斜杠/的形式
     * @throws Exception
     */
    public static List<String> getAllResourcesNameUnderPackage(String packageName, final String resourcesNameRegex, final boolean recursive) throws Exception {

        //为空就是根目录
        if (packageName == null) {
            packageName = "";
        }

        // 第一个class类的集合
        List<String> resourcesResults = new ArrayList<String>();

        // 转成资源文件目录结构，获取包的名字 并进行替换
        final String packageDirName = packageName.replace('.', '/');

        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        LOG.info("扫描包[{}]用的类加载器:[{}]", packageName, Thread.currentThread().getContextClassLoader());
        Enumeration<URL> resourcesDirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

        // 循环迭代下去
        while (resourcesDirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = resourcesDirs.nextElement();
            resourcesResults.addAll(getResourcesNameByUrL(url, packageName, resourcesNameRegex, recursive));
        }

        return resourcesResults;
    }

    /**
     * 获取URL下面所有资源名字
     * @param url                资源的URL，不能为空
     * @param packageName 指定包名下,可以为空
     * @param resourcesNameRegex 资源名字的正则，可以为空
     * @param recursive
     * @throws Exception
     * @return
     */
    public static List<String> getResourcesNameByUrL(URL url, String packageName, String resourcesNameRegex, boolean recursive) throws Exception {
        List<String> resourcesResults = new ArrayList<>();
        if (packageName == null) {
            packageName = "";
        }
        // 得到协议的名称
        String protocol = url.getProtocol();

        // 如果是以文件的形式保存在服务器上
        if ("file".equals(protocol)) {
            // 获取包的物理路径
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            // 以文件的方式扫描整个包下的文件 并添加到集合中
            findResourcesNameUnderPackageByFilePath(packageName, filePath, recursive, resourcesResults, resourcesNameRegex);

            // 如果是jar包文件
        } else if ("jar".equals(protocol)) {
            findResourcesNameUnderPackageByJar(packageName, resourcesResults, recursive, url, resourcesNameRegex);
        }

        return resourcesResults;
    }

    /**
     * 在jar中，找出资源文件名字
     * @param packageName 包目录
     * @param resourcesResults 资源结构
     * @param recursive 是否递归
     * @param resourcesUrl 资源URL
     * @param resourcesNameRegex 资源名称正则
     * @throws Exception
     */
    private static void findResourcesNameUnderPackageByJar(String packageName, List<String> resourcesResults, boolean recursive, URL resourcesUrl, String resourcesNameRegex) throws Exception {
        if(packageName == null){
            packageName = "";
        }
        final String packageDirName = packageName.replace('.', '/');

        // 定义一个JarFile
        JarFile jar = ((JarURLConnection) resourcesUrl.openConnection()).getJarFile();

        // 从此jar包 得到一个枚举类,里面包含了jar里面所有内容，可以是目录 和一些jar包里的其他文件 如META-INF等文件
        Enumeration<JarEntry> entries = jar.entries();

        //        资源正则是否为空
        final boolean resourcesNameRegexIsEmpty = resourcesNameRegex == null || "".equals(resourcesNameRegex);

        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            // 如果是以/开头的,需要去掉/获取后面的字符串
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }

            // 如果前半部分和定义的包名相同
            if (!name.startsWith(packageDirName)) {
                continue;
            }

            // 如果不是目录，如果以"/"结尾 是一个包
            if (entry.isDirectory()) {
                continue;
            }

            //匹配资源的名字
            if(resourcesNameRegexIsEmpty || name.matches(resourcesNameRegex)){
                //如果不是循环迭代，那就判断该资源文件是否在当前包下
                if(!recursive){
                    //去掉文件名和后缀得到包名路径，通过：截取最后一个斜杠前的字符串，判断是否跟当前包目录字符长度一样
                    int last = name.lastIndexOf("/");
                    if(last != -1){
                        String currentPackageDirName = name.substring(0, last);
                        //判断长度即可，效率比equals高
                        if(packageDirName.length() != currentPackageDirName.length()){
                            continue;
                        }
                    }
                }

                resourcesResults.add(name.replace('/', '.'));
            }
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     * 用于是在IDE调试项目或者web的文件方式的时候加载类路径的class方式。
     *
     * @param packageName 包名
     * @param packageFilePath 包所在文件路径
     * @param recursive 是否循环迭代
     * @param resourcesResults 结果集，存放结果
     * @throws Exception
     */
    private static void findResourcesNameUnderPackageByFilePath(final String packageName, String packageFilePath, final boolean recursive, List<String> resourcesResults, final String resourcesNameRegex) throws Exception {

        // 获取此包的目录 建立一个File
        File dir = new File(packageFilePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

//        资源正则是否为空
        final boolean resourcesNameRegexIsEmpty = resourcesNameRegex == null || "".equals(resourcesNameRegex);

        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || resourcesNameRegexIsEmpty || (file.getName().matches(resourcesNameRegex));
            }
        });

        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                if(StringUtils.isNotEmpty(packageName)){
                    findResourcesNameUnderPackageByFilePath(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, resourcesResults, resourcesNameRegex);

                }else{
                    findResourcesNameUnderPackageByFilePath(file.getName(), file.getAbsolutePath(), recursive, resourcesResults, resourcesNameRegex);
                }
            } else {
                // 添加到集合中去
                if(StringUtils.isNotEmpty(packageName)){
                    resourcesResults.add(packageName + '.' + file.getName());

                }else{
                    resourcesResults.add(file.getName());
                }
            }
        }
    }

    /**
     * 资源文件加载成InputStream
     * @param resourcesPath
     * @return
     */
    public static InputStream readResourcesAsInputStream(String resourcesPath) {
        return ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resourcesPath);
    }

    /**
     * 资源文件加载成InputStream
     * @param resourcesPath
     * @return
     */
    public static InputStream readResourcesAsInputStream(String resourcesPath, ClassLoader classLoader) {
        if (classLoader == null) {
            return ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resourcesPath);
        }else{
            return classLoader.getResourceAsStream(resourcesPath);
        }

    }

    /**
     * 资源文件加载成InputStream
     * @param resourcesPath
     * @return
     */
    public static String readResourcesAsString(String resourcesPath) throws Exception {
        return IOUtils.resourceToString(resourcesPath);
    }
    /**
     * 资源文件加载成String
     * @param resourcesPath
     * @return
     */
    public static String readResourcesAsString(String resourcesPath, ClassLoader classLoader) throws Exception {
        if (classLoader == null) {
            return IOUtils.resourceToString(resourcesPath, Charset.forName(CharsetType.UTF8), ClassLoaderUtils.getDefaultClassLoader());
        }else{
            return IOUtils.resourceToString(resourcesPath, Charset.forName(CharsetType.UTF8), classLoader);
        }
    }

    /**
     * 资源文件加载成InputStream
     * @param resourcesPath
     * @return
     */
    public static Reader readResourcesAsReader(String resourcesPath) {
        return new InputStreamReader(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resourcesPath));
    }

    /**
     * 资源文件加载成InputStream
     * @param resourcesPath
     * @return
     */
    public static Reader readResourcesAsReader(String resourcesPath, ClassLoader classLoader) {
        InputStream inputStream = readResourcesAsInputStream(resourcesPath, classLoader);
        if (inputStream != null) {
            return new InputStreamReader(inputStream);
        }
        return null;
    }

}

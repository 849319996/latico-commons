package com.latico.commons.common.util.logging;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <PRE>
 *  资源加载
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:48:00
 * @Version: 1.0
 */
public class ResourcesLoader {

    /**
     * 获取类上面带有指定注解
     *
     * @param clazz           被检测的类
     * @param annotationClass 检测的注解类类型
     * @param <A>             指定注解类型
     * @return 指定注解的注解实例
     */
    public static <A extends Annotation> A getAnnotationPresentOnClass(Class<?> clazz, Class<A> annotationClass) {
        if (!isAnnotationPresentOnClass(clazz, annotationClass)) {
            return null;
        }
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * 检测类上面是否带有指定注解
     *
     * @param clazz           被检测的类
     * @param annotationClass 检测的注解
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresentOnClass(Class<?> clazz, Class<A> annotationClass) {
        if (clazz == null) {
            return false;
        }
        if (annotationClass == null) {
            return false;
        }
        if (clazz.isAnnotationPresent(annotationClass)) {
            return true;
        }

        return false;
    }

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
    private static String convertResourcesNameToClassName(String resourcesName) {
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
        Enumeration<URL> resourcesDirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

        // 循环迭代下去
        while (resourcesDirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = resourcesDirs.nextElement();
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
            if (name.startsWith(packageDirName)) {
                // 如果是一个.class文件 而且不是目录，如果以"/"结尾 是一个包
                if (!entry.isDirectory()) {
                    //匹配资源的名字
                    if(resourcesNameRegex == null || "".equals(resourcesNameRegex) || name.matches(resourcesNameRegex)) {
                        //如果不是循环迭代，那就判断是否在当前包下
                        if(!recursive){
                            //截取最后一个斜杠前的字符串，判断是否跟当前包目录字符长度一样
                            int last = name.lastIndexOf("/");
                            if(last != -1){
                                String currentPackageDirName = name.substring(0, last);
                                if(packageDirName.length() != currentPackageDirName.length()){
                                    continue;
                                }
                            }
                        }

                        resourcesResults.add(name.replace('/', '.'));
                    }

                }
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
        boolean resourcesNameRegexIsEmpty = resourcesNameRegex == null || "".equals(resourcesNameRegex);

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
                if(packageName != null && !"".equals(packageName)){
                    findResourcesNameUnderPackageByFilePath(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, resourcesResults, resourcesNameRegex);

                }else{
                    findResourcesNameUnderPackageByFilePath(file.getName(), file.getAbsolutePath(), recursive, resourcesResults, resourcesNameRegex);
                }
            } else {
                // 添加到集合中去
                if(packageName != null && !"".equals(packageName)){
                    resourcesResults.add(packageName + '.' + file.getName());

                }else{
                    resourcesResults.add(file.getName());
                }
            }
        }
    }

}

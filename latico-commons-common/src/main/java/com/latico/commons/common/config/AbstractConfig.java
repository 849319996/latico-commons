package com.latico.commons.common.config;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.FieldUtils;
import com.latico.commons.common.util.reflect.ObjectUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.xml.XmlUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * 抽象的配置类
 * 1、先初始化资源配置文件，再初始化外部工程配置文件，这样外部配置优先级最高。
 * 2、工程自定义配置文件可以允许不存在，这样的话，全部配置都是默认配置。
 * 3、一般情况下，资源配置文件的内容跟工程配置文件内容一样或者资源配置文件内容更多，
 * 而只开放了很小一部分配置给工程配置文件
 *
 * 字段自动注入方法:
 * 可以使用注解 {@link FieldConfigNameAnnotation} 对字段跟配置文件中的字段关联，
 * 这样用反射进行字段值写入，可以参考示例{@link ConfigExample}

 //配置文件定义
 <config>
 <name1>value1</name1>
 <name2>value2</name2>
 <name3>3</name3>
 </config>

 //定义字段和配置文件的映射
 @FieldConfigNameAnnotation("name1")
 private String nameAnnotation;

 private String name2;

 @FieldConfigNameAnnotation("name3")
 private int nameInt;

 //反射注入
 Element rootElement = Dom4jUtils.getRootElement(fileContent);

 Map<String, String> childsNameValueMap = Dom4jUtils.getChildsNameValueMap(rootElement);

 //通过反射写入字段的值到对象字段
 writeFieldValue(this, childsNameValueMap);
 //或者下面的写法
 writeFieldValueToCurrentInstance(childsNameValueMap);

 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:24:53
 * @Version: 1.0
 */
public abstract class AbstractConfig implements Config {

    /** LOG 日志工具 */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfig.class);

    /**
     * 加载或者刷新配置
     * 先加载资源配置，后加载工程配置，工程配置可以覆盖资源配置
     *
     * @return true:成功 false:失败
     */
    @Override
    public synchronized boolean initOrRefreshConfig() {
        boolean succ = true;
        try {
            //初始化资源配置文件
            String resourcesConfigFileContent= loadResourcesConfigFileToString(getResourcesConfigFilePath());
            if (StringUtils.isNotBlank(resourcesConfigFileContent)) {
                succ &= initResourcesConfigFile(resourcesConfigFileContent);
            }

            //初始化文件系统配置文件
            String configFileContent= loadConfigFileToString(getConfigFilePath());
            if (StringUtils.isNotBlank(configFileContent)) {
                succ &= initConfigFile(configFileContent);
            }

            //初始化其他配置文件
            initOtherConfig();
        } catch (Throwable e) {
            System.out.println("加载配置文件异常");
            e.printStackTrace(System.err);
            LOG.error("加载配置文件异常", e);
            succ = false;
        }

        //打印配置信息
        LOG.info(getConfigInfo());
        return succ;
    }

    /**
     * 加载工程配置文件内容成字符串
     * @param configFilePath 工程配置文件路径
     * @return
     */
    protected String loadConfigFileToString(String configFilePath) {
        if (StringUtils.isBlank(configFilePath)) {
            return "";
        }
        try {
            File file = new File(configFilePath);
            if (file.exists()) {
                //用支持读取XML文件头的方式加载文件
                return XmlUtils.readXmlFileToString(configFilePath);
            } else {
                System.out.println("配置文件不存在(允许):" + configFilePath);
                LOG.warn("配置文件不存在(允许):" + configFilePath);
            }
        } catch (Throwable e) {
            System.err.println("加载配置文件存在异常:" + configFilePath);
            e.printStackTrace(System.err);
            LOG.error("加载配置文件存在异常:" + configFilePath, e);
        }
        return "";
    }

    /**
     * 用支持加载XML格式的方式加载配置文件
     * @param resourcesConfigFilePath 资源配置文件路径
     */
    protected String loadResourcesConfigFileToString(String resourcesConfigFilePath) {
        if (StringUtils.isBlank(resourcesConfigFilePath)) {
            return "";
        }
        try {
            //用支持读取XML文件头的方式加载文件
            return XmlUtils.readResourcesXmlFileToString(resourcesConfigFilePath);
        } catch (Throwable e) {
            System.err.println("加载资源配置文件存在异常:" + resourcesConfigFilePath);
            e.printStackTrace(System.err);
            LOG.error("加载资源配置文件存在异常:" + resourcesConfigFilePath, e);
        }
        return "";
    }

    /**
     * @return 配置信息，字符串形式,默认读取{@link #toString }
     */
    protected String getConfigInfo() {
        return toString();
    }

    /**
     * 获取资源配置文件路径
     * @return 资源配置文件路径
     */
    protected abstract String getResourcesConfigFilePath();

    /**
     * @return 工程配置文件路径
     */
    protected abstract String getConfigFilePath();

    /**
     * 初始化资源配置文件，默认调用{@link #initConfig}
     *
     * @param fileContent 资源配置文件内容
     */
    protected boolean initResourcesConfigFile(String fileContent) throws Exception {
        return initConfig(fileContent);
    }

    /**
     * 初始化配置文件，默认调用{@link #initConfig}
     * @param fileContent 工程配置文件内容
     * @return 是否成功
     */
    protected boolean initConfigFile(String fileContent) throws Exception {
        return initConfig(fileContent);
    }

    /**
     * 初始化配置，默认情况下initResourcesConfigFile和initConfigFile都调用该方法
     * 因为一般情况下，资源配置文件的内容跟工程配置文件内容一样或者资源配置文件内容更多，
     * 而只开放了很小一部分配置给工程配置文件；
     * @param fileContent 配置文件内容
     * @return 是否成功
     * @throws Exception
     */
    protected abstract boolean initConfig(String fileContent) throws Exception;

    /**
     * 写字段值到当前实例
     * @param fieldNameValueMap
     * @throws Exception
     */
    protected void writeFieldValueToCurrentInstance(Map<String, ?> fieldNameValueMap) throws Exception {
        writeFieldValue(this, fieldNameValueMap);
    }

    /**
     * 写字段值到指定实例
     * @param instance 对象实例
     * @param fieldNameValueMap
     * @throws Exception
     */
    protected static void writeFieldValue(Object instance, Map<String, ?> fieldNameValueMap) throws Exception {
        Map<String, Field> nameFieldMap = getConfigFieldNameMap(instance.getClass());
        writeValueToSimpleField(instance, fieldNameValueMap, nameFieldMap);
    }

    /**
     * 写简单类型字段
     * @param instance
     * @param fieldNameValueMap
     * @param nameFieldMap
     * @throws Exception
     */
    protected static void writeValueToSimpleField(Object instance, Map<String, ?> fieldNameValueMap, Map<String, Field> nameFieldMap) throws Exception {
        ObjectUtils.writeValueToSimpleField(instance, nameFieldMap, fieldNameValueMap);
    }

    /**
     * 获取配置字段名字map，优先使用配置FieldConfigNameAnnotation注解的字段，如果没有，那就使用字段名称
     * @param clazz
     * @return
     */
    protected static Map<String, Field> getConfigFieldNameMap(Class clazz) {
        Map<String, Field> allFieldNameMap = FieldUtils.getAllFieldNameMap(clazz, true);

        Map<String, Field> nameFieldMap = new HashMap<>();
        for (Map.Entry<String, Field> entry : allFieldNameMap.entrySet()) {
            Field field = entry.getValue();
            FieldConfigNameAnnotation annotation = FieldUtils.getAnnotationPresentOnField(field, FieldConfigNameAnnotation.class);

            if (annotation == null || "".equals(annotation.value())) {
                nameFieldMap.put(field.getName(), field);
            }else{
                //就算添加了字段信息注解，字段原名称也添加
                nameFieldMap.put(field.getName(), field);
                nameFieldMap.put(annotation.value(), field);
            }
        }
        return nameFieldMap;
    }

    /**
     * 初始化其他配置,比如数据库的数据源配置，还有比如：
     * 因为一般一个程序或者一个组件只拥有一个配置文件是最好的，但是其他更多配置，那么预留这个方法来加载。
     */
    protected abstract void initOtherConfig();
}

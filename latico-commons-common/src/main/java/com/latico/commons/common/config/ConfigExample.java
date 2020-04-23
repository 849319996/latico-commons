package com.latico.commons.common.config;

import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.xml.Dom4jUtils;
import org.dom4j.Element;

import javax.sql.DataSource;
import java.util.Map;

/**
 * <PRE>
 * 配置加载示例
 * <p>
 * 具体XML配置文件configExample.xml的内容如下：
 * <config>
 * <common commonId="commonIdValue" commonName="commonNameValue">
 * <name1>value1</name1>
 * <name2 a="1" ag="3" >value2</name2>
 * <name3>3</name3>
 * <name4>3</name4>
 * <name5>5</name5>
 * <name6></name6>
 * </common>
 * </config>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-27 11:24:29
 * @version: 1.0
 */
public class ConfigExample extends AbstractConfig {
    /**
     * instance 单例实例
     */
    private static volatile ConfigExample instance = null;

    /**
     * 数据源
     */
    private volatile DataSource dataSource;

    /**
     * 默认配置文件
     */
    private final String resourcesConfigFile = RESOURCES_CONFIG_FILE_ROOT_DIR + "configExample.xml";
    /**
     * 配置文件，会覆盖默认配置文件，该文件是实际项目需要使用的时候添加,adapterFilePathSupportWebContainer的作用是增加识别WEB-INF目录（war包部署方式）
     */
    private final String configFile = PathUtils.adapterFilePathSupportWebContainer(CONFIG_FILE_ROOT_DIR + "configExample.xml");

    /**
     * 读取common节点上面的commonId属性
     */
    private String commonId;
    /**
     * 读取common节点上面的commonName属性
     */
    private String commonName;
    /**
     * 使用注解转换字段名称
     */
    @FieldConfigNameAnnotation("name1")
    private String nameAnnotation;

    /**
     * 不适用注解，直接匹配跟配置文件的一样
     */
    private String name2;
    /**
     * int类型，使用注解转换字段名称
     */
    @FieldConfigNameAnnotation("name3")
    private int nameInt;

    private String name5;


    private ConfigExample() {
        initOrRefreshConfig();
    }

    /**
     * 获取单例，同步双重校验的好处在于，兼顾了效率、支持延迟加载、可以再创建对象后，
     * 调用方法进行初始化，而不需要在构造方法初始化，因为有些参数的加载，需要对象创建成功后进行
     *
     * @return
     */
    public static ConfigExample getInstance() {
        if (instance == null) {
            synchronized (ConfigExample.class) {
                if (instance == null) {
                    instance = new ConfigExample();
                }
            }
        }
        return instance;
    }

    @Override
    protected void initOtherConfig() {

        //有些数据源不能重复初始化
        if (this.dataSource == null) {
            synchronized (this) {
                if (this.dataSource == null) {
                    initDataSource();
                }
            }
        }
    }

    /**
     * 初始化数据源
     */
    private void initDataSource() {
    }

    /**
     * 获取资源配置文件路径
     *
     * @return 资源配置文件路径
     */
    @Override
    protected String getResourcesConfigFilePath() {
        return resourcesConfigFile;
    }

    /**
     * @return 工程配置文件路径
     */
    @Override
    protected String getConfigFilePath() {
        return configFile;
    }

    /**
     * 初始化配置，默认情况下initResourcesConfigFile和initConfigFile都调用该方法
     * 因为一般情况下，资源配置文件的内容跟工程配置文件内容一样或者资源配置文件内容更多，
     * 而只开放了很小一部分配置给工程配置文件；
     *
     * @param fileContent 配置文件内容
     * @return 是否成功
     * @throws Exception
     */
    @Override
    protected boolean initConfig(String fileContent) throws Exception {
        Element rootElement = Dom4jUtils.getRootElement(fileContent);

        Element commonEle = rootElement.element("demo");

//        把属性写进本对象
        Map<String, String> allAttributeNameValueMap = Dom4jUtils.getAllAttributeNameValueMap(commonEle);
        writeFieldValue(this, allAttributeNameValueMap);

        //        把子列表的值写进本对象
        Map<String, String> childsNameValueMap = Dom4jUtils.getChildsNameValueMap(commonEle);
        //通过反射写入字段的值
//        writeFieldValue(this, childsNameValueMap);
        writeFieldValueToCurrentInstance(childsNameValueMap);

        return true;
    }

    public String getResourcesConfigFile() {
        return resourcesConfigFile;
    }

    public String getConfigFile() {
        return configFile;
    }

    public String getNameAnnotation() {
        return nameAnnotation;
    }

    public String getName2() {
        return name2;
    }

    public int getNameInt() {
        return nameInt;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public String getCommonId() {
        return commonId;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getName5() {
        return name5;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ConfigExample{");
        sb.append("dataSource=").append(dataSource);
        sb.append(", resourcesConfigFile='").append(resourcesConfigFile).append('\'');
        sb.append(", configFile='").append(configFile).append('\'');
        sb.append(", commonId='").append(commonId).append('\'');
        sb.append(", commonName='").append(commonName).append('\'');
        sb.append(", nameAnnotation='").append(nameAnnotation).append('\'');
        sb.append(", name2='").append(name2).append('\'');
        sb.append(", nameInt=").append(nameInt);
        sb.append(", name5='").append(name5).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

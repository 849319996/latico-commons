package com.latico.commons.orm.mybatisplus;

import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.util.io.PropertiesUtils;
import com.latico.commons.orm.mybatisplus.generator.GeneratorConfigInfo;
import com.latico.commons.orm.mybatisplus.generator.MybatisPlusGenerator;

import java.util.Properties;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-31 23:24
 * @version: 1.0
 */
public class MybatisPlusUtils {
    /**
     * @param resourcesConfigFile 资源目录下Properties配置文件
     */
    public static void execGeneratorByResourcesPropertiesFile(String resourcesConfigFile) throws Exception {

        Properties properties = PropertiesUtils.readPropertiesFromResources(resourcesConfigFile, CharsetType.UTF8);

        GeneratorConfigInfo config = MybatisPlusGenerator.getGeneratorConfigInfo(properties);

        MybatisPlusGenerator.execGenerator(config);
    }

    /**
     * @param configFilePath Properties配置文件
     */
    public static void execGeneratorByPropertiesFile(String configFilePath) throws Exception {

        Properties properties = PropertiesUtils.readPropertiesFromFile(configFilePath, CharsetType.UTF8);

        GeneratorConfigInfo config = MybatisPlusGenerator.getGeneratorConfigInfo(properties);

        MybatisPlusGenerator.execGenerator(config);
    }

    /**
     * 执行代码生成器
     * @param generatorConfigInfo
     */
    public static void execGenerator(GeneratorConfigInfo generatorConfigInfo) {
        MybatisPlusGenerator.execGenerator(generatorConfigInfo);
    }
}

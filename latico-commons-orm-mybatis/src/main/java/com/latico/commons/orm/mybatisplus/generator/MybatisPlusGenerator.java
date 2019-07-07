package com.latico.commons.orm.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.latico.commons.common.util.logging.Log;
import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.other.BooleanUtils;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * <PRE>
 * Mybatis-Plus代码生成插件
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-31 23:26
 * @Version: 1.0
 */
public class MybatisPlusGenerator {
    private static final Log LOG = LogUtils.getLog(MybatisPlusGenerator.class);
    /**
     * 执行代码生成器
     *
     * @param generatorConfigInfo 配置信息
     */
    public static void execGenerator(GeneratorConfigInfo generatorConfigInfo) {
        LOG.info("生成器配置:{}", generatorConfigInfo);
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 全局配置
        GlobalConfig globalConfig = createGlobalConfig(generatorConfigInfo);
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = createDataSourceConfig(generatorConfigInfo);
        autoGenerator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = createPackageConfig(generatorConfigInfo);
        autoGenerator.setPackageInfo(packageConfig);

        // 策略配置
        StrategyConfig strategy = createStrategyConfig(generatorConfigInfo);
        autoGenerator.setStrategy(strategy);

        //模板引擎
        AbstractTemplateEngine templateEngine = createTemplateEngine(generatorConfigInfo);
        autoGenerator.setTemplateEngine(templateEngine);

        //自定义配置
        InjectionConfig injectionConfig = createInjectionConfig(generatorConfigInfo);
        autoGenerator.setCfg(injectionConfig);

        //执行
        autoGenerator.execute();
    }

    /**
     * 代码生成配置信息
     * @param properties
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static GeneratorConfigInfo getGeneratorConfigInfo(Properties properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        GeneratorConfigInfo config = new GeneratorConfigInfo();

        Object outputDir = properties.get("outputDir");
        if (StringUtils.isNotEmpty(outputDir)) {
            config.setOutputDir(outputDir.toString());
        }

        config.setAuthor(properties.get("author").toString());
        Object dbType = properties.get("dbType");
        if (StringUtils.isNotEmpty(dbType)) {
            config.setDbType(DbType.getDbType(dbType.toString()));
        }

        config.setDriverName(properties.get("driverName").toString());

        config.setDbUrl(properties.get("dbUrl").toString());
        config.setUsername(properties.get("username").toString());
        config.setPassword(properties.get("password").toString());

        Object typeConvertClassName = properties.get("typeConvertClassName");
        if(StringUtils.isNotEmpty(typeConvertClassName)){
            Class<ITypeConvert> iTypeConvertClass = (Class<ITypeConvert>) Class.forName(typeConvertClassName.toString());
            config.setTypeConvert(iTypeConvertClass.newInstance());
        }

        //自定义转换

        config.setPackageModuleName(properties.get("packageModuleName").toString());
        config.setPackageParent(properties.get("packageParent").toString());

        Object mapperXmlFileRelativeDir = properties.get("mapperXmlFileRelativeDir");
        if (StringUtils.isNotEmpty(mapperXmlFileRelativeDir)) {
            config.setMapperXmlFileRelativeDir(mapperXmlFileRelativeDir.toString());
        }

        String tableNameStr = properties.get("tableNames").toString();

        String[] tableNames = tableNameStr.split("[,，;；\\s]+");
        config.setTableNames(Arrays.asList(tableNames));

        String underlineToCamel = properties.get("underlineToCamel").toString();
        if (BooleanUtils.isFalseOr0(underlineToCamel)) {
            config.setUnderlineToCamel(false);
        }else{
            config.setUnderlineToCamel(true);
        }
        return config;
    }

    private static InjectionConfig createInjectionConfig(final GeneratorConfigInfo generatorConfigInfo) {
        //自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();

        FileOutConfig xmlFileOutConfig = new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {

                // 自定义输入文件名称
                String mapperXmlFilePath = generatorConfigInfo.getOutputDir();

                mapperXmlFilePath = PathUtils.combine(generatorConfigInfo.getOutputDir(), generatorConfigInfo.getMapperXmlFileRelativeDir());

                if (generatorConfigInfo.getPackageModuleName() != null && !"".equals(generatorConfigInfo.getPackageModuleName())) {
                    mapperXmlFilePath = PathUtils.combine(mapperXmlFilePath, generatorConfigInfo.getPackageModuleName());
                }
                mapperXmlFilePath = PathUtils.combine(mapperXmlFilePath, tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML);


                return mapperXmlFilePath;
            }
        };
        focList.add(xmlFileOutConfig);
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    private static AbstractTemplateEngine createTemplateEngine(GeneratorConfigInfo generatorConfigInfo) {
        return new FreemarkerTemplateEngine();
    }

    private static PackageConfig createPackageConfig(GeneratorConfigInfo generatorConfigInfo) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(generatorConfigInfo.getPackageModuleName());
        packageConfig.setParent(generatorConfigInfo.getPackageParent());
        return packageConfig;
    }

    private static StrategyConfig createStrategyConfig(GeneratorConfigInfo generatorConfigInfo) {
        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        if (generatorConfigInfo.isUnderlineToCamel()) {
            strategy.setNaming(NamingStrategy.underline_to_camel);
        }else{
            strategy.setNaming(NamingStrategy.nochange);
        }

        //自定义继承的Entity类全称，带包名
        //strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");

        //是否为lombok模型（默认 false）
        strategy.setEntityLombokModel(false);

        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);

        //自定义继承的Controller类全称，带包名
        //strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        //需要包含的表名，允许正则表达式（与exclude二选一配置）
//        strategy.setInclude(scanner("表名"));
//        strategy.setInclude("t_ems","t_host","t_interface","t_net_link","t_script_ability","t_script_template","t_script_template_detail");

        //需要包含的表名，允许正则表达式（与exclude二选一配置）
        strategy.setInclude(generatorConfigInfo.getTableNames().toArray(new String[generatorConfigInfo.getTableNames().size()]));
//        strategy.setInclude("user");
        //自定义基础的Entity类，公共字段
        //strategy.setSuperEntityColumns("id");

        //驼峰转连字符
        strategy.setControllerMappingHyphenStyle(false);

        return strategy;
    }

    private static DataSourceConfig createDataSourceConfig(GeneratorConfigInfo generatorConfigInfo) {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(generatorConfigInfo.getDbType());
        dataSourceConfig.setUrl(generatorConfigInfo.getDbUrl());
        dataSourceConfig.setDriverName(generatorConfigInfo.getDriverName());
        dataSourceConfig.setUsername(generatorConfigInfo.getUsername());
        dataSourceConfig.setPassword(generatorConfigInfo.getPassword());

        //如果是pdm由oracle转换mysql后的数据类型，需要使用自定义的字段转换器
        if (generatorConfigInfo.getTypeConvert() != null) {
            dataSourceConfig.setTypeConvert(generatorConfigInfo.getTypeConvert());
        }

        return dataSourceConfig;
    }

    private static GlobalConfig createGlobalConfig(GeneratorConfigInfo generatorConfigInfo) {
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();

        globalConfig.setOutputDir(generatorConfigInfo.getOutputDir());
        globalConfig.setAuthor(generatorConfigInfo.getAuthor());
        globalConfig.setOpen(generatorConfigInfo.isOpenDirWhenFinish());

        //开启 BaseResultMap
        globalConfig.setBaseResultMap(true);

        //生成资本列信息到xml
        globalConfig.setBaseColumnList(true);

        //覆盖已有文件
        globalConfig.setFileOverride(true);

        //时间策略
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        return globalConfig;
    }

}

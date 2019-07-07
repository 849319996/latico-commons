package com.latico.commons.orm.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.latico.commons.common.envm.DBTypeEnum;
import com.latico.commons.orm.mybatisplus.MybatisPlusUtils;
import com.latico.commons.common.util.time.DateTimeUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * <PRE>
 *
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-06 15:15:24
 * @Version: 1.0
 */
public class MybatisPlusGeneratorTest {

    /**
     * 直接执行，可以生成mybatis的代码到程序当前目录
     */
    @Test
    public void execGeneratorByResourcesConfigFile() {
        try {
            MybatisPlusUtils.execGeneratorByResourcesPropertiesFile("config/mybatis-plus-generator.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void execGenerator() {
        GeneratorConfigInfo config = new GeneratorConfigInfo();

        config.setOutputDir("./_generator/" + DateTimeUtils.toStr(new Date(System.currentTimeMillis()), "yyyyMMddHHmmss"));

        config.setAuthor("latico");

        config.setDbType(DbType.MYSQL);
        config.setDriverName(DBTypeEnum.MYSQL.DRIVER);

        config.setDbUrl("jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf-8");
        config.setUsername("root");
        config.setPassword("root");

        //自定义转换
//        config.setTypeConvert(new TypeConvertImpl());

        config.setPackageModuleName("test");
        config.setPackageParent("com.latico.commons.dao");

        config.setMapperXmlFileRelativeDir("resources/config/mapper");

        List<String> tableNames = new ArrayList<>();
        tableNames.add("user");
        config.setTableNames(tableNames);

        MybatisPlusGenerator.execGenerator(config);
    }

}
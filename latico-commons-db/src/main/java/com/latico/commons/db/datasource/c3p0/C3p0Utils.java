package com.latico.commons.db.datasource.c3p0;

import com.latico.commons.db.datasource.DataSourceParam;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;

/**
 * <PRE>
 * C3P0数据源，使用方式如下：
 * 1、在src目录下创建c3p0-config.xml文件写入下面xml配置即可；
 * 2、自定义参数创建数据源；
 * 3、每个数据源只能实例化一次；
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-02 21:02
 * @Version: 1.0
 */
public class C3p0Utils {

    /**
     * 默认方式获取数据源，默认会读取src下的c3p0-config.xml文件的<default-config>节点的配置
     * @return
     */
    public static ComboPooledDataSource createDataSourceDefault() {
        return new ComboPooledDataSource();
    }

    /**
     * 默认会读取src下的c3p0-config.xml文件的<default-config>节点的配置,
     * @param dataSourceId
     * @return
     */
    public static ComboPooledDataSource createDataSource(String dataSourceId) {
        return new ComboPooledDataSource(dataSourceId);
    }

    /**
     * 指定参数创建数据源
     * @param dsp
     * @return
     * @throws Exception
     */
    public static ComboPooledDataSource createDataSource(DataSourceParam dsp) throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        //设置url
        dataSource.setJdbcUrl(dsp.getUrl());

        //设置驱动
        dataSource.setDriverClass(dsp.getDriverClassName());

        //用户名
        dataSource.setUser(dsp.getUsername());

        //密码
        dataSource.setPassword(dsp.getPassword());

        //初始连接数
        dataSource.setInitialPoolSize(dsp.getInitialSize());

        //最大连接数
        dataSource.setMaxPoolSize(dsp.getMaxActive());

        //最大空闲时间,单位秒
        dataSource.setMaxIdleTime(dsp.getMaxActiveTime()/1000);
        return dataSource;
    }
}

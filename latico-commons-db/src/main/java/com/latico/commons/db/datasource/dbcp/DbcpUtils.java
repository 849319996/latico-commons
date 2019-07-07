package com.latico.commons.db.datasource.dbcp;

import com.latico.commons.db.datasource.DataSourceParam;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * <PRE>
 * apache的DBCP数据源
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-02 17:44
 * @Version: 1.0
 */
public class DbcpUtils {

    /**
     * @param dsp
     * @return
     * @throws Exception
     */
    public static DataSource createDataSource(DataSourceParam dsp) throws Exception {
        Properties prop = new Properties();
        //使用对象的形式
        prop.setProperty("driverClassName", dsp.getDriverClassName());
        prop.setProperty("url", dsp.getUrl());
//        prop.setProperty("connectionProperties", dsp.getConnectionProperties());
        prop.setProperty("username", dsp.getUsername());
        prop.setProperty("password", dsp.getPassword());
        prop.setProperty("initialSize", dsp.getInitialSize() + "");
        prop.setProperty("maxActive", dsp.getMaxActive() + "");
        prop.setProperty("maxIdle", dsp.getMaxIdle() + "");
        prop.setProperty("minIdle", dsp.getMinIdle() + "");
        prop.setProperty("maxWait", dsp.getMaxActiveTime() + "");

        return createDataSource(prop);
    }

    /**
     * 创建数据源
     * @param prop Properties类型入参
     * @return
     * @throws Exception
     */
    public static DataSource createDataSource(Properties prop) throws Exception {
        return BasicDataSourceFactory.createDataSource(prop);
    }
}

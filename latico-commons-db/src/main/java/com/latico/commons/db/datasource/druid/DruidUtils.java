package com.latico.commons.db.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.latico.commons.db.datasource.DataSourceParam;

/**
 * 阿里巴巴的数据源
 * <PRE>
 DataSourceParam dsp = new DataSourceParam("druid", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

 DruidDataSource druidDataSource = DataSourceUtils.createDruidDataSource(dsp);
 DruidPooledConnection conn = null;
 try {
     conn = druidDataSource.getConnection();
     long id = DBUtils.queryInt(conn, "select id from user where username='name2'");
     System.out.println(id);
 } catch (SQLException e) {
    e.printStackTrace();
 }finally {
    DBUtils.close(conn);
 }
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-02 14:07
 * @Version: 1.0
 */
public class DruidUtils {

    /**
     * 创建数据源
     * @param dsp 数据源参数
     * @return
     */
    public static DruidDataSource createDataSource(DataSourceParam dsp) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(dsp.getDriverClassName());
        ds.setUrl(dsp.getUrl());
        ds.setUsername(dsp.getUsername());
        ds.setPassword(dsp.getPassword());
        ds.setMaxActive(dsp.getMaxActive());
        ds.setKeepAlive(dsp.isKeepAlive());
        ds.setDbType(dsp.getDbType());
        ds.setInitialSize(dsp.getInitialSize());
        ds.setMaxWait(dsp.getMaxActiveTime());
        ds.setMinIdle(dsp.getMinIdle());

        return ds;
    }
}

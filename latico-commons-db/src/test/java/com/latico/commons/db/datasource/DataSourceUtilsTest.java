package com.latico.commons.db.datasource;

import com.latico.commons.db.datasource.c3p0.C3p0Utils;
import com.latico.commons.common.envm.DBTypeEnum;
import com.latico.commons.common.util.db.DBUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;

public class DataSourceUtilsTest {

    @Test
    public void createDruidDataSource() {
        DataSourceParam dsp = new DataSourceParam("druid", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceUtils.createDruidDataSource(dsp);
            conn = dataSource.getConnection();

            long id = DBUtils.queryInt(conn, "select id from user where username='name2'");

            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }
    }

    @Test
    public void createProxoolDataSource() {
        DataSourceParam dsp = new DataSourceParam("Proxool", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceUtils.createProxoolDataSource(dsp);
            conn = dataSource.getConnection();

            long id = DBUtils.queryInt(conn, "select id from user where username='name2'");

            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }
    }

    @Test
    public void createDbcpDataSource() {
        DataSourceParam dsp = new DataSourceParam("Proxool", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceUtils.createDbcpDataSource(dsp);
            conn = dataSource.getConnection();

            long id = DBUtils.queryInt(conn, "select id from user where username='name2'");

            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }
    }

    @Test
    public void createC3p0DataSource() {
        DataSourceParam dsp = new DataSourceParam("C3p0", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = DataSourceUtils.createC3p0DataSource(dsp);
            conn = dataSource.getConnection();

            long id = DBUtils.queryInt(conn, "select id from user where username='name2'");

            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }
    }

    @Test
    public void createC3p0DataSource2() {
        DataSourceParam dsp = new DataSourceParam("C3p0", DBTypeEnum.MYSQL.DRIVER, "jdbc:mysql://localhost:3306/test?characterEncoding=utf-8&useUnicode=true&autoReconnect=true", "root", "root");

        DataSource dataSource = null;
        Connection conn = null;
        try {
            dataSource = C3p0Utils.createDataSource("mydb");
            conn = dataSource.getConnection();

            long id = DBUtils.queryInt(conn, "select id from user where username='name2'");

            System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }
    }

    @Test
    public void getDataSourceParamByPropertiesResourcesFile() {
        try {
            System.out.println(DataSourceUtils.getDataSourceParamByPropertiesResourcesFile("config/db.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMultiDataSourceParamByPropertiesResourcesFile() {
        try {
            System.out.println(DataSourceUtils.getMultiDataSourceParamByPropertiesResourcesFile("config/multi-db-config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.latico.commons.db.sql;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import com.latico.commons.common.util.db.DBUtils;
import com.latico.commons.common.util.logging.LogUtils;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

public class DBUtilsTest {
    @Test
    public void loadLogback() {
        LogUtils.loadLogBackConfigDefault();
//        loadLogBackConfigFromResources("/config/logback.xml");
    }

    public static void loadLogBackConfigFromResources(String logbackConfPath) {

        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            InputStream resourceAsStream = DBUtilsTest.class.getResourceAsStream(logbackConfPath);
            loadLogBackConfig(resourceAsStream);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("Fail to load logBack configure file: " + logbackConfPath);
        }

    }
    /**
     * 使用流的方式加载日志文件
     *
     * @param is 输入流
     */
    public static void loadLogBackConfig(InputStream is) {
//添加系统属性
        try {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();

            configurator.doConfigure(is);
        } catch (Throwable e) {
            e.printStackTrace(System.err);
            System.err.println("加载loopback配置异常");
        }

    }
    @Test
    public void test() {
        DBUtils.registerDriverMySql();
        Connection conn = DBUtils.getConnectionByMysql("localhost", 3306, "test", "root", "root");

        Object[] inParam = new Object[]{1};
        int[] outParamType = new int[]{Types.VARCHAR};
        try {
            Object[] getnamebyids = DBUtils.execProcedure(conn, "getnamebyid", inParam, outParamType);
            System.out.println(Arrays.toString(getnamebyids));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
        }

    }

    @Test
    public void execProcedure(){

        Connection conn = DBUtils.getConnectionByOracle("172.168.27.7", 1522, "latico11g", "IPRAN2", "IPRAN2");

        try {
            Arrays.toString(DBUtils.execProcedure(conn, "L_P_D_PREF", null, null));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
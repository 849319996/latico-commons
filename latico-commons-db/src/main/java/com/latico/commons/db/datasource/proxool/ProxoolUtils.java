package com.latico.commons.db.datasource.proxool;

import com.latico.commons.db.datasource.DataSourceParam;
import com.latico.commons.common.envm.CharsetType;
import com.latico.commons.common.envm.DBTypeEnum;
import com.latico.commons.common.util.db.DBUtils;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

/**
 * <PRE>
 * proxool数据源，应该是最轻量级
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-01-02 15:40
 * @Version: 1.0
 */
public class ProxoolUtils {

    /**
     * 创建数据源,因为是创建单个，所以直接返回数据源
     * @param dsp
     * @return
     */
    public static ProxoolDataSource createDataSource(DataSourceParam dsp) throws Exception {

        if (DBUtils.registerDriver(DBTypeEnum.PROXOOL.DRIVER)) {
            //使用Properties方式注册
            Properties properties = getProperties(dsp);
            register(properties);
            return getDataSource(dsp.getId());
        }

        return null;
    }

    /**
     * 获取数据源
     * @param alias 数据源ID
     * @return
     */
    public static ProxoolDataSource getDataSource(String alias){
        return new ProxoolDataSource(alias);
    }

    /**
     * 创建数据源，可能存在多个，所以此处不返回哪个数据源，具体要使用哪个，就自己调用获取{@link ProxoolUtils#getDataSource(String)}
     * @param dsp
     * @return
     */
    public static boolean register(Properties dsp) throws Exception {

        if (DBUtils.registerDriver(DBTypeEnum.PROXOOL.DRIVER)) {
            PropertyConfigurator.configure(dsp);
            return true;
        }
        return false;
    }

    /**
     * XML配置方式创建数据源，可能存在多个，所以此处不返回哪个数据源，具体要使用哪个，就自己调用获取{@link ProxoolUtils#getDataSource(String)}
     * @param proxoolXml
     * @return
     */
    public static boolean registerDataSourceByXml(String proxoolXml) throws Exception {

        if (DBUtils.registerDriver(DBTypeEnum.PROXOOL.DRIVER)) {
            Reader reader = new StringReader(proxoolXml);
            JAXPConfigurator.configure(reader, false);
            return true;
        }
        return false;
    }

    /**
     * 需要组装成下面这种格式：
     jdbc-1.proxool.alias=test
     #jdbc-1.proxool.driver-class=oracle.jdbc.OracleDriver
     #jdbc-1.proxool.driver-url=jdbc:oracle:thin:@127.0.0.1:1521:orcl
     jdbc-1.proxool.driver-class=com.mysql.jdbc.Driver
     jdbc-1.proxool.driver-url=jdbc:mysql://localhost:3306/db_course
     jdbc-1.user=root
     jdbc-1.password=root

     jdbc-1.proxool.maximum-connection-count=8
     jdbc-1.proxool.minimum-connection-count=5
     jdbc-1.proxool.prototype-count=4
     jdbc-1.proxool.verbose=true
     jdbc-1.proxool.statistics=10s,1m,1d
     jdbc-1.proxool.statistics-log-level=error
     * @param dsp
     * @return
     */
    private static Properties getProperties(DataSourceParam dsp) {
        Properties properties = new Properties();
        //前缀
        final String prefix = "jdbc-" + dsp.getId() + ".proxool.";
        properties.put(prefix +  "alias", dsp.getId());
        properties.put(prefix +  "driver-url", dsp.getUrl());
        properties.put(prefix +  "driver-class", dsp.getDriverClassName());
        properties.put(prefix +  "user", dsp.getUsername());
        properties.put(prefix +  "password", dsp.getPassword());
        properties.put(prefix +  "characterEncoding", dsp.getCharset());

//        单个连接的最大激活时间（即超时时间）
        properties.put(prefix +  "maximum-active-time", String.valueOf(dsp.getMaxActiveTime()));

//        properties.put(prefix +  "house-keeping-test-sql", ds.getHouseKeepingTestSql());
//        properties.put(prefix +  "house-keeping-sleep-time", String.valueOf(ds.getHouseKeepingSleepTime()));

//        可同时建立的最大连接数.
//	 *  即可同时新增的连接请求, 但还没有可供使用的连接.
//        properties.put(prefix +  "simultaneous-build-throttle", String.valueOf(ds.getSimultaneousBuildThrottle()));

//        可同时存在的最大连接数
        properties.put(prefix +  "maximum-connection-count", String.valueOf(dsp.getMaxActive()));

        //(连接池)保有的最少连接数
        properties.put(prefix +  "minimum-connection-count", String.valueOf(dsp.getMinIdle()));

        //当没有空闲连接可以分配时, 在队列中等候的最大请求数.
        //	 * 超过这个请求数的用户连接就不会被接受
//        properties.put(prefix +  "maximum-new-connections", String.valueOf(dsp.getMaxActive()));

        properties.put(prefix +  "prototype-count", String.valueOf(dsp.getMinIdle()));

//        单个连接的最大使用寿命，默认跟最大激活时间一样
        properties.put(prefix +  "maximum-connection-lifetime", String.valueOf(dsp.getMaxLifeTime()));

        //目前不配置是否检测连接是否被使用
//        properties.put(prefix +  "test-before-use", String.valueOf(ds.isTestBeforeUse()));
//        properties.put(prefix +  "test-after-use", String.valueOf(ds.isTestAfterUse()));
        properties.put(prefix +  "trace", String.valueOf(false));
        return properties;
    }


    /**
     * XML方式加载配置
     * @param dsp
     * @throws Exception
     */
    private static boolean registerByXml(DataSourceParam dsp) throws Exception {
        String proxoolXml = getProxoolXml(dsp);
        return registerDataSourceByXml(proxoolXml);
    }

    /**
     * 需要组装成下面的格式，使用模板替换：
     *
     <?xml version="1.0" encoding="ISO-8859-1"?>
     <something-else-entirely>
     <proxool>
     <alias>@{alias}@</alias>
     <driver-url>@{driver-url}@</driver-url>
     <driver-class>@{driver-class}@</driver-class>
     <driver-properties>
     <property name="user" value="@{username}@" />
     <property name="password" value="@{password}@" />
     <property name="characterEncoding" value="@{characterEncoding}@"/>
     </driver-properties>
     <maximum-active-time>@{maximum-active-time}@</maximum-active-time>
     <house-keeping-test-sql>@{house-keeping-test-sql}@</house-keeping-test-sql>
     <house-keeping-sleep-time>@{house-keeping-sleep-time}@</house-keeping-sleep-time>
     <simultaneous-build-throttle>@{simultaneous-build-throttle}@</simultaneous-build-throttle>
     <maximum-connection-count>@{maximum-connection-count}@</maximum-connection-count>
     <minimum-connection-count>@{minimum-connection-count}@</minimum-connection-count>
     <maximum-new-connections>@{maximum-new-connections}@</maximum-new-connections>
     <prototype-count>@{prototype-count}@</prototype-count>
     <maximum-connection-lifetime>@{maximum-connection-lifetime}@</maximum-connection-lifetime>
     <test-before-use>@{test-before-use}@</test-before-use>
     <test-after-use>@{test-after-use}@</test-after-use>
     <trace>@{trace}@</trace>
     </proxool>
     </something-else-entirely>

     * @param dsp
     * @return
     * @throws Exception
     */
    private static String getProxoolXml(DataSourceParam dsp) throws Exception {
        String TEMPLATE_PROXOOL = "com/latico/commons/db/datasource/proxool/proxool.tpl";
        Template  tpl = new Template (TEMPLATE_PROXOOL, CharsetType.ISO);
        tpl.set("alias", dsp.getId());
        tpl.set("driver-url", dsp.getUrl());
        tpl.set("driver-class", dsp.getDriverClassName());
        tpl.set("username", dsp.getUsername());
        tpl.set("password", dsp.getPassword());
        tpl.set("characterEncoding", dsp.getCharset());

//        单个连接的最大激活时间（即超时时间）
        tpl.set("maximum-active-time", String.valueOf(dsp.getMaxActiveTime()));

//        tpl.set("house-keeping-test-sql", ds.getHouseKeepingTestSql());
//        tpl.set("house-keeping-sleep-time", String.valueOf(ds.getHouseKeepingSleepTime()));

//        可同时建立的最大连接数.
//	 *  即可同时新增的连接请求, 但还没有可供使用的连接.
//        tpl.set("simultaneous-build-throttle", String.valueOf(ds.getSimultaneousBuildThrottle()));

//        可同时存在的最大连接数
        tpl.set("maximum-connection-count", String.valueOf(dsp.getMaxActive()));

        //(连接池)保有的最少连接数
        tpl.set("minimum-connection-count", String.valueOf(dsp.getMinIdle()));

        //当没有空闲连接可以分配时, 在队列中等候的最大请求数.
        //	 * 超过这个请求数的用户连接就不会被接受
        tpl.set("maximum-new-connections", String.valueOf(dsp.getMaxActive()));

        tpl.set("prototype-count", String.valueOf(dsp.getMinIdle()));

//        单个连接的最大使用寿命，默认跟最大激活时间一样
        tpl.set("maximum-connection-lifetime", String.valueOf(dsp.getMaxLifeTime()));

        //目前不配置是否检测连接是否被使用
//        tpl.set("test-before-use", String.valueOf(ds.isTestBeforeUse()));
//        tpl.set("test-after-use", String.valueOf(ds.isTestAfterUse()));
        tpl.set("trace", String.valueOf(false));
        return tpl.getContent();
    }

    /**
     * <PRE>
     * 设置是否自动关闭线程池.
     *
     * 	使用场景：
     *   通常当在进程销毁钩子中需要入库操作时, 若使用线程池, 则必定报错：
     * 		Attempt to refer to a unregistered pool by its alias xxx
     *
     *   这是因为proxool总是最先向JVM请求销毁自身, 导致在进程销毁钩子无法使用线程池, 只能使用常规的JDBC操作.
     *
     *   通过此方法, 在程序使用线程池之前设置 {@link #setAutoShutdownPool() false} 可以避免这种主动销毁的行为
     *   但是在进程钩子的最后, 需要手动调用 {@link #shutdownPool()} 方法关闭线程池
     * </PRE>
     * @param auto true:自动关闭线程池(默认); false:手动关闭线程池
     */
    public static void setAutoShutdownPool(boolean auto) {
        if(auto == true) {
            ProxoolFacade.enableShutdownHook();
        } else {
            ProxoolFacade.disableShutdownHook();
        }
    }

    /**
     * 马上关闭线程池
     */
    public static void shutdownPool() {
        shutdownPool(0);
    }

    /**
     * 延迟一段时间后关闭线程池
     * @param delay 延迟时间, 单位:ms
     */
    public static void shutdownPool(int delay) {
        ProxoolFacade.shutdown(delay < 0 ? 0 : delay);
    }
}

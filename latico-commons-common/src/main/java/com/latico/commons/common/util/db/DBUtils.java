package com.latico.commons.common.util.db;

import com.latico.commons.common.envm.DBTypeEnum;
import com.latico.commons.common.envm.TableType;
import com.latico.commons.common.util.collections.CollectionUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.ClassUtils;
import com.latico.commons.common.util.string.StringUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * <PRE>
 *  数据库操作通用工具
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:46:33
 * @Version: 1.0
 */
public class DBUtils {

    /**
     * LOG 日志工具
     */
    private static final Logger LOG = LoggerFactory.getLogger(DBUtils.class);

    /**
     * 注册数据库驱动
     *
     * @param driver 驱动类名
     * @return true:注册成功
     */
    public static boolean registerDriver(String driver) {
        if (driver != null && !"".equals(driver.trim())) {
            try {
                Class.forName(driver);
                return true;
            } catch (Throwable e) {
                LOG.error("注册驱动失败", e);
            }
        }

        return false;
    }

    /**
     * 注册Mysql数据库驱动
     *
     * @return
     */
    public static boolean registerDriverMySql() {
        return registerDriver(DBTypeEnum.MYSQL.DRIVER);
    }

    /**
     * 注册oralce数据库驱动
     *
     * @return
     */
    public static boolean registerDriverOracle() {
        return registerDriver(DBTypeEnum.ORACLE.DRIVER);
    }
    /**
     * 通过URL方式获取数据库连接
     *
     * @param url      数据库连接的URL，例如Mysql："jdbc:mysql://localhost:3306/databaseName?useUnicode=true&characterEncoding=utf-8";
     * @param user     用户名
     * @param password 密码
     * @return
     */
    public static Connection getConnection(String url, String user, String password) {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOG.error("获取数据库连接失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 通过URL方式获取数据库连接
     *
     * @param driver   数据库驱动类
     * @param url      数据库连接的URL，例如Mysql："jdbc:mysql://localhost:3306/databaseName?useUnicode=true&characterEncoding=utf-8";
     * @param user     用户名
     * @param password 密码
     * @return
     */
    public static Connection getConnection(String driver, String url, String user, String password) {
        try {
            if (!registerDriver(driver)) {
                return null;
            }
            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Mysql数据库连接
     * jdbc:mysql://localhost:3306/databaseName?useUnicode=true&socketTimeout=600000&characterEncoding=utf-8
     *
     * @param host              主机
     * @param port              端口
     * @param databaseName      数据库名称
     * @param user              用户名
     * @param password          密码
     * @param socketTimeout     socket的超时时间 单位毫秒，建议：3600000
     * @param characterEncoding 字符集，默认utf-8
     * @return
     */
    public static Connection getConnectionByMysql(String host, int port, String databaseName, String user, String password, long socketTimeout, String characterEncoding) {
        try {
            String url = DBTypeEnum.MYSQL.JDBCURL;
            if (characterEncoding == null || "".equals(characterEncoding)) {
                characterEncoding = "utf-8";
            }
            url = url.replace(DBTypeEnum.PH_HOST, host);
            url = url.replace(DBTypeEnum.PH_PORT, port + "");
            url = url.replace(DBTypeEnum.PH_DBNAME, databaseName);
            url = url.replace(DBTypeEnum.PH_TIMEOUT, socketTimeout + "");
            url = url.replace(DBTypeEnum.PH_CHARSET, characterEncoding);

            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Mysql数据库连接
     * jdbc:mysql://localhost:3306/databaseName?useUnicode=true&socketTimeout=600000&characterEncoding=utf-8
     * 默认3600000毫秒的超时时间
     * 默认utf-8字符集
     *
     * @param host         主机
     * @param port         端口
     * @param databaseName 数据库名称
     * @param user         用户名
     * @param password     密码
     * @return
     */
    public static Connection getConnectionByMysql(String host, int port, String databaseName, String user, String password) {
        try {
            String url = DBTypeEnum.MYSQL.JDBCURL;
            url = url.replace(DBTypeEnum.PH_HOST, host);
            url = url.replace(DBTypeEnum.PH_PORT, port + "");
            url = url.replace(DBTypeEnum.PH_DBNAME, databaseName);
            url = url.replace(DBTypeEnum.PH_TIMEOUT, "3600000");
            url = url.replace(DBTypeEnum.PH_CHARSET, "utf-8");

            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取Mysql数据库连接
     * jdbc:mysql://localhost:3306/databaseName?useUnicode=true&socketTimeout=600000&characterEncoding=utf-8
     *
     * @param host              主机
     * @param port              端口
     * @param databaseName      数据库名称
     * @param user              用户名
     * @param password          密码
     * @param socketTimeout     socket的超时时间 单位毫秒，建议：600000
     * @param characterEncoding 字符集，默认utf-8
     * @return
     */
    public static Connection getConnectionByMysql(String driver, String host, int port, String databaseName, String user, String password, long socketTimeout, String characterEncoding) {
        try {
            if (!registerDriver(driver)) {
                return null;
            }
            String url = DBTypeEnum.MYSQL.JDBCURL;
            if (characterEncoding == null || "".equals(characterEncoding)) {
                characterEncoding = "utf-8";
            }
            url = url.replace(DBTypeEnum.PH_HOST, host);
            url = url.replace(DBTypeEnum.PH_PORT, port + "");
            url = url.replace(DBTypeEnum.PH_DBNAME, databaseName);
            url = url.replace(DBTypeEnum.PH_TIMEOUT, socketTimeout + "");
            url = url.replace(DBTypeEnum.PH_CHARSET, characterEncoding);

            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败" + e.getMessage(), e);
        }
        return null;
    }


    /**
     * 获取Mysql数据库连接
     *
     * @param host         主机
     * @param port         端口
     * @param databaseName 数据库名称
     * @param user         用户名
     * @param password     密码
     * @return
     */
    public static Connection getConnectionByOracle(String host, int port, String databaseName, String user, String password) {
        try {
            String url = DBTypeEnum.ORACLE.JDBCURL;
            url = url.replace(DBTypeEnum.PH_HOST, host);
            url = url.replace(DBTypeEnum.PH_PORT, port + "");
            url = url.replace(DBTypeEnum.PH_DBNAME, databaseName);
            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败", e);
        }
        return null;
    }

    /**
     * 获取Mysql数据库连接
     *
     * @param host         主机
     * @param port         端口
     * @param databaseName 数据库名称
     * @param user         用户名
     * @param password     密码
     * @return
     */
    public static Connection getConnectionByOracle(String driver, String host, int port, String databaseName, String user, String password) {
        try {
            if (!registerDriver(driver)) {
                return null;
            }
            String url = DBTypeEnum.ORACLE.JDBCURL;
            url = url.replace(DBTypeEnum.PH_HOST, host);
            url = url.replace(DBTypeEnum.PH_PORT, port + "");
            url = url.replace(DBTypeEnum.PH_DBNAME, databaseName);
            return DriverManager.getConnection(url, user, password);
        } catch (Throwable e) {
            LOG.error("获取数据库连接失败", e);
        }
        return null;
    }

    /**
     * 通过数据源获取数据库连接
     *
     * @param dataSource
     * @return
     */
    public static Connection getConnection(DataSource dataSource) {
        try {
            if (dataSource != null) {
                return dataSource.getConnection();
            }

        } catch (SQLException e) {
            LOG.error("获取数据库连接失败", e);
        }
        return null;
    }

    /**
     * 通过数据源获取数据库连接
     *
     * @param dataSource 数据源
     * @param username   用户名
     * @param password   密码
     * @return
     */
    public static Connection getConnection(DataSource dataSource, String username, String password) {
        try {
            if (dataSource != null) {
                return dataSource.getConnection(username, password);
            }

        } catch (SQLException e) {
            LOG.error("获取数据库连接失败", e);
        }
        return null;
    }

    /**
     * <PRE>
     * 获取数据库连接(先通过连接池获取，若连接池获取失败，则改用JDBC获取).
     * 在连接成功前，重试若干次.
     * </PRE>
     *
     * @param ds       数据库配置信息
     * @param retry    重试次数
     * @param interval 重试的间隔时间，单位毫秒
     * @return 数据库连接(若连接失败返回null)
     */
    public static Connection getConnection(DataSource ds, int retry, long interval) {

        if (ds == null) {
            return null;
        }
        Connection conn = null;
        int cnt = 0;
        do {
            conn = getConnection(ds);
            if (conn != null) {
                break;
            }
            cnt++;
            ThreadUtils.sleep(interval);
        } while (retry < 0 || cnt < retry);
        return conn;
    }
    /**
     * 测试数据库连接是否可用
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static boolean testDbAvailable(String driver, String url, String username, String password) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            return true;

        } catch (Throwable e) {

        } finally {
            DBUtils.close(conn);
        }
        return false;
    }

    /**
     * 关闭数据库sql接口
     *
     * @param statement 数据库sql接口
     * @return true:关闭成功; false:关闭失败
     */
    public static boolean close(Statement statement) {
        boolean isOk = true;
        if (statement != null) {
            try {
                statement.close();
            } catch (Throwable e) {
                LOG.error("IO流关闭失败.", e);
                isOk = false;
            }
        }
        return isOk;
    }

    /**
     * 关闭数据库结果集接口
     *
     * @param resultSet 数据库结果集接口
     * @return true:关闭成功; false:关闭失败
     */
    public static boolean close(ResultSet resultSet) {
        boolean isOk = true;
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Throwable e) {
                LOG.error("IO流关闭失败.", e);
                isOk = false;
            }
        }
        return isOk;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn 数据库连接
     */
    public static boolean close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.error("关闭数据库连接失败.", e);
                return false;
            }
        }
        return true;
    }

    /**
     * 关闭数据库相关IO资源
     *
     * @param conn      数据库连接
     * @param statement 数据库连接声明
     * @param resultSet 数据库连接结果集
     */
    public static boolean close(Connection conn, Statement statement, ResultSet resultSet) {
        boolean bool = close(resultSet);
        bool &= close(statement);
        bool &= close(conn);
        return bool;
    }

    /**
     * 执行预编译sql
     *
     * @param conn
     * @param preSql
     * @param params
     * @return
     */
    public static boolean execute(Connection conn, String preSql, Collection<Object> params) {
        return execute(conn, preSql, params.toArray());
    }

    /**
     * 执行预编译sql
     *
     * @param conn
     * @param preSql
     * @param params
     * @return
     */
    public static int executeUpdate(Connection conn, String preSql, Collection<Object> params) {
        return executeUpdate(conn, preSql, params.toArray());
    }

    /**
     * 执行预编译sql
     *
     * @param conn   数据库连接
     * @param preSql 预编译sql
     * @param params 参数表
     * @return true:执行成功; false:执行失败
     */
    public static boolean execute(Connection conn, String preSql, Object[] params) {
        boolean rst = false;
        PreparedStatement ps = null;

        String preparedSQL = null;
        try {

            preparedSQL = getPreparedSQL(preSql, params);

            ps = conn.prepareStatement(preSql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Object value = params[i];
                    setValueToPrepareStatement(ps, i + 1, value);
                }
            }

            LOG.info("执行更新SQL:{}", preparedSQL);
            ps.execute();
            rst = true;

        } catch (Exception e) {
            LOG.error("执行sql失败: [{}].", e, preparedSQL);
        } finally {
            close(ps);
        }
        return rst;
    }



    /**
     * 执行预编译sql
     *
     * @param conn   数据库连接
     * @param preSql 预编译sql
     * @param params 参数表
     * @return true:执行成功; false:执行失败
     */
    public static int executeUpdate(Connection conn, String preSql, Object[] params) {
        PreparedStatement ps = null;
        String preparedSQL = null;
        try {
            ps = conn.prepareStatement(preSql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    Object value = params[i];
                    setValueToPrepareStatement(ps, i + 1, value);
                }
            }
            preparedSQL = getPreparedSQL(preSql, params);
            LOG.info("执行更新SQL:{}", preparedSQL);

            return ps.executeUpdate();

        } catch (Exception e) {
            LOG.error("执行sql失败: [{}].", e, preparedSQL);
        } finally {
            close(ps);
        }
        return -1;
    }

    /**
     * 执行预编译sql
     *
     * @param conn   数据库连接
     * @param preSql 预编译sql
     * @param paramsList 参数表,批量
     * @return true:执行成功; false:执行失败
     */
    public static int[] executeUpdateBatch(Connection conn, String preSql, List<Object[]> paramsList) {
        if (paramsList == null) {
            return new int[0];
        }
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(preSql);

            for (Object[] params : paramsList) {
                if (params == null) {
                    continue;
                }

                for (int i = 0; i < params.length; i++) {
                    Object value = params[i];
                    setValueToPrepareStatement(ps, i + 1, value);
                }
                ps.addBatch();

                String preparedSQL = getPreparedSQL(preSql, params);
                LOG.info("添加批量更新SQL:{}", preparedSQL);
            }
            LOG.info("执行批量更新SQL:{}", preSql);
            return ps.executeBatch();

        } catch (Exception e) {
            LOG.error("执行sql失败: [{}].", e, preSql);
        } finally {
            close(ps);
        }
        return new int[0];
    }
    /**
     * 执行更新操作
     *
     * @param conn
     * @param sql  插入或者更新SQL
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(Connection conn, String sql) throws SQLException {
        if (conn == null || StringUtils.isEmpty(sql)) {
            return 0;
        }
        int num = -1;
        Statement stm = null;

        try {
            stm = conn.createStatement();
            LOG.info("执行更新SQL:{}", sql);
            num = stm.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(stm);
        }
        return num;
    }
    /**
     * 执行更新操作
     *
     * @param conn
     * @param sqls  插入或者更新SQL
     * @return
     * @throws SQLException
     */
    public static int[] executeUpdateBatch(Connection conn, List<String> sqls) throws Exception {
        if (conn == null || sqls == null) {
            return new int[0];
        }

        Statement stm = null;
        try {
            stm = conn.createStatement();
            for (String sql : sqls) {
                stm.addBatch(sql);
            }
            LOG.info("执行批量更新SQL:{}", sqls);
            return stm.executeBatch();
        } catch (Exception e) {
            throw e;
        } finally {
            close(stm);
        }
    }



    /**
     * 查询对象的时候，把OLB类型原生，不转换成字符串
     *
     * @param clazz
     * @param fieldNameMap
     * @param conn
     * @param sql
     * @param charset
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> queryObject(Class<T> clazz, Map<String, Field> fieldNameMap, Connection conn, String sql, String charset, boolean lobToStr) throws Exception {
        List<T> results = null;
        Statement stmt = null;
        ResultSet rs = null;
        Map<String, Field> fieldNameMapNew = new LinkedHashMap<>();
        for (Map.Entry<String, Field> entry : fieldNameMap.entrySet()) {
            fieldNameMapNew.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        try {
            stmt = conn.createStatement();
            LOG.debug("执行查询SQL:{}", sql);
            stmt.execute(sql);
            rs = stmt.getResultSet();
            results = getObjectByResultSet(clazz, rs, fieldNameMapNew, charset, lobToStr);
        } catch (Exception e) {
            throw e;
        } finally {
            close(null, stmt, rs);
        }
        return results;
    }
    /**
     * 获取Oracle指定序列的下一个值
     *
     * @param conn    数据库连接
     * @param seqName 序列名称
     * @return
     */
    public static long queryOracleSequenceNext(Connection conn, String seqName)
            throws SQLException {
        String sql = new StringBuilder().append("select ").append(seqName).append(".nextval as id from dual").toString();

        return queryLong(conn, sql);
    }

    /**
     * 从数据库查询一个整数值.
     * 若返回的不是 1x1 的结果集，只取 [1][1] 作为返回值.
     *
     * @param sql 查询sql
     * @return 查询失败则返回-1
     * @throws SQLException
     */
    public static long queryLong(Connection conn, String sql)
            throws SQLException {
        long num = -1;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                num = rs.getLong(1);
            }
        } finally {
            close(null, pstm, rs);
        }
        return num;
    }

    /**
     * 从数据库查询一个整数值.
     * 若返回的不是 1x1 的结果集，只取 [1][1] 作为返回值.
     *
     * @param sql 查询sql
     * @return 查询失败则返回-1
     * @throws SQLException
     */
    public static long queryInt(Connection conn, String sql)
            throws SQLException {
        long num = -1;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                num = rs.getInt(1);
            }
        } finally {
            close(null, pstm, rs);
        }
        return num;
    }

    /**
     * 从数据库查询一个整数值.
     * 若返回的不是 1x1 的结果集，只取 [1][1] 作为返回值.
     *
     * @param sql 查询sql
     * @return 查询失败则返回-1
     * @throws SQLException
     */
    public static double queryDouble(Connection conn, String sql) throws SQLException {
        double num = -1;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                num = rs.getDouble(1);
            }
        } finally {
            close(null, pstm, rs);
        }
        return num;
    }



    /**
     * 执行SQL操作
     *
     * @param conn
     * @param sql  插入或者更新SQL
     * @return
     * @throws SQLException
     */
    public static boolean execute(Connection conn, String sql) throws SQLException {
        if (conn == null || StringUtils.isEmpty(sql)) {
            return false;
        }
        Statement stm = null;

        try {
            stm = conn.createStatement();
            LOG.info("执行SQL:{}", sql);
            return stm.execute(sql);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(stm);
        }
    }



    /**
     * <PRE>
     * 查询键值对(其中值会被强制转换为String类型).
     * 仅适用于形如 【select key, value from table where ...】 的sql
     * SQL查询出2列，第一列是key，第二列是value
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  键值对查询SQL
     * @return Map<key   ,       value> 键值对表 （不会返回null）,顺序跟SQL一样
     */
    public static Map<String, String> queryKeyValueStr(Connection conn, String sql) {
        Map<String, String> kvo = new LinkedHashMap<String, String>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            ResultSetMetaData rsmd = null;
            while (rs.next()) {
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                if (count < 2) {
                    break;
                }

                kvo.put(rs.getString(1), rs.getString(2));
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return kvo;
    }

    /**
     * <PRE>
     * 查询键值对(其中值会保留其原本数据类型).
     * 仅适用于形如 【select key, value from table where ...】 的sql
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  键值对查询SQL
     * @return Map<key   ,       value> 键值对表 （不会返回null）,顺序跟SQL一样
     */
    public static Map<String, Object> queryKeyValueObj(Connection conn, String sql) {
        Map<String, Object> kvo = new LinkedHashMap<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            ResultSetMetaData rsmd = null;
            while (rs.next()) {
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                if (count < 2) {
                    break;
                }

                kvo.put(rs.getString(1), rs.getObject(2));
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return kvo;
    }

    /**
     * <PRE>
     * 查询多行表数据.
     * 每行数据以列名为key，以列值为val（列值会被强制转换成String类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return List<Map   <   colName   ,       colVal>> (不会返回null)
     */
    public static List<Map<String, String>> queryKeyValueStrs(Connection conn, String sql) {
        List<Map<String, String>> kvsList = new LinkedList<Map<String, String>>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            ResultSetMetaData rsmd = null;
            while (rs.next()) {
                Map<String, String> kvs = new LinkedHashMap<String, String>();
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    kvs.put(rsmd.getColumnLabel(i), rs.getString(i));
                }
                kvsList.add(kvs);
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return kvsList;
    }

    /**
     * <PRE>
     * 查询多行表数据.
     * 每行数据以列名为key，以列值为val（列值会保留其原本数据类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return List<Map   <   colName   ,       colVal>> (不会返回null)
     */
    public static List<Map<String, Object>> queryKeyValueObjs(Connection conn, String sql) {
        List<Map<String, Object>> kvsList = new LinkedList<Map<String, Object>>();

        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            ResultSetMetaData rsmd = null;
            while (rs.next()) {
                Map<String, Object> kvs = new LinkedHashMap<>();
                rsmd = rs.getMetaData();
                int count = rsmd.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    kvs.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                kvsList.add(kvs);
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return kvsList;
    }

    /**
     * <PRE>
     * 查询[第一行][第一列]的单元格值（所得值强制转换为String类型）.
     * 若返回的不是 1x1 的结果集，只取 [1][1] 作为返回值.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return 查询结果（查询失败则返回null）
     */
    public static String queryCellStr(Connection conn, String sql) {
        String cell = "";
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                if (rsmd.getColumnCount() > 0) {
                    cell = rs.getString(1);
                }
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return cell;
    }

    /**
     * <PRE>
     * 查询[第一行][第一列]的单元格值（所得值保留其原本的数据类型）.
     * 若返回的不是 1x1 的结果集，只取 [1][1] 作为返回值.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return 查询结果（查询失败则返回null）
     */
    public static Object queryCellObj(Connection conn, String sql) {
        Object cell = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LOG.debug("执行查询SQL:{}", sql);
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                if (rsmd.getColumnCount() > 0) {
                    cell = rs.getObject(1);
                }
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return cell;
    }

    /**
     * <PRE>
     * 查询[第一行]表数据.
     * 行数据以列名为key，以列值为val（列值会被强制转换成String类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return Map<colName   ,       colVal> (不会返回null)
     */
    public static Map<String, String> queryFirstRowStr(Connection conn, String sql) {
        Map<String, String> row = new LinkedHashMap<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int count = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= count; i++) {
                    row.put(rsmd.getColumnLabel(i), rs.getString(i));
                }
            }

            rs.close();
            pstm.close();

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return row;
    }

    /**
     * <PRE>
     * 查询[第一行]表数据.
     * 行数据以列名为key，以列值为val（列值保留其原本的数据类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return Map<colName   ,       colVal> (不会返回null)
     */
    public static Map<String, Object> queryFirstRowObj(Connection conn, String sql) {
        Map<String, Object> row = new LinkedHashMap<String, Object>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int count = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= count; i++) {
                    row.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
            }

        } catch (Throwable e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return row;
    }

    /**
     * <PRE>
     * 查询[第col列]表数据（数据值会被强制转换成String类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return List<colVal> (不会返回null)
     */
    public static List<String> queryColumnStr(Connection conn, String sql, int col) {
        List<String> vals = new LinkedList<String>();
        col = (col <= 0 ? 1 : col);

        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            int count = rs.getMetaData().getColumnCount();
            col = (col >= count ? count : col);
            while (rs.next()) {
                vals.add(rs.getString(col));
            }

        } catch (Exception e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return vals;
    }

    /**
     * <PRE>
     * 查询[第col列]表数据（数据值保留其原本的数据类型）.
     * </PRE>
     *
     * @param conn 数据库连接
     * @param sql  查询sql
     * @return List<colVal> (不会返回null)
     */
    public static List<Object> queryColumnObj(Connection conn, String sql, int col) {
        List<Object> vals = new LinkedList<Object>();
        col = (col <= 0 ? 1 : col);

        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            LOG.debug("执行查询SQL:{}", sql);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            int count = rs.getMetaData().getColumnCount();
            col = (col >= count ? count : col);
            while (rs.next()) {
                vals.add(rs.getObject(col));
            }

        } catch (Exception e) {
            LOG.error("执行sql失败: [{}].", sql, e);
        } finally {
            close(rs);
            close(pstm);
        }
        return vals;
    }

    /**
     * 执行存储过程，带入参和出参
     * <p>
     * DBUtils.registerDriverMySql();
     * Connection conn = DBUtils.getConnectionByMysql("localhost", 3306, "test", "root", "root", 600000, "utf-8");
     * <p>
     * Object[] inParam = new Object[]{1};
     * int[] outParamType = new int[]{Types.VARCHAR};
     * try {
     * Object[] getnamebyids = DBUtils.execProcedure(conn, "getnamebyid", inParam, outParamType);
     * System.out.println(Arrays.toString(getnamebyids));
     * } catch (Throwable e) {
     * e.printStackTrace();
     * }finally {
     * DBUtils.close(conn);
     * }
     *
     * @param procName     存储过程名称
     * @param inParam      入参数组
     * @param outParamType 出参数组类型，例如：java.sql.Types.VARCHAR、java.sql.Types.INTEGER
     * @return 指定出参获取的结果
     */
    public static Object[] execProcedure(Connection conn, String procName, Object[] inParam, int[] outParamType) throws Exception {
        if (procName == null || procName.length() == 0) {
            LOG.warn("存储过程名称为空！");
            return null;
        }

        if (inParam == null) {
            inParam = new Object[0];
        }

        if (outParamType == null) {
            outParamType = new int[0];
        }

        //存储过程调用器
        CallableStatement cs = null;

        //参数组装
        int params = inParam.length + outParamType.length;
        StringBuilder paramString = new StringBuilder();

        for (int i = 0; i < params; i++) {
            paramString.append(",?");
        }

        String proc = null;
        //截取前面逗号，存储过程语句组装格式：{call procName(?,?)}
        if (StringUtils.isBlank(paramString)) {
            proc = "{ call " + procName + "() }";
        } else {
            proc = "{ call " + procName + "(" + paramString.substring(1) + ") }";
        }

        String info = "名称:" + procName + ", 指定入参:" + Arrays.toString(inParam) + ", 指定出参类型:" + Arrays.toString(outParamType);
        try {
            //预编译
            cs = conn.prepareCall(proc);

            //入参处理
            for (int i = 0; i < inParam.length; i++) {
                cs.setObject(i + 1, (inParam[i]));
            }

            //出参处理
            for (int i = 0; i < outParamType.length; i++) {
                cs.registerOutParameter(inParam.length + i + 1, outParamType[i]);
            }

            LOG.info("开始调用存储过程, " + info);
            //存储过程调用
            cs.execute();
            LOG.info("调用存储过程完成, " + info);

            //结果处理
            Object[] results = new Object[outParamType.length];
            for (int i = 0; i < outParamType.length; i++) {
                results[i] = cs.getObject(inParam.length + i + 1);
            }

            return results;

        } catch (Throwable e) {
            throw new Exception("执行存储过程发生异常:" + info + ", " + e.getMessage(), e);
        } finally {
            close(cs);
        }
    }
    /**
     * 插入数据到Oracle的Blob字段，SQL格式必须是：select BLOB字段名称 from 表名
     * @param conn  数据库连接
     * @param selectBlobFieldSql SQL格式必须是：select BLOB字段名称 from 表名
     * @param bytes 数据
     * @return
     */
    public static boolean insertOneBlobFieldOracle(Connection conn, String selectBlobFieldSql, byte[] bytes) {
        boolean flag;
        ResultSet  rs = null;
        PreparedStatement ps = null;
        InputStream in = null;
        OutputStream out = null;
        //处理发现过程字段
        try {

            conn.setAutoCommit(false);

//            拼接BLOB关键词到SQL
            selectBlobFieldSql = selectBlobFieldSql + " FOR UPDATE";

            LOG.info("ORACLE BLOB字段操作,获取BLOB字段游标:[{}]", selectBlobFieldSql);
            ps = conn.prepareStatement(selectBlobFieldSql);
            rs = ps.executeQuery();
            BLOB blob = null;
            if(rs.next()) {
                blob = (BLOB) rs.getBlob(1);
            }
            in = new ByteArrayInputStream(bytes);
            out = blob.getBinaryOutputStream(1L);
            byte[] buffer = new byte[1024];

            int length = -1;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer, 0, length);
            }
//			  out.write(bytes, 0, bytes.length);
            out.flush();
            out.close();
            conn.commit();

            flag = true;
            LOG.info("ORACLE BLOB字段操作:[{}] 写BLOB字段完成, 操作字段:{}", selectBlobFieldSql, rs.getMetaData().getColumnName(1));
        } catch (Exception e) {
            LOG.error(selectBlobFieldSql, e);
            flag = false;
        }finally{
            IOUtils.close(in);
            IOUtils.close(out);

            if(conn != null){
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                }
            }

            DBUtils.close(rs);
            DBUtils.close(ps);
        }
        return flag;
    }

    /**
     * 插入数据到Oracle的Clob字段
     * @param conn  数据库连接
     * @param selectClobFieldSql SQL格式必须是：select CLOB字段名称 from 表名
     * @param data 数据
     * @return
     */
    public static boolean insertOneClobFieldOracle(Connection conn, String selectClobFieldSql, String data) {
        boolean flag;
        ResultSet  rs = null;
        PreparedStatement ps = null;
        Writer out = null;
        //处理发现过程字段
        try {

            conn.setAutoCommit(false);
            //            拼接BLOB关键词到SQL
            selectClobFieldSql = selectClobFieldSql + " FOR UPDATE";

            LOG.info("ORACLE CLOB字段操作,获取CLOB字段游标:[{}]", selectClobFieldSql);
            ps = conn.prepareStatement(selectClobFieldSql);
            rs = ps.executeQuery();
            CLOB clob = null;
            if(rs.next()) {
                clob = (CLOB) rs.getClob(1);
            }
            out = clob.getCharacterOutputStream(1L);
            out.write(data);
            out.flush();
            out.close();
            conn.commit();

            flag = true;
            LOG.info("ORACLE CLOB字段操作:[{}] 写CLOB字段完成, 操作字段:{}", selectClobFieldSql, rs.getMetaData().getColumnName(1));
        } catch (Exception e) {
            LOG.error(selectClobFieldSql, e);
            flag = false;
        }finally{
            IOUtils.close(out);
            if(conn != null){
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                }
            }
            DBUtils.close(rs);
            DBUtils.close(ps);
        }
        return flag;
    }
    /**
     * 通过Connection判断数据库类型
     *
     * @param conn 数据库连接
     * @return 数据库类型
     */
    public static DBTypeEnum judgeDBType(Connection conn) {
        DBTypeEnum db = DBTypeEnum.UNKNOW;
        if (conn == null) {
            return db;
        }

        try {
            String driver = conn.getMetaData().getDatabaseProductName();

            if (driver.toUpperCase().contains("MYSQL")) {
                db = DBTypeEnum.MYSQL;

            } else if (driver.toUpperCase().contains("ORACLE")) {
                db = DBTypeEnum.ORACLE;

            } else if (driver.toUpperCase().contains("ADAPTIVE SERVER ENTERPRISE")) {
                db = DBTypeEnum.SYBASE;

            } else if (driver.toUpperCase().contains("SQLITE")) {
                db = DBTypeEnum.SQLITE;

            } else if (driver.toUpperCase().contains("SQLSERVER")) {
                db = DBTypeEnum.MSSQL;

            } else if (driver.toUpperCase().contains("POSTGRESQL")) {
                db = DBTypeEnum.POSTGRESQL;

            } else if (driver.toUpperCase().contains("RMIJDBC")
                    || driver.toUpperCase().contains("OBJECTWEB")) {
                db = DBTypeEnum.ACCESS;

            } else if (driver.toUpperCase().contains("DB2")) {
                db = DBTypeEnum.IBM;

            }
        } catch (SQLException e) {
            LOG.error("判断数据库类型失败", e);
        }
        return db;
    }

    /**
     * 开/关 数据库自动提交
     *
     * @param conn       数据库连接
     * @param autoCommit 是否自动提交
     */
    public static void setAutoCommit(Connection conn, boolean autoCommit) {
        if (conn != null) {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                LOG.error("开/关数据库自动提交失败.", e);
            }
        }
    }
    /**
     * 生成 Insert的Prepared SQL语句
     *
     * @param tableName   表名
     * @param columnNames 列名
     * @return
     */
    public static String makeInsertPreparedSql(String tableName, Collection<String> columnNames) {

        StringBuilder fieldNames = new StringBuilder(" (");
        StringBuilder fieldValues = new StringBuilder(" values(");

        for (String columnName : columnNames) {
            fieldNames.append(columnName).append(", ");
            fieldValues.append("?, ");
        }

        StringBuilder sql = new StringBuilder("insert into ").append(tableName);
        sql.append(fieldNames.substring(0, fieldNames.length() - 2)).append(")").append(fieldValues.substring(0, fieldValues.length() - 2)).append(")");
        return sql.toString();
    }

    /**
     * 组装查询SQL，获取类对象的查询SQL
     *
     * @param tableName   表名
     * @param columnNames 列名
     * @return
     */
    public static <T> String makeSelectSql(String tableName, List<String> columnNames) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (String columnName : columnNames) {
            sql.append(columnName).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" from ").append(tableName);
        return sql.toString();
    }

    /**
     * 组装查询SQL，获取类对象的查询SQL
     *
     * @param tableName      表名
     * @param columnAliasMap 列名和别名的Map
     * @return
     */
    public static <T> String makeSelectSql(String tableName, Map<String, String> columnAliasMap) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (Map.Entry<String, String> columnName : columnAliasMap.entrySet()) {
            sql.append(columnName.getKey()).append(" ").append(columnName.getValue()).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length());
        sql.append(" from ").append(tableName);
        return sql.toString();
    }

    /**
     * 根据类对象获取删除SQL
     *
     * @param tableName
     * @return
     */
    public static String makeDeleteSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tableName);
        return sql.toString();
    }


    /**
     * 获取更新的Prepared类型SQL
     *
     * @param tableName
     * @param columns
     * @return
     */
    public static String makeUpdatePreparedSql(String tableName, Collection<String> columns) {

        StringBuilder sql = new StringBuilder("update ").append(tableName)
                .append(" set ");
        for (String column : columns) {
            sql.append(column).append(" = ?, ");
        }
        sql.deleteCharAt(sql.length() - 2);
        return sql.substring(0, sql.length() - 1);
    }




    private static <T> List<T> getObjectByResultSet(Class<T> clazz, ResultSet rs, Map<String, Field> fieldNameMap, String charset, boolean lobToStr) throws Exception {
        List<T> results = new ArrayList<T>();

        String dbFieldName = null;
        int colCount = 0;
        ResultSetMetaData meta = null;
        T result = null;
        while (rs.next()) {
            result = clazz.newInstance();
            meta = rs.getMetaData();
            colCount = meta.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                dbFieldName = meta.getColumnLabel(i);

                if (rs.getObject(i) == null) {
                    continue;
                }
                Field field = fieldNameMap.get(dbFieldName.toLowerCase());
                if (field != null) {
                    setFieldByDbType(rs, result, field, Class.forName(meta.getColumnClassName(i)), i, lobToStr, charset);
                }
            }

            results.add(result);
        }
        return results;

    }

    public static <T> void setFieldByDbType(ResultSet rs, T result, Field field,
                                            Class<?> dbFieldClass, int colIndex, boolean lobToStr, String charset) throws Exception {
        field.setAccessible(true);
        if (ClassUtils.isStringClass(dbFieldClass)) {
            field.set(result, rs.getString(colIndex));

        } else if (ClassUtils.isIntegerClass(dbFieldClass)) {
            field.set(result, Integer.valueOf(rs.getInt(colIndex)));

        } else if (ClassUtils.isSqlDateClass(dbFieldClass)) {
            field.set(result, rs.getDate(colIndex));

        } else if (ClassUtils.isLongClass(dbFieldClass)) {
            field.set(result, Long.valueOf(rs.getLong(colIndex)));

        } else if (ClassUtils.isBooleanClass(dbFieldClass)) {
            field.set(result, Boolean.valueOf(rs.getBoolean(colIndex)));

        } else if (ClassUtils.isDoubleClass(dbFieldClass)) {
            field.set(result, Double.valueOf(rs.getDouble(colIndex)));

        } else if (ClassUtils.isByteClass(dbFieldClass)) {
            field.set(result, Byte.valueOf(rs.getByte(colIndex)));

        } else if (ClassUtils.isFloatClass(dbFieldClass)) {
            field.set(result, Float.valueOf(rs.getFloat(colIndex)));

        } else if (ClassUtils.isShortClass(dbFieldClass)) {
            field.set(result, Short.valueOf(rs.getShort(colIndex)));

        } else if (ClassUtils.isBigDecimalClass(dbFieldClass)) {
            field.set(result, rs.getLong(colIndex));

        } else if (ClassUtils.isTimestampClass(dbFieldClass)) {
            field.set(result, rs.getTimestamp(colIndex));

        } else if (ClassUtils.isSqlTimeClass(dbFieldClass)) {
            field.set(result, rs.getTime(colIndex));

        } else if (ClassUtils.isClobClass(dbFieldClass)) {
            if (lobToStr) {
                Clob clog = rs.getClob(colIndex);
                field.set(result, getClobToStr(clog));
            } else {
                field.set(result, rs.getClob(colIndex));
            }

        } else if (ClassUtils.isBlobClass(dbFieldClass)) {
            if (lobToStr) {
                field.set(result, getBlobToStr(rs.getBlob(colIndex), charset));

            } else {
                field.set(result, rs.getBlob(colIndex));
            }

        } else {
            field.set(result, rs.getObject(colIndex));
        }
    }

    /**
     * 根据数据库的流（数据库相关连接资源不能关闭）把CLOB转换成STR
     *
     * @param clog
     * @return
     * @throws SQLException
     */
    public static String getClobToStr(Clob clog) throws SQLException {
        return clog.getSubString(1, (int) clog.length());
    }

    /**
     * 根据数据库的流（数据库相关连接资源不能关闭）把BLOB转换成STR
     *
     * @param blob
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String getBlobToStr(Blob blob, String charset)
            throws IOException, SQLException {

        InputStream in = blob.getBinaryStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, data.length)) != -1) {
            outStream.write(data, 0, count);
        }

        data = null;
        // 此处转换可能需要注意中文乱码问题
        // 指定数据库用了什么字符集 XXX此处转换可能需要注意中文乱码问题
        String result = new String(outStream.toByteArray(), charset);
        return result;

    }

    /**
     * 获得所有表名
     *
     * @param conn 数据源
     * @return 表名列表
     */
    public static List<String> getTables(Connection conn) throws SQLException {
        return getTables(conn, TableType.TABLE);
    }

    /**
     * 获得所有表名
     *
     * @param conn 数据库连接
     * @param types 表类型
     * @return 表名列表
     */
    public static List<String> getTables(Connection conn, TableType... types) throws SQLException {
        return getTables(conn, null, null, types);
    }

    /**
     * 获得所有表名
     *
     * @param conn 数据库连接
     * @param schema 表数据库名，对于Oracle为用户名
     * @param types 表类型
     * @return 表名列表
     */
    public static List<String> getTables(Connection conn, String schema, TableType... types) throws SQLException {
        return getTables(conn, schema, null, types);
    }

    /**
     * 获得所有表名
     *
     * @param conn 数据库连接
     * @param schema 表数据库名，对于Oracle为用户名
     * @param tableNameRegex 表名正则
     * @param types 表类型
     * @return 表名列表
     */
    public static List<String> getTables(Connection conn, String schema, String tableNameRegex, TableType... types) throws SQLException {
        final List<String> tables = new ArrayList<String>();
        ResultSet rs = null;
        String[] tableTypes = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            tableTypes[i] = types[i].value();
        }
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getTables(conn.getCatalog(), schema, tableNameRegex, tableTypes);
            if (rs == null) {
                return null;
            }
            String table;
            while (rs.next()) {
                table = rs.getString("TABLE_NAME");
                if (StringUtils.isNotBlank(table)) {
                    tables.add(table);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close(rs);
        }
        return tables;
    }

    /**
     * 获得结果集的所有列名
     *
     * @param rs 结果集
     * @return 列名数组
     * @throws SQLException SQL执行异常
     */
    public static String[] getColumnNames(ResultSet rs) throws SQLException {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            String[] labelNames = new String[columnCount];
            for (int i = 0; i < labelNames.length; i++) {
                labelNames[i] = rsmd.getColumnLabel(i + 1);
            }
            return labelNames;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * 获得表的所有列名
     *
     * @param conn 数据源
     * @param tableName 表名
     * @return 列数组
     * @throws SQLException SQL执行异常
     */
    public static String[] getColumnNames(Connection conn, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<String>();
        ResultSet rs = null;
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
            return columnNames.toArray(new String[columnNames.size()]);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }
    }


    /**
     * 获得表的元信息
     *
     * @param conn 数据源
     * @param tableName 表名
     * @return Table对象
     */
    public static List<String> getTablePrimaryKeysColumnName(Connection conn, String tableName) throws SQLException {
        List<String> primaryKeys = new ArrayList<>();
        ResultSet rs = null;
        try {
            final DatabaseMetaData metaData = conn.getMetaData();
            // 获得主键
            rs = metaData.getPrimaryKeys(conn.getCatalog(), null, tableName);
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }

        return primaryKeys;
    }


    /**
     * 获得表的元信息
     *
     * @param conn 数据源
     * @param tableName 表名
     * @return Table对象
     */
    public static List<String> getTableColumnNames(Connection conn, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        ResultSet rs = null;
        try {
            final DatabaseMetaData metaData = conn.getMetaData();

            // 获得列
            rs = metaData.getColumns(conn.getCatalog(), null, tableName, null);
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            close(rs);
        }

        return columnNames;
    }



    /**
     * 获得PreparedStatement向数据库提交的SQL语句
     * @param sql
     * @param params
     * @return
     */
    public static String getPreparedSQL(String sql, Object[] params) {
        // 1 如果没有参数，说明是不是动态SQL语句
        int paramNum = 0;
        if (null != params){
            paramNum = params.length;
        }
        if (1 > paramNum){
            return sql;
        }
        // 2 如果有参数，则是动态SQL语句
        StringBuilder returnSQL = new StringBuilder();
        String[] subSQL = sql.split("\\?");

        for (int i = 0; i < paramNum; i++) {
            if(params[i] == null || "".equals(params[i]) || "null".equalsIgnoreCase(params[i].toString())){
                returnSQL.append(subSQL[i]).append("null");
            }else{
                returnSQL.append(subSQL[i]).append("'").append(params[i]).append("'");
            }
        }

        if (subSQL.length > params.length) {
            returnSQL.append(subSQL[subSQL.length - 1]);
        }
        return returnSQL.toString();
    }

    /**
     *
     * @param sql
     * @param params
     * @return
     */
    public static String getPreparedSQL(String sql, List<Object> params) {
        return getPreparedSQL(sql, CollectionUtils.toArrayObj(params));
    }
    /**
     * 把值添加到PrepareStatement
     * @param ps PrepareStatement
     * @param parameterIndex 参数的索引
     * @param value 值
     * @throws SQLException
     */
    private static void setValueToPrepareStatement(PreparedStatement ps, int parameterIndex, Object value) throws SQLException {
        if (value == null) {
            ps.setString(parameterIndex, null);
        } else {
            if (value instanceof Timestamp) {
                ps.setObject(parameterIndex, value);

            } else if (value instanceof java.sql.Date) {
                //java.sql.Date显示的时间有问题，需要转换成Timestamp
                java.sql.Date date = (java.sql.Date) value;
                ps.setTimestamp(parameterIndex, new Timestamp(date.getTime()));

            } else if (value instanceof java.util.Date) {
                //java.util.Date不是sql的时间类型，需要转换成Timestamp
                java.util.Date date = (java.util.Date) value;
                ps.setTimestamp(parameterIndex, new Timestamp(date.getTime()));

            } else {
                ps.setObject(parameterIndex, value);
            }
        }
    }

    /**
     * 根据数据库的流（数据库相关连接资源不能关闭）把CLOB转换成STR
     *
     * @param clob
     * @return
     * @throws SQLException
     */
    public static String convertClobToStr(Clob clob) throws SQLException, IOException {
        if (clob == null) {
            return null;
        }
        return clob.getSubString(1, (int) clob.length());
    }

    /**
     * 指定字符集转换CLOB字段到字符串
     *
     * @param clob
     * @param charset 字符集
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static String convertClobToStr(Clob clob, String charset) throws SQLException, IOException {
        if (clob == null) {
            return null;
        }
        return IOUtils.toString(clob.getAsciiStream(), charset);
    }

    /**
     * 根据数据库的流（数据库相关连接资源不能关闭）把BLOB转换成STR
     *
     * @param blob
     * @param charset 字符集
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public static String convertBlobToStr(Blob blob, String charset)
            throws IOException, SQLException {

        InputStream in = blob.getBinaryStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] data = new byte[1024];
        int count = -1;
        while ((count = in.read(data, 0, data.length)) != -1) {
            outStream.write(data, 0, count);
        }
        return new String(outStream.toByteArray(), charset);
    }

    /**
     * 字符串转Clob
     *
     * @param str 字符串
     * @return
     */
    public static Clob convertStrToClob(String str) throws SQLException {
        if (null == str) {
            return null;
        }
        return new javax.sql.rowset.serial.SerialClob(str.toCharArray());
    }

}

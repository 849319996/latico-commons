package com.latico.commons.common.util.db;

import com.latico.commons.common.envm.DBTypeEnum;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 *  存储过程工具
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-27 11:46:20
 * @Version: 1.0
 */
public class StoredProcedureUtils {
    private static final Logger LOG = LoggerFactory.getLogger(StoredProcedureUtils.class);
    /**
     * <pre>
     * 执行存储过程，获得简单返回值（支持[无返回值]和 [单值]返回两种形式）。
     * 根据数据库连接自动识别 mysql、sybase、oracle。
     *
     * 注意：
     * 参数如果有null，则可能出错，特别是sybase数据库
     *
     * mysql存储过程要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：最后一个结果集（即SELECT语句）的第1行、第1列的值。
     *
     * sybase存储过程要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：return所指定的值。
     *
     * oracle存储过程要求：
     *  入参表：当proSql的占位符?个数 比 入参表params长度多0，为无返回值形式；
     *       多1，为有返回值形式。其余情况抛出SQLException异常。
     * 	返回值：当proSql的占位符?个数比入参表params多1，则认为最后1个占位符是出参。
     * </pre>
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return
     * 		对于返回单值的存储过程，返回String类型，即兼容数字和字符、但日期类型无法保证格式。
     * 		对于无返回值的存储过程，会返回任意值，不取返回值即可。
     */
    public static String execSP(Connection conn, String proSql, Object[] params) {
        if(conn == null) {
            LOG.error("DB connection is closed.");
        }

        String result = null;
        DBTypeEnum dbType = DBUtils.judgeDBType(conn);
        switch (dbType) {
            case MYSQL: {
                result = execSpByMysql(conn, proSql, params);
                break;
            }
            case SYBASE: {
                result = execSpBySybase(conn, proSql, params);
                break;
            }
            case ORACLE: {
                result = execSpByOracle(conn, proSql, params);
                break;
            }
            default: {
                result = "";
                LOG.error("Unsupport database types.");
            }
        }
        return result;
    }

    /**
     * <PRE>
     * mysql存储过程调用，支持[无返回值]和 [单值]返回两种形式。
     *
     * 要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：最后一个结果集（即SELECT语句）的第1行、第1列的值。
     * </PRE>
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return
     * 		对于返回单值的存储过程，返回String类型，即兼容数字和字符、但日期类型无法保证格式。
     * 		对于无返回值的存储过程，会返回任意值，不取返回值即可。
     *
     * @throws SQLException 占位符与入参表个数不一致，或执行异常则抛出错误。
     */
    private static String execSpByMysql(Connection conn, String proSql, Object[] params) {
        String result = null;
        if(conn == null) {
            LOG.error("DB connection is closed.");

        } else if(proSql == null) {
            LOG.error("Procedure Sql is null.");

        } else {
            int paramNum = (params == null ? 0 : params.length);
            int placeNum = StringUtils.countMatches(proSql, '?');
            if(placeNum - paramNum != 0) {
                LOG.error("execute procedure [{}] fail: "
                        + "'?' countMatches doesn't match params countMatches.", proSql);

            } else {
                CallableStatement cs = null;
                ResultSet rs = null;
                try {
                    proSql = proSql.trim();
                    if(proSql.matches("^(?i)(call|exec) .*$")) {
                        proSql = proSql.substring(5);
                    }

                    cs = conn.prepareCall("{ CALL " + proSql + " }");
                    if(params != null){
                        for(int i = 0; i < params.length; i++) {
                            if (params[i] == null) {
                                cs.setNull(i + 1, Types.INTEGER);
                            } else {
                                cs.setObject(i + 1, params[i]);
                            }
                        }
                    }
                    cs.executeQuery();

                    //取最后一个结果集的首行首列值
                    try {
                        do {
                            rs = cs.getResultSet();
                            if(rs != null && rs.next()) {
                                result = rs.getString(1);
                            }
                        } while(cs.getMoreResults() == true);

                    } catch(NullPointerException e) {
                        result = "";	// 存储过程无返回值
                    }

                } catch (SQLException e) {
                    LOG.error("execute procedure [{}] fail.", proSql, e);

                } finally {
                    IOUtils.close(rs);
                    IOUtils.close(cs);
                }
            }
        }
        return result;
    }

    /**
     * <PRE>
     * sybase存储过程调用，支持[无返回值]和 [单值]返回两种形式。
     *
     * 要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：return所指定的值。
     * </PRE>
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return
     * 		对于返回单值的存储过程，返回String类型，即兼容数字和字符、但日期类型无法保证格式。
     * 		对于无返回值的存储过程，会返回任意值，不取返回值即可。
     *
     * @throws SQLException 占位符与入参表个数不一致，或执行异常则抛出错误。
     */
    private static String execSpBySybase(Connection conn, String proSql, Object[] params) {
        String result = null;
        if(conn == null) {
            LOG.error("DB connection is closed.");

        } else if(proSql == null) {
            LOG.error("Procedure Sql is null.");

        } else {
            int paramNum = (params == null ? 0 : params.length);
            int placeNum = StringUtils.countMatches(proSql, '?');
            if(placeNum - paramNum != 0) {
                LOG.error("execute procedure [{}] fail: "
                        + "'?' countMatches doesn't match params countMatches.", proSql);

            } else {
                CallableStatement cs = null;
                try {
                    proSql = proSql.trim();
                    if(proSql.matches("^(?i)(call|exec) .*$")) {
                        proSql = proSql.substring(5);
                    }

                    cs = conn.prepareCall("{ ? = CALL " + proSql + " }");
                    cs.registerOutParameter(1, Types.JAVA_OBJECT);
                    if(params != null){
                        for(int i = 0; i < params.length; i++) {
                            if (params[i] == null) {
                                cs.setNull(i + 2, Types.INTEGER);
                            } else {
                                cs.setObject(i + 2, params[i]);
                            }
                        }
                    }
                    cs.execute();
                    result = cs.getString(1);

                } catch (SQLException e) {
                    LOG.error("execute procedure [{}] fail.", proSql, e);

                } finally {
                    IOUtils.close(cs);
                }
            }
        }
        return result;
    }

    /**
     * <PRE>
     * oracle存储过程调用，支持[无返回值]和 [单值]返回两种形式。
     *
     * 要求：
     *  入参表：当proSql的占位符?个数 比 入参表params长度多0，为无返回值形式；
     *       多1，为有返回值形式。其余情况抛出SQLException异常。
     * 	返回值：当proSql的占位符?个数比入参表params多1，则认为最后1个占位符是出参。
     * </PRE>
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return
     * 		对于返回单值的存储过程，返回String类型，即兼容数字和字符、但日期类型无法保证格式。
     * 		对于无返回值的存储过程，会返回任意值，不取返回值即可。
     *
     * @throws SQLException 占位符与入参表个数不一致，或执行异常则抛出错误。
     */
    private static String execSpByOracle(Connection conn, String proSql, Object[] params) {
        String result = null;
        if(conn == null) {
            LOG.error("DB connection is closed.");

        } else if(proSql == null) {
            LOG.error("Procedure Sql is null.");

        } else {
            int paramNum = (params == null ? 0 : params.length);
            int placeNum = StringUtils.countMatches(proSql, '?');
            int diff = placeNum - paramNum;	// 占位符数 与 参数个数 的差异值

            if(diff != 0 && diff != 1) {
                LOG.error("execute procedure [{}] fail: "
                        + "'?' countMatches doesn't match params countMatches.", proSql);

            } else {
                CallableStatement cs = null;
                try {
                    proSql = proSql.trim();
                    if(proSql.matches("^(?i)(call|exec) .*$")) {
                        proSql = proSql.substring(5);
                    }

                    cs = conn.prepareCall("{ CALL " + proSql + " }");
                    int i = 0;
                    if(params != null){
                        for(; i < params.length; i++) {
                            if (params[i] == null) {
                                cs.setNull(i + 1, Types.INTEGER);
                            } else {
                                cs.setObject(i + 1, params[i]);
                            }
                        }
                    }

                    // 占位符数 比 参数个数 多1， 说明最后一个参数是出参
                    if(diff == 1) {
                        i = (i == 0 ? 1 : ++i);
                        cs.registerOutParameter(i, Types.VARCHAR);
                    }

                    cs.execute();
                    result = (diff == 1 ? cs.getString(i) : null);

                } catch (SQLException e) {
                    LOG.error("execute procedure [{}] fail.", proSql, e);

                } finally {
                    IOUtils.close(cs);
                }
            }
        }
        return result;
    }

    /**
     * <pre>
     * 调用存储过程，获取[结果集]返回。
     * 根据数据库连接自动识别 mysql、sybase、oracle。
     *
     * 注意：
     * 参数如果有null，则可能出错，特别是sybase数据库
     *
     * mysql存储过程要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：最后一个结果集（即SELECT语句）的第1行、第1列的值。
     *
     * sybase存储过程要求：
     *  入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：return所指定的值。
     *
     * oracle存储过程要求：
     *  入参表：当proSql的占位符?个数 比 入参表params长度多0，为无返回值形式；
     *       多1，为有返回值形式。其余情况抛出SQLException异常。
     * 	返回值：当proSql的占位符?个数比入参表params多1，则认为最后1个占位符是出参。
     * </pre>
     *
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return List<Map<String, Object>>结果集（不会返回null）
     */
    public static List<Map<String, Object>> callSP(Connection conn,
                                                   String proSql, Object[] params) {
        if(conn == null) {
            LOG.error("DB connection is closed.");
        }

        List<Map<String, Object>> result = null;
        DBTypeEnum dbType = DBUtils.judgeDBType(conn);
        switch (dbType) {
            case MYSQL: {
                result = _callSpByMysqlOrSybase(conn, proSql, params);
                break;
            }
            case SYBASE: {
                result = _callSpByMysqlOrSybase(conn, proSql, params);
                break;
            }
            case ORACLE: {
                result = _callSpByOracle(conn, proSql, params);
                break;
            }
            default: {
                result = new LinkedList<Map<String,Object>>();
                LOG.error("Unsupport database types.");
            }
        }
        return result;
    }

    /**
     * <PRE>
     * 存储过程调用，支持[结果集]返回形式。
     * 兼容mysql和sybase，不支持oralce。
     *
     * 要求：
     * 	入参表：proSql的占位符?个数 必须与 入参表params长度相同，否则抛出SQLException异常。
     * 	返回值：最后一个结果集（即SELECT语句）。
     * <PRE>
     * @param conn 数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表
     * @return 返回结果集的多行记录，每行为 列名-列值 的键值对。
     */
    private static List<Map<String, Object>> _callSpByMysqlOrSybase(
            Connection conn, String proSql, Object[] params) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        if(conn == null) {
            LOG.error("DB connection is closed.");

        } else if(proSql == null) {
            LOG.error("Procedure Sql is null.");

        } else {
            int paramNum = (params == null ? 0 : params.length);
            int placeNum = StringUtils.countMatches(proSql, '?');
            if(placeNum - paramNum != 0) {
                LOG.error("execute procedure [{}] fail: "
                        + "'?' countMatches doesn't match params countMatches.", proSql);

            } else {
                CallableStatement cs = null;
                ResultSet rs = null;
                try {
                    proSql = proSql.trim();
                    if(proSql.matches("^(?i)(call|exec) .*$")) {
                        proSql = proSql.substring(5);
                    }

                    cs = conn.prepareCall("{ CALL " + proSql + " }");
                    if(params != null){
                        for(int i = 0; i < params.length; i++) {
                            cs.setObject(i + 1, params[i]);
                        }
                    }
                    cs.executeQuery();

                    //取最后一个结果集，拼装返回值
                    do {
                        rs = cs.getResultSet();
                        if(rs != null) {
                            result.clear();	//若有下一个结果集，则清空前一个结果集
                            ResultSetMetaData rsmd = rs.getMetaData();
                            int colCnt = rsmd.getColumnCount();

                            Map<String, Object> rowMap = null;
                            while(rs.next()) {

                                rowMap = new HashMap<String, Object>();
                                for(int i = 1; i <= colCnt; i++) {
                                    rowMap.put(rsmd.getColumnLabel(i),
                                            rs.getObject(i));
                                }
                                result.add(rowMap);
                            }
                        }
                    } while(cs.getMoreResults() == true);

                } catch (SQLException e) {
                    LOG.error("execute procedure [{}] fail.", proSql, e);

                } finally {
                    IOUtils.close(rs);
                    IOUtils.close(cs);
                }
            }
        }
        return result;
    }

    /**
     * <PRE>
     * oracle存储过程调用，仅支持[结果集]返回。
     *
     * 要求：
     *  入参表：proSql的占位符?个数 比 入参表params长度多1，且最后1个占位符为返回结果集。
     *  	其余情况抛出SQLException异常。
     * 	返回值：结果集。
     * </PRE>
     * @param conn Oracle数据库连接
     * @param proSql 存储过程SQL，占位符格式，如 SP_TEST(?,?,?)
     * @param params 入参表，长度必须必占位符少1
     * @return 返回结果集的多行记录，每行为 列名-列值 的键值对。
     */
    private static List<Map<String, Object>> _callSpByOracle(
            Connection conn, String proSql, Object[] params) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
        if(conn == null) {
            LOG.error("DB connection is closed.");

        } else if(proSql == null) {
            LOG.error("Procedure Sql is null.");

        } else {
            int paramNum = (params == null ? 0 : params.length);
            int placeNum = StringUtils.countMatches(proSql, '?');
            int diff = placeNum - paramNum;	// 占位符数 与 参数个数 的差异值
            if(diff != 1) {
                LOG.error("execute procedure [{}] fail: "
                        + "'?' countMatches doesn't match params countMatches.", proSql);

            } else {
                CallableStatement cs = null;
                ResultSet rs = null;
                try {
                    proSql = proSql.trim();
                    if(proSql.matches("^(?i)(call|exec) .*$")) {
                        proSql = proSql.substring(5);
                    }

                    cs = conn.prepareCall("{ CALL " + proSql + " }");
                    int i = 0;
                    if(params != null){
                        for(; i < params.length; i++) {
                            cs.setObject(i + 1, params[i]);
                        }
                    }

                    //注册最后一个出参（游标类型）oracle.jdbc.OracleTypes.CURSOR = -10
                    cs.registerOutParameter(++i, -10);
                    cs.execute();
                    rs = cs.getResultSet();
                    if(rs != null) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int colCnt = rsmd.getColumnCount();
                        Map<String, Object> rowMap = null;

                        while(rs.next()) {
                            rowMap = new HashMap<String, Object>();

                            for(i = 1; i <= colCnt; i++) {
                                rowMap.put(rsmd.getColumnLabel(i),
                                        rs.getObject(i));
                            }
                            result.add(rowMap);
                        }
                    }
                } catch (SQLException e) {
                    LOG.error("execute procedure [{}] fail.", proSql, e);

                } finally {
                    IOUtils.close(rs);
                    IOUtils.close(cs);
                }
            }
        }
        return result;
    }
}

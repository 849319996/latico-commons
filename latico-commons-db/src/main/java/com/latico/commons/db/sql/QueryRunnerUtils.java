package com.latico.commons.db.sql;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <PRE>
 * apache的QueryRunner工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-03-11 1:32
 * @Version: 1.0
 */
public class QueryRunnerUtils {

    /**
     * @return 获取默认的查询工具
     */
    public static QueryRunner getQueryRunner() {
        return new QueryRunner();
    }

    /**
     * 使用QueryRunner查询，需要保证sql的列名和对象bean的一致
     * @param conn
     * @param sql
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     */
    public static <T extends Object> List<T> query(Connection conn, String sql, Class<T> clazz) throws SQLException {
        return getQueryRunner().query(conn, sql, new BeanListHandler<T>(clazz));
    }

    public static int insert(Connection conn, String sql, Object... params) throws SQLException {
        QueryRunner queryRunner = getQueryRunner();
        return queryRunner.update(conn, sql, params);
    }

}

package com.latico.commons.common.util.db.dao;

import com.latico.commons.common.util.db.DBUtils;

import java.sql.Connection;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-06-18 15:11
 * @version: 1.0
 */
public class DemoDao extends BaseDao {
    @Override
    protected Connection getConnection() {
        Connection conn = DBUtils.getConnectionByMysql("172.168.63.232", 3307, "test", "root", "root");
        return conn;
    }
}

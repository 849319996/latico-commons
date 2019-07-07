package com.latico.commons.common.util.db.dao;

import com.latico.commons.common.util.bean.NM_PERF_PORT_FLOW_PART;
import com.latico.commons.common.util.db.DBUtils;

import java.sql.Connection;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-06-18 15:20
 * @Version: 1.0
 */
public class NM_PERF_PORT_FLOW_PART_DAO extends BaseDao<NM_PERF_PORT_FLOW_PART> {

    @Override
    protected Connection getConnection() {
        Connection conn = DBUtils.getConnectionByOracle("172.168.27.7", 1522, "latico11g", "IPRAN2", "IPRAN2");
        return conn;
    }
}

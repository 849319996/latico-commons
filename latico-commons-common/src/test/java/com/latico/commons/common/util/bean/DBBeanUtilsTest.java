package com.latico.commons.common.util.bean;

import com.latico.commons.common.util.db.DBUtils;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DBBeanUtilsTest {

    @Test
    public void getBeanFromDB1() {
        Connection conn = DBUtils.getConnectionByMysql("172.168.63.232", 3307, "test", "root", "root");
        try {
            List<String> table = new ArrayList<String>();

            table.add("Demo");
            DBBeanUtils.getBeanFromDB(conn, "com.latico.commons.common.util.bean", "./src/test/java/com/latico/commons/common/util/bean", table);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.close(conn);
        }
    }

    @Test
    public void getBeanFromDB2() {
        Connection conn = DBUtils.getConnectionByOracle("172.168.27.7", 1521, "orcl", "root", "root");
        try {
            List<String> table = new ArrayList<String>();

            table.add("NM_PERF_PORT_FLOW_PART");
            DBBeanUtils.getBeanFromDB(conn, "com.latico.commons.common.util.bean", "./src/test/java/com/latico/commons/common/util/bean", table);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            DBUtils.close(conn);
        }
    }

}
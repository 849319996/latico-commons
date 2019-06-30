package com.latico.commons.common.util.db;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DBUtilsTest {

    @Test
    public void makeInsertPreparedSql() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("cloumn1");
//        columnNames.add("cloumn2");
//        columnNames.add("cloumn3");
        System.out.println(DBUtils.makeInsertPreparedSql("user", columnNames));
    }

    @Test
    public void makeSelectSql() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("cloumn1");
//        columnNames.add("cloumn2");
//        columnNames.add("cloumn3");
        System.out.println(DBUtils.makeSelectSql("user", columnNames));
    }
    @Test
    public void makeSelectSql2() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("cloumn1");
        columnNames.add("cloumn2");
        columnNames.add("cloumn3");
        System.out.println(DBUtils.makeSelectSql("user", columnNames));
    } @Test
    public void makeSelectSql3() {
        Map<String, String> columnNames = new LinkedHashMap<>();
        columnNames.put("cloumn1", "col1");
        columnNames.put("cloumn2", "col2");
        columnNames.put("cloumn3", "col3");
        System.out.println(DBUtils.makeSelectSql("user", columnNames));
    }

    @Test
    public void makeUpdatePreparedSql() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("cloumn1");
        columnNames.add("cloumn2");
        columnNames.add("cloumn3");
        System.out.println(DBUtils.makeUpdatePreparedSql("user", columnNames));
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
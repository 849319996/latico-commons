package com.latico.commons.common.util.db.dao;

import org.junit.Test;

public class BaseDaoTest {

    @Test
    public void insert1() {
        BaseDaoImplExample dao = new BaseDaoImplExample();
        System.out.println(dao.SQL_SELECT);
        System.out.println(dao.SQL_DELETE);
        System.out.println(dao.SQL_INSERT);
        System.out.println(dao.SQL_UPDATE);
        System.out.println(dao.getCharset());
    }
    @Test
    public void insert2() {
        BaseDaoImplExample2 dao = new BaseDaoImplExample2();
        System.out.println(dao.SQL_SELECT);
        System.out.println(dao.SQL_DELETE);
        System.out.println(dao.SQL_INSERT);
        System.out.println(dao.SQL_UPDATE);
        System.out.println(dao.getCharset());
    }

    @Test
    public void insert3() {
        BaseDaoImplExample2 dao = new BaseDaoImplExample2();
        System.out.println(dao.SQL_SELECT);
        System.out.println(dao.SQL_DELETE);
        System.out.println(dao.SQL_INSERT);
        System.out.println(dao.SQL_UPDATE);
        System.out.println(dao.getCharset());
    }
}
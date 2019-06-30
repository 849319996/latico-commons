package com.latico.commons.common.util.db.dao;

import org.junit.Test;

public class DemoDaoTest {

    /**
     *
     */
    @Test
    public void test1(){
        DemoDao demoDao = new DemoDao();
        Demo demo = new Demo();
        demoDao.insert(demo);

    }
}
package com.latico.commons.db.mycat.dao;

import com.latico.commons.db.mycat.entity.Demo;
import org.junit.Test;

public class DemoDaoTest {
    /**
     *
     */
    @Test
    public void test1(){
        DemoDao demoDao = new DemoDao();

        for (int i = 1; i <= 20; i++) {
            Demo demo = new Demo();
            demo.setId(i);
            demo.setAdministrator(i%2);
            demo.setUsername("username"+i);
            demo.setPassword("password"+i);
            demoDao.insert(demo);
        }
        System.out.println(demoDao.selectAll());
    }

    /**
     *
     */
    @Test
    public void test2(){
        DemoDao demoDao = new DemoDao();
        System.out.println(demoDao.selectAll());
    }
    /**
     *
     */
    @Test
    public void test3(){
        DemoDao demoDao = new DemoDao();
        System.out.println(demoDao.delete("where 1=1"));
    }


}
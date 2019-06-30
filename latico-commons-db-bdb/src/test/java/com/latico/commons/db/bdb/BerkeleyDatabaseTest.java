package com.latico.commons.db.bdb;

import org.junit.Test;

import static org.junit.Assert.*;

public class BerkeleyDatabaseTest {
    @Test
    public void use(){
        BerkeleyDatabase bdb = new BerkeleyDatabase("./src/test/resources/com/latico/commons/db/bdb/db_data_dir", "BDB");

        bdb.put("abc", "123");
        bdb.put("abc2", "1232");
        bdb.put("abc3", "1233");
        bdb.put("abc4", "1234");

        System.out.println(bdb.get("abc"));
        System.out.println(bdb.get("abc2"));
        System.out.println(bdb.get("abc3"));
        System.out.println(bdb.get("abc4"));
        bdb.closes();


    }
}
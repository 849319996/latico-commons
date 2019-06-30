package com.latico.commons.common;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.version.VersionExample;
import org.junit.Test;

public class VersionTest {

    @Test
    public void getVersionInfo() {
        LogUtils.loadLogBackConfigFromResources(null);
        VersionExample.printVersionInfo();
    }

    @Test
    public void test() {
        int 你好 = 1;
        System.out.println(你好);
//    Log log = LogFactory.getLog(VersionExample.class);

//        try {
//            System.out.println(Class.forName("com.latico.commons.common.util.logging.LogFactoryTest"));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        log.toString();

    }

    @Test
    public void test2() {
        System.out.println(VersionTest.class.getResourceAsStream("abc3.txt"));
        System.out.println(VersionTest.class.getResourceAsStream("abc4.txt"));
        System.out.println(VersionTest.class.getResourceAsStream("log4j.properties"));

        System.out.println(VersionTest.class.getClassLoader().getResourceAsStream("com/latico/commons/abc3.txt"));
        System.out.println(VersionTest.class.getClassLoader().getResourceAsStream("com/latico/commons/abc4.txt"));
        System.out.println(VersionTest.class.getClassLoader().getResourceAsStream("com/latico/commons/log4j.properties"));
    }
}
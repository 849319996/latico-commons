package com.latico.commons.common.bookmarke;

import org.junit.Test;

/**
 * 制作PDF的书签功能
 */
public class PdfFileUtilsTest {
   
    @Test
    public void test() throws Exception {
        int addNum = 19;
        String dir = "bookmarke/src/";
        String file = dir + "test.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    /**
     * 增加页码
     */
    @Test
    public void test1() throws Exception {
        int addNum = 19;
        String dir = "bookmarke/src/";
        String file = dir + "hadoop实战第二版目录书签.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test2() throws Exception {
        int addNum = 19;
        String dir = "bookmarke/src/";
        String file = dir + "精通Hadoop目录书签.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test3() throws Exception {
        int addNum = 19;
        String dir = "bookmarke/src/";
        String file = dir + "test2.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test4() throws Exception {
        int addNum = 15;
        String dir = "bookmarke/src/";
        String file = dir + "深入理解hadoop第二版.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test5() throws Exception {
        int addNum = 14;
        String dir = "bookmarke/src/";
        String file = dir + "Java性能优化权威指南.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test6() throws Exception {
        int addNum = 21;
        String dir = "bookmarke/src/";
        String file = dir + "Java软件结构与数据结构（第4版）.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test7() throws Exception {
        int addNum = 18;
        String dir = "bookmarke/src/";
        String file = dir + "深入理解Elasticsearch（原书第2版）.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test8() throws Exception {
        int addNum = 19;
        String dir = "bookmarke/src/";
        String file = dir + "Kafka权威指南.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test9() throws Exception {
        int addNum = 13;
        String dir = "bookmarke/src/";
        String file = dir + "MyBatis技术内幕.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test10() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "深入浅出Netty.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test11() throws Exception {
        int addNum = 22;
        String dir = "bookmarke/src/";
        String file = dir + "[实战Nginx 取代Apache的高性能Web服务器].张宴.扫描版.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test12() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "Hadoop实战第2版-陆嘉恒.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test13() throws Exception {
        int addNum = 12;
        String dir = "bookmarke/src/";
        String file = dir + "微服务架构基础-Spring Boot+Spring Cloud+Docker.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test14() throws Exception {
        int addNum = 23;
        String dir = "bookmarke/src/";
        String file = dir + "Spring Cloud与Docker微服务架构实战（第2版）.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


}
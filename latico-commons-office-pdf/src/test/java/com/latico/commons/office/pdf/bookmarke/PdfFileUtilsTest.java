package com.latico.commons.office.pdf.bookmarke;

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

    @Test
    public void test15() throws Exception {
        int addNum = 12;
        String dir = "bookmarke/src/";
        String file = dir + "深入理解MySQL.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test16() throws Exception {
        int addNum = 13;
        String dir = "bookmarke/src/";
        String file = dir + "深入理解MySQL核心技术.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test17() throws Exception {
        int addNum = 36;
        String dir = "bookmarke/src/";
        String file = dir + "Head First设计模式.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test18() throws Exception {
        int addNum = 2;
        String dir = "bookmarke/src/";
        String file = dir + "陶哲轩实分析-第一版.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test19() throws Exception {
        int addNum = 10;
        String dir = "bookmarke/src/";
        String file = dir + "陶哲轩教你学数学.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test20() throws Exception {
        int addNum = 11;
        String dir = "bookmarke/src/";
        String file = dir + "工程数学-线性代数(第五版).txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test21() throws Exception {
        int addNum = 15;
        String dir = "bookmarke/src/";
        String file = dir + "高等数学 第7版 上册 同济大学.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test22() throws Exception {
        int addNum = 6;
        String dir = "bookmarke/src/";
        String file = dir + "高等数学 第7版 下册 同济大学.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test23() throws Exception {
        int addNum = 7;
        String dir = "bookmarke/src/";
        String file = dir + "高等数学-同济大学第7版-上册-习题答案解析.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test24() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "高等数学-同济大学第7版-下册-习题答案解析-第八章.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test25() throws Exception {
        int addNum = 6;
        String dir = "bookmarke/src/";
        String file = dir + "高等数学-同济大学第7版-下册-习题答案解析.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test26() throws Exception {
        int addNum = 23;
        String dir = "bookmarke/src/";
        String file = dir + "数学指南-实用数学手册.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test27() throws Exception {
        int addNum = 30;
        String dir = "bookmarke/src/";
        String file = dir + "普林斯顿数学指南 第一卷.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test28() throws Exception {
        int addNum = 27;
        String dir = "bookmarke/src/";
        String file = dir + "普林斯顿数学指南 第二卷.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test29() throws Exception {
        int addNum = 31;
        String dir = "bookmarke/src/";
        String file = dir + "普林斯顿数学指南 第三卷.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test30() throws Exception {
        int addNum = 22;
        String dir = "bookmarke/src/";
        String file = dir + "写给大家看的大数据.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test31() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "httpClient4.1入门教程.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test32() throws Exception {
        int addNum = 15;
        String dir = "bookmarke/src/";
        String file = dir + "数据挖掘  实用案例分析.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test33() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "《Spring5高级编程（第5版）》_王净译.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test34() throws Exception {
        int addNum = 22;
        String dir = "bookmarke/src/";
        String file = dir + "Spring Data实战.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test35() throws Exception {
        int addNum = 11;
        String dir = "bookmarke/src/";
        String file = dir + "Spring源码深度解析(第一版).txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test36() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "深入浅出Spring Boot 2.x.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test37() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "JavaScript基础教程（第9版）.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test38() throws Exception {
        int addNum = 16;
        String dir = "bookmarke/src/";
        String file = dir + "大数据架构师指南.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test39() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "《疯狂Spring Cloud》电子书（一）.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test40() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "疯狂Spring Cloud微服务架构实战.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test41() throws Exception {
        int addNum = 18;
        String dir = "bookmarke/src/";
        String file = dir + "Maven实战.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test42() throws Exception {
        int addNum = 1;
        String dir = "bookmarke/src/";
        String file = dir + "PostgreSQL实战1.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test43() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "PostgreSQL实战2.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test44() throws Exception {
        int addNum = -1;
        String dir = "bookmarke/src/";
        String file = dir + "PostgreSQL实战3.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test45() throws Exception {
        int addNum = -1;
        String dir = "bookmarke/src/";
        String file = dir + "Netty实战.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test46() throws Exception {
        int addNum = 0;
        String dir = "bookmarke/src/";
        String file = dir + "Java 8实战.txt";
        BookMarkeUtilsTest.makeBookMarkePageAdd(file, addNum);
    }


}
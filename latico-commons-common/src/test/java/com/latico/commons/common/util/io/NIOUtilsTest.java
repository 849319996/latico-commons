package com.latico.commons.common.util.io;

import org.junit.Test;

public class NIOUtilsTest {

    @Test
    public void readFileToString() {
        try {
            System.out.println(NIOUtils.readFileToString("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.java"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void writeFile() {
        try {
            System.out.println(NIOUtils.writeFile("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.java", "你好啊啊", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void copyFile() {
        try {
            System.out.println(NIOUtils.copyFile("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.java", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test3.java", true));
            System.out.println(NIOUtils.copyFile("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.java", "C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test3.java", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
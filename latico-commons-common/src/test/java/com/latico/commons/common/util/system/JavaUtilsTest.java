package com.latico.commons.common.util.system;

import org.junit.Test;

public class JavaUtilsTest {

    @Test
    public void compileJavaFiles() {

        try {
            JavaUtils.compileJavaFiles("UTF-8","C:\\Users\\Administrator\\Desktop\\新建文件夹2\\Test2.java");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
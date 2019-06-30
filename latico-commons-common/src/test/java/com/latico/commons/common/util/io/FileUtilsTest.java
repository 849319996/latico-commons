package com.latico.commons.common.util.io;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void copyFile() {
        try {
            org.apache.commons.io.FileUtils.copyFile(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\ipran-net\\WebContent\\WEB-INF\\lib\\ipranProcessorClient.jar"), new File("C:\\Users\\Administrator\\Desktop\\新建文件夹2\\ipranProcessorClient.jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listFiles() {
        System.out.println(FileUtils.listFiles("./doc", ".xml"));
    }
    @Test
    public void listFiles2() {
//        System.out.println(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\latico-commons\\doc").exists());
//        System.out.println(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\latico-commons\\doc").isDirectory());
//        System.out.println(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\latico-commons\\doc").isFile());
        System.out.println(FileUtils.listFiles(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\latico-commons\\doc"), FileFilterUtils.suffixFileFilter(".xml"), DirectoryFileFilter.INSTANCE));
        System.out.println(FileUtils.listFiles(new File("D:\\eclipse\\eclipse_jee_4_5_1\\workspace\\latico-commons\\doc"), new String[]{"xml"}, true));
    }
}
package com.latico.commons.common.start;

import com.latico.commons.common.util.io.FileUtils;
import org.junit.Test;

public class MainExampleTest {

    @Test
    public void test() {
        System.out.println("文件存在:" + FileUtils.exists("config/Readme2.md"));
    }

}
package com.latico.commons.freemarker;

import freemarker.template.Template;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FreemarkerUtilsTest {


    @Test
    public void generateByResourcesFile() throws Exception {
        //1.创建数据模型
        Map<String, Object> root = new HashMap<String, Object>();
//2.赋值
        root.put("user_name", "胖先生");

        System.out.println(FreemarkerUtils.generateByResourcesFile("ftl/demo.ftl", root));
    }

    @Test
    public void generateByResourcesFileToFile() throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("user_name", "胖先生");
        FreemarkerUtils.generateResultFileByResourcesFile("ftl/demo.ftl", root, "./logs/test.txt");
    }
}
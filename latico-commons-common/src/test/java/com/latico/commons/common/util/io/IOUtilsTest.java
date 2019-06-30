package com.latico.commons.common.util.io;

import org.junit.Test;

import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void resourceToString() throws IOException {
        System.out.println(IOUtils.resourceToString("/config/configExample.xml"));
        System.out.println(IOUtils.resourceToString("/datanet_conf.xml"));
//        System.out.println(PropertiesUtils.getPropertiesByResourcesPath("/configExample.xml"));
    }
    @Test
    public void test() throws IOException {
//        System.out.println(PropertiesUtils.getPropertiesByResourcesPath("/configExample.xml"));
    }

}
package com.latico.commons.common.util.xml;

import org.junit.Test;

import java.io.IOException;

public class XmlUtilsTest {

    @Test
    public void isVaild() {
        System.out.println(Dom4jUtils.isInvaild("datanet_conf.xml"));
    }

    @Test
    public void isInvaild() {
    }

    @Test
    public void readXmlFileToString() throws IOException {
        System.out.println(XmlUtils.readResourcesXmlFileToString("datanet_conf.xml"));
    }

    @Test
    public void readResourcesXmlFileToString() {
    }

    @Test
    public void removeEmptyLines() {
    }

    @Test
    public void formatXml() {
    }

    @Test
    public void getXmlHeadEncoding() {
    }

    @Test
    public void getXmlHeadEncoding1() {
    }

    @Test
    public void toNamespaceURL() {
    }

    @Test
    public void toCompressPath() {
    }

    @Test
    public void matchesPath() {
    }
}
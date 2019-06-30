package com.latico.commons.common.util.xml;

import org.junit.Test;

public class Dom4jUtilsTest {

    @Test
    public void test() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<simulator.veraxsystems.com><types><type filepath=\"./results/iftable/iftable10.24.89.12.log\"><devices>><device ip=\"10.24.89.12\" netmask=\"32\" port=\"161\"/></devices>></type><type filepath=\"./results/iftable/iftable1.37.185.2.log\"><devices>><device ip=\"1.37.185.2\" netmask=\"32\" port=\"161\"/></devices>></type></types></simulator.veraxsystems.com>\n";

        System.out.println(Dom4jUtils.formatDefault(xml));
    }
}
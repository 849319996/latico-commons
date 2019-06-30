package com.latico.commons.webservice.soap.wsdl;

import org.junit.Test;

public class WsdlParerUtilsTest {

    @Test
    public void readWsdl() {
        System.out.println(WsdlParerUtils.readWsdlUri("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl"));
    }

    @Test
    public void readWsdl2() {
        System.out.println(WsdlParerUtils.readWsdlUri("http://127.0.0.1:8080/wsdemo?wsdl"));
    }

    @Test
    public void extractBodyParamInfo() {
    }

    @Test
    public void readWsdlFile() {
        System.out.println(WsdlParerUtils.readWsdlFile("E:\\公司文档\\凯通\\20160727DDPs\\DDPs\\ManageResourceInventory\\IIS\\wsdl\\EquipmentInventoryRetrieval\\EquipmentInventoryRetrievalMessages.wsdl", false));

    }

    @Test
    public void readWsdlFileDir() {
        System.out.println(WsdlParerUtils.readWsdlFile("E:\\公司文档\\凯通\\20160727DDPs\\DDPs\\ManageResourceInventory\\IIS\\wsdl\\EquipmentInventoryRetrieval\\EquipmentInventoryRetrievalJms", false));

    }
}
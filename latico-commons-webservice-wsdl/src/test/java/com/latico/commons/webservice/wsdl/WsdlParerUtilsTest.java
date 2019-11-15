package com.latico.commons.webservice.wsdl;

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
        System.out.println(WsdlParerUtils.readWsdlFile("./doc/ddps/ManageResourceInventory/IIS/wsdl/EquipmentInventoryRetrieval/EquipmentInventoryRetrievalHttp.wsdl", false));

    }

    @Test
    public void readWsdlFileDir() {
        System.out.println(WsdlParerUtils.readWsdlFile("./doc/ddps/ManageResourceInventory/IIS/wsdl/EquipmentInventoryRetrieval/EquipmentInventoryRetrievalJms.wsdl", false));

    }
}
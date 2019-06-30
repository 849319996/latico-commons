package com.latico.commons.net.snmp.client;

import org.junit.Test;

public class ISnmpClientTest {
    @Test
    public void test(){
        SnmpClient snmpClient = SnmpClientFactory.getSnmp4jClient();
        snmpClient.init("172.168.7.25", "public");
        System.out.println(snmpClient.walkDetail("1.3.6.1.2.1.4.22.1"));
        snmpClient.close();
    }
    @Test
    public void test2(){
        SnmpClient snmpClient = SnmpClientFactory.getAdventnetSnmpClient();
        snmpClient.init("172.168.7.25", "public");
        System.out.println(snmpClient.walkDetail("1.3.6.1.2.1.4.22.1"));
        snmpClient.close();
    }
    @Test
    public void test3(){
        SnmpClient snmpClient = SnmpClientFactory.getSnmp4jClient();
        snmpClient.init("10.24.89.12", "public");
        System.out.println(snmpClient.walkDetail(".1.3.6.1.4.1.2011.5.25.31.1.1.3.1.19"));
        snmpClient.close();
    }

}
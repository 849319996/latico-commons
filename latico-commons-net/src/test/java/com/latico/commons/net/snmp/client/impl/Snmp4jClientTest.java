package com.latico.commons.net.snmp.client.impl;

import com.latico.commons.net.snmp.bean.SnmpLine;
import com.latico.commons.net.snmp.bean.SnmpRow;
import com.latico.commons.net.snmp.bean.SnmpTable;
import com.latico.commons.net.snmp.client.SnmpClient;
import com.latico.commons.net.snmp.client.SnmpClientFactory;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

public class Snmp4jClientTest {

    @Test
    public void getTable() {
        SnmpClient snmp4jClient = SnmpClientFactory.getSnmp4jClient();
        //初始化
        System.out.println(new Timestamp(System.currentTimeMillis()));
        snmp4jClient.init("172.168.7.31", 161, "public", "v2", 30000);
        System.out.println(new Timestamp(System.currentTimeMillis()));

        //采集一个table的指定列
        SnmpTable ifTable = snmp4jClient.getSnmpTable("1.3.6.1.2.1.2.2.1",
                2, 5, 10, 16);

        System.out.println(ifTable);
        System.out.println(new Timestamp(System.currentTimeMillis()));

        // 获取所有需要采集流量的端口
        for (SnmpLine ifTableLine : ifTable.getLines()) {
            //截取掉table的OID和列ID后剩余的全部作为行ID
            String lineId = ifTableLine.getId();
            String name = ifTableLine.getValueString(2);
            Long ifHCInOctets = ifTableLine.getValueLong(10);
            Long ifHCOutOctets = ifTableLine.getValueLong(16);
            Double ifHighSpeed = ifTableLine.getValueDouble(5);

        }
    }

    @Test
    public void walk() {
        SnmpClient snmp4jClient = SnmpClientFactory.getSnmp4jClient();
        //初始化
        snmp4jClient.init("172.168.7.25", "public");

        //采集一个table的指定列
        List<SnmpRow> snmpRows = snmp4jClient.walkDetail("1.3.6.1.2.1.2.2.1.2");

        // 获取所有需要采集流量的端口
        for (SnmpRow snmpRow : snmpRows) {
            System.out.println(snmpRow.getOid());
            System.out.println(snmpRow.getValue());
        }
    }
}
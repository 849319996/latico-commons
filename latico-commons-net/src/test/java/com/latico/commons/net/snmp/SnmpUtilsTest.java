package com.latico.commons.net.snmp;

import org.junit.Test;

import static org.junit.Assert.*;

public class SnmpUtilsTest {

    @Test
    public void isNull() {
        System.out.println(SnmpUtils.isNull("No Such oid"));
        System.out.println(SnmpUtils.isNull("No noSuchInstance oid"));
        System.out.println(SnmpUtils.isNull("noSuchInstance oid"));
        System.out.println(SnmpUtils.isNull("noSuchInsta oid"));
    }
}
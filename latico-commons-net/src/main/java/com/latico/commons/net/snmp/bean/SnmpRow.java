package com.latico.commons.net.snmp.bean;

import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.string.StringUtils;

import java.io.Serializable;

/**
 * <PRE>
 * SNMP 列,SNMP walk用到
 *
 public static final byte ASN_BOOLEAN = 0x01;
 public static final byte ASN_INTEGER = 0x02;
 public static final byte ASN_BIT_STR = 0x03;
 public static final byte ASN_OCTET_STR = 0x04;
 public static final byte ASN_NULL = 0x05;
 public static final byte ASN_OBJECT_ID = 0x06;
 public static final byte ASN_SEQUENCE = 0x10;
 public static final byte ASN_SET = 0x11;
 public static final byte ASN_UNIVERSAL = 0x00;
 public static final byte ASN_APPLICATION = 0x40;
 public static final byte ASN_CONTEXT = (byte)0x80;
 public static final byte ASN_PRIVATE = (byte)0xC0;
 public static final byte ASN_PRIMITIVE = (byte)0x00;
 public static final byte ASN_CONSTRUCTOR = (byte)0x20;

 public static final byte ASN_LONG_LEN = (byte)0x80;
 public static final byte ASN_EXTENSION_ID = (byte)0x1F;
 public static final byte ASN_BIT8 = (byte)0x80;

 public static final byte INTEGER = ASN_UNIVERSAL | 0x02;
 public static final byte INTEGER32 = ASN_UNIVERSAL | 0x02;
 public static final byte BITSTRING = ASN_UNIVERSAL | 0x03;
 public static final byte OCTETSTRING = ASN_UNIVERSAL | 0x04;
 public static final byte NULL = ASN_UNIVERSAL | 0x05;
 public static final byte OID = ASN_UNIVERSAL | 0x06;
 public static final byte SEQUENCE = ASN_CONSTRUCTOR | 0x10;

 public static final byte IPADDRESS = ASN_APPLICATION | 0x00;
 public static final byte COUNTER = ASN_APPLICATION | 0x01;
 public static final byte COUNTER32 = ASN_APPLICATION | 0x01;
 public static final byte GAUGE = ASN_APPLICATION | 0x02;
 public static final byte GAUGE32 = ASN_APPLICATION | 0x02;
 public static final byte TIMETICKS = ASN_APPLICATION | 0x03;
 public static final byte OPAQUE = ASN_APPLICATION | 0x04;
 public static final byte COUNTER64 = ASN_APPLICATION | 0x06;

 public static final int NOSUCHOBJECT = 0x80;
 public static final int NOSUCHINSTANCE = 0x81;
 public static final int ENDOFMIBVIEW = 0x82;

 private static final int LENMASK = 0x0ff;
 public static final int MAX_OID_LENGTH = 127;
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-21 15:53
 * @version: 1.0
 */
public class SnmpRow implements Serializable {
    private static final long serialVersionUID = 1063115730717644669L;

    /**
     * 截取walk的节点OID剩下的
     */
    private String id;

    /**
     * OID
     */
    private String oid;
    /**
     * 值的类型 int形式
     */
    private int typeInt;

    /**
     * 值的类型，字符串形式
     */
    private String typeStr;

    /**
     * 值
     */
    private String value;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnmpRow{");
        sb.append("id='").append(id).append('\'');
        sb.append(", oid='").append(oid).append('\'');
        sb.append(", typeInt=").append(typeInt);
        sb.append(", typeStr='").append(typeStr).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return double值格式
     */
    public Double getValueDouble() {
        if (value != null) {
            if (StringUtils.isDouble(value)) {
                return NumberUtils.toDouble(value);
            }
        }
        return null;
    }

    /**
     * @return int值格式
     */
    public Integer getValueInt() {
        if (value != null) {
            if (StringUtils.isInt(value)) {
                return NumberUtils.toInt(value);
            }
        }
        return null;
    }

    /**
     * @return float值格式
     */
    public Float getValueFloat() {
        if (value != null) {
            if (StringUtils.isDouble(value)) {
                return NumberUtils.toFloat(value);
            }
        }
        return null;
    }

    /**
     * @return Long值格式
     */
    public Long getValueLong() {
        if (value != null) {
            if (StringUtils.isLong(value)) {
                return NumberUtils.toLong(value);
            }
        }
        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

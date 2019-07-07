package com.latico.commons.net.snmp.bean;


import com.latico.commons.common.util.string.StringUtils;

import java.io.Serializable;
import java.util.Map;


/**
 * <PRE>
 * SNMP的一行
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年10月24日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SnmpLine implements Serializable {

    private static final long serialVersionUID = -1404456702181943078L;
    /** id 行的ID,截取掉table的OID和列ID后剩余的全部作为行ID */
    private String id;
    
    /** columnIdValues 每一行的多个列ID和值的Map映射 */
    private Map<String, String> columnIdValues;
    
    public String getValueString(Object columnId) {
        if(columnIdValues != null && columnId != null) {
            return columnIdValues.get(columnId.toString());
        }
        return null;
    }
    
    public Integer getValueInt(Object columnId) {
        if(columnIdValues != null && columnId != null) {
            String value = columnIdValues.get(columnId.toString());
            if(value != null && StringUtils.isInt(value)) {
                return Integer.parseInt(value);
            }
        }
        return null;
    }
    
    public Long getValueLong(Object columnId) {
        if(columnIdValues != null && columnId != null) {
            String value = columnIdValues.get(columnId.toString());
            if(value != null && StringUtils.isLong(value)) {
                return Long.parseLong(value);
            }
        }
        return null;
    }
    
    public Double getValueDouble(Object columnId) {
        if(columnIdValues != null && columnId != null) {
            String value = columnIdValues.get(columnId.toString());
            if(value != null && StringUtils.isDouble(value)) {
                return Double.parseDouble(value);
            }
        }
        return null;
    }

    /**
     * @return 截取掉table的OID和列ID后剩余的全部作为行ID
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getColumnIdValues() {
        return columnIdValues;
    }

    public void setColumnIdValues(Map<String, String> columnIdValues) {
        this.columnIdValues = columnIdValues;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnmpLine{");
        sb.append("id='").append(id).append('\'');
        sb.append(", columnIdValues=").append(columnIdValues);
        sb.append('}');
        return sb.toString();
    }

}

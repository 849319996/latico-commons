package com.latico.commons.net.snmp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <PRE>
 * SNMP的一个表
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年10月24日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class SnmpTable implements Serializable {

    private static final long serialVersionUID = -3301472323666551584L;
    /**
     * 一个表有多行
     */
    private List<SnmpLine> lines;

    public List<SnmpLine> getLines() {
        return lines;
    }

    public void setLines(List<SnmpLine> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SnmpTable{");
        sb.append("lines=").append(lines);
        sb.append('}');
        return sb.toString();
    }

}

package com.latico.commons.disruptor.pc.impl;

/**
 * <PRE>
 * 事件包装类，用来装事件的实际数据
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-05-22 15:59
 * @Version: 1.0
 */
public class OneArgEventDefault<Arg> {
    /**
     * 事件的实际数据
     */
    private Arg data;

    public Arg getData() {
        return data;
    }

    public void setData(Arg data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OneArgEventDefault{");
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}

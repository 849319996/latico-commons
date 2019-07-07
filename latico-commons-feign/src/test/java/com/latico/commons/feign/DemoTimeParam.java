package com.latico.commons.feign;


import java.io.Serializable;
import java.util.Date;

/**
 * <PRE>
 *  时间对象
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-25 14:25:20
 * @Version: 1.0
 */
public class DemoTimeParam implements Serializable {

    private static final long serialVersionUID = -6041985998342837879L;

    private Date time;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }


    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DemoTimeParam{");
        sb.append("time=").append(time);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}

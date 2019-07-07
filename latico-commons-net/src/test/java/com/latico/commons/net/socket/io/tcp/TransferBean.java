package com.latico.commons.net.socket.io.tcp;

import java.io.Serializable;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-16 20:33
 * @Version: 1.0
 */
public class TransferBean implements Serializable {

    private static final long serialVersionUID = 3121073285349669945L;

    private String name;

    private boolean close;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClose() {
        return close;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TransferBean{");
        sb.append("name='").append(name).append('\'');
        sb.append(", close=").append(close);
        sb.append('}');
        return sb.toString();
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}

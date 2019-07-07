package com.latico.commons.hessian.common.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-09 22:27:29
 * @Version: 1.0
 */
public class BeanExample implements Serializable {
    /**
     * 必须序列化ID
     */
    private static final long serialVersionUID = -1352128884522619903L;
    private String userName;

    private int age;

    private Map<String, Object> detailData;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Object> getDetailData() {
        return detailData;
    }

    public void setDetailData(Map<String, Object> detailData) {
        this.detailData = detailData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeanExample{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", age=").append(age);
        sb.append(", detailData=").append(detailData);
        sb.append('}');
        return sb.toString();
    }
}
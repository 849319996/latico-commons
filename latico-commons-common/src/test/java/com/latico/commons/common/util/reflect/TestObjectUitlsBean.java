package com.latico.commons.common.util.reflect;

import java.util.Date;

/**
 * @author: latico
 * @date: 2018/12/16 1:39
 * @version: 1.0
 */
public class TestObjectUitlsBean {
    private Date date;
    private long number;
    private int age;
    private String name;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TestObjectUitlsBean{");
        sb.append("date=").append(date);
        sb.append(", number=").append(number);
        sb.append(", age=").append(age);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

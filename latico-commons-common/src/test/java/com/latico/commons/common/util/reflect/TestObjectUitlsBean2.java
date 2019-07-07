package com.latico.commons.common.util.reflect;

/**
 * @Author: latico
 * @Date: 2018/12/16 1:39
 * @Version: 1.0
 */
public class TestObjectUitlsBean2 extends TestObjectUitlsBean {
    private int age;
    private String name;

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
        final StringBuilder sb = new StringBuilder("TestObjectUitlsBean2{");
        sb.append("age=").append(age);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

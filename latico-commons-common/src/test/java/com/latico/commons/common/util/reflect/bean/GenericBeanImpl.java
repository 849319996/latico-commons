package com.latico.commons.common.util.reflect.bean;

import com.latico.commons.common.util.version.VersionExample;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-22 16:58
 * @version: 1.0
 */
public class GenericBeanImpl extends AbstractGenericBean<VersionExample> {
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GenericBeanImpl{");
        sb.append("age=").append(age);
        sb.append('}');
        return sb.toString();
    }
}

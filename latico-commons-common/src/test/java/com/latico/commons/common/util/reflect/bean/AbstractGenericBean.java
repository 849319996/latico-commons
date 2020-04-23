package com.latico.commons.common.util.reflect.bean;

import com.latico.commons.common.util.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-22 16:58
 * @version: 1.0
 */
public abstract class AbstractGenericBean<T> {

    private Map<String, Field> filedMap = FieldUtils.getAllFieldNameMap(this.getClass());
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractGenericBean{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void test() {
        System.out.println(filedMap);
    }
}

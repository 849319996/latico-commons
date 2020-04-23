package com.latico.commons.javaagent.onload;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-07-12 11:38
 * @version: 1.0
 */
public class BeanExample {
    /**
     *
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeanExample{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
    public void operation() {
        System.out.println("执行操作");
    }
}

package com.latico.commons.common.config.bean;

import com.latico.commons.common.config.FieldConfigNameAnnotation;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-04 2:39
 * @Version: 1.0
 */
@XStreamAlias("common")
public class Common {
    /**
     * 使用注解转换字段名称
     */
    @XStreamAlias("name1")
    private String nameAnnotation;

    /**
     * 不适用注解，直接匹配跟配置文件的一样
     */
    private String name2;
    /**
     * int类型，使用注解转换字段名称
     */
    @XStreamAlias("name3")
    private int nameInt;

    private int name4;
    private String name5;
    private String name6;

    public String getNameAnnotation() {
        return nameAnnotation;
    }

    public void setNameAnnotation(String nameAnnotation) {
        this.nameAnnotation = nameAnnotation;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getNameInt() {
        return nameInt;
    }

    public void setNameInt(int nameInt) {
        this.nameInt = nameInt;
    }

    public String getName5() {
        return name5;
    }

    public int getName4() {
        return name4;
    }

    public void setName4(int name4) {
        this.name4 = name4;
    }

    public void setName5(String name5) {
        this.name5 = name5;
    }

    public String getName6() {
        return name6;
    }

    public void setName6(String name6) {
        this.name6 = name6;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Common{");
        sb.append("nameAnnotation='").append(nameAnnotation).append('\'');
        sb.append(", name2='").append(name2).append('\'');
        sb.append(", nameInt=").append(nameInt);
        sb.append(", name4=").append(name4);
        sb.append(", name5='").append(name5).append('\'');
        sb.append(", name6='").append(name6).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

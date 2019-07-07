package com.latico.commons.common.util.compare;

import com.latico.commons.common.util.compare.annotationmode.ComapreAutoIncrementFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.ComapreKeyFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.ComapreValueFieldAnnotation;
import com.latico.commons.common.util.compare.annotationmode.CompareAnnotation;
import com.latico.commons.common.util.db.dao.NotDBFieldAnnotation;

import javax.persistence.Column;
import javax.persistence.Table;


/**
 * @Author: latico
 * @Date: 2018/12/05 16:00
 * @Version: 1.0
 */
@Table(name = "Compare_Bean")
@CompareAnnotation
public class CompareBean3{
    @ComapreAutoIncrementFieldAnnotation
    private int id;
    @ComapreKeyFieldAnnotation
    private String name;
    @Column(name = "nick_name")
    @ComapreKeyFieldAnnotation
    private String nickName;

    @ComapreValueFieldAnnotation
    private int age;
    @NotDBFieldAnnotation

    @ComapreValueFieldAnnotation
    private String sex;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CompareBean{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", age=").append(age);
        sb.append(", sex='").append(sex).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}

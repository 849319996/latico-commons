package com.latico.commons.common.util.example;

import java.lang.annotation.*;

/**
 * 定义性别注解
 *
 * @author liuyazhuang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Gender {

    public enum GenderEnum {BOY, GIRL}

    GenderEnum gender() default GenderEnum.BOY;
}
package com.latico.commons.common.util.example;

import java.lang.annotation.*;

/**
 * 定义基本信息Info注解
 *
 * @author liuyazhuang
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BaseInfo {
    String name() default "liuyazhuang";

    int age() default 18;

    String[] hobby() default {"basketball", "football"};
}
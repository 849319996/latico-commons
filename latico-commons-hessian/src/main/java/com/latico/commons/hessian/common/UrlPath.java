package com.latico.commons.hessian.common;

import java.lang.annotation.*;

/**
 * <PRE>
 * 用于构建URL路径的子路径
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-10 0:50
 * @Version: 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UrlPath {
    String value();
}

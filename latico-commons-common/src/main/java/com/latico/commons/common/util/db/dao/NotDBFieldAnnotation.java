package com.latico.commons.common.util.db.dao;

import java.lang.annotation.*;

/**
 * <PRE>
 * 非数据库字段
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-23 15:40
 * @Version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotDBFieldAnnotation {
}

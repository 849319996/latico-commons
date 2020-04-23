package com.latico.commons.common.util.db.dao;

import java.lang.annotation.*;

/**
 * <PRE>
 * 非数据库字段
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-23 15:40
 * @version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotDBFieldAnnotation {
}

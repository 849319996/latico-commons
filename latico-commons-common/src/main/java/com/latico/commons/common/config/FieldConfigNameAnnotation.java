package com.latico.commons.common.config;

import java.lang.annotation.*;

/**
 * <PRE>
 *  字段在配置文件中的名称
 * </PRE>
 * @author: latico
 * @date: 2019-06-27 11:26:08
 * @version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldConfigNameAnnotation {
    /**
     * @return 对应配置文件的节点名称
     */
    String value();
}

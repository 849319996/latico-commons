package com.latico.commons.common.util.db.dao;

import java.lang.annotation.*;

/**
 * <PRE>
 * 数据库表名
 * 也可以使用{@link javax.persistence.Table}
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-23 15:40
 * @version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DBTableNameAnnotation {
    /**
     * @return 对应配置文件的节点名称
     */
    String value();
}

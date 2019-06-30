package com.latico.commons.common.util.db.dao;

import java.lang.annotation.*;

/**
 * <PRE>
 * 数据库字段名
 * 也可以使用{@link javax.persistence.Column}
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-23 15:40
 * @Version: 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DBFieldNameAnnotation {
    /**
     * @return 对应配置文件的节点名称
     */
    String value();
}

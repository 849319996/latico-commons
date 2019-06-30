package com.latico.commons.common.util.compare.annotationmode;

import java.lang.annotation.*;

/**
 * <PRE>
 *  自动递增字段标注，效果跟{@link CompareAnnotation#autoIncrementFieldName()}一样
 *
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-13 16:39:20
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ComapreAutoIncrementFieldAnnotation {
}

package com.latico.commons.common.util.compare.annotationmode;

import java.lang.annotation.*;

/**
 * <PRE>
 * 差异比较的key注解，效果跟{@link CompareAnnotation#comapreKeyRelatedFieldNames()}一样
 * 该注解标志的字段，会作为key来关联
 * </PRE>
 * @Author: LanDingDong
 * @Date: 2019-06-13 16:39:27
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ComapreKeyFieldAnnotation {
}

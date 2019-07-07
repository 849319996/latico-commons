package com.latico.commons.common.util.compare.annotationmode;

import java.lang.annotation.*;

/**
 * <PRE>
 * 差异比较的value注解，效果跟{@link CompareAnnotation#compareValueRelatedFieldNames()}一样
 * 该注解标志的字段，会作为比较的值来关联
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-13 16:39:43
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ComapreValueFieldAnnotation {
}

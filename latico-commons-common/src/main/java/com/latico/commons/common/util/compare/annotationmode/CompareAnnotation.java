package com.latico.commons.common.util.compare.annotationmode;

import java.lang.annotation.*;

/**
 * <PRE>
 *  差异比较的注解，也可以指定父类所有字段
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-06-13 16:40:01
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CompareAnnotation {
    /**
     * 可选
     * @return 对象的自增字段名，用于比较过程中，从旧对象传递给新对象，这样入库的时候就不需要像mysql数据库等申请新的自增ID
     */
    String autoIncrementFieldName() default "";

    /**
     * 可选，可以指定多个字段名称，也可以通过注解：{@link ComapreKeyFieldAnnotation}标注，效果一样
     * @return 比较对象的时候，关联key，一般是一个字段，但是要根据实际情况可以指定多个字段
     */
    String[] comapreKeyRelatedFieldNames() default {};

    /**
     * 可选，可以指定多个字段名称，也可以通过注解：{@link ComapreValueFieldAnnotation}标注，效果一样
     * @return 比较对象的时候，comapreKeyRelatedFieldNames，差异比较的字段，但是要根据实际情况可以指定多个字段
     */
    String[] compareValueRelatedFieldNames() default {};
}

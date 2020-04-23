package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.math.NumberUtils;
import com.latico.commons.common.util.other.BooleanUtils;
import com.latico.commons.common.util.time.DateTimeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 字段工具
 * @author: latico
 * @date: 2018/12/11 23:59
 * @version: 1.0
 */
public class FieldUtils extends org.apache.commons.lang3.reflect.FieldUtils {
    /** LOG 日志工具 */
    private static final Logger LOG = LoggerFactory.getLogger(FieldUtils.class);
    
    /**
     * 检测字段上面是否带有指定注解
     *
     * @param field           被检测的字段
     * @param annotationClass 检测的注解
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresentOnField(Field field, Class<A> annotationClass) {
        if (field == null) {
            return false;
        }
        if (annotationClass == null) {
            return false;
        }
        if (field.isAnnotationPresent(annotationClass)) {
            return true;
        }

        return false;
    }

    /**
     * 指定字段获取注解
     *
     * @param field           指定字段
     * @param annotationClass 指定注解
     * @param <A>
     * @return 注解实例
     */
    public static <A extends Annotation> A getAnnotationPresentOnField(Field field, Class<A> annotationClass) {
        if (field == null) {
            return null;
        }
        if (annotationClass == null) {
            return null;
        }

        if (field.isAnnotationPresent(annotationClass)) {
            //拿到注解实例
            return field.getAnnotation(annotationClass);
        }
        return null;
    }

    /**
     * 返回字段名字跟字段的Map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Object> Map<String, Field> getAllFieldNameMap(Class<T> clazz) {
        return getAllFieldNameMap(clazz, false);
    }
    /**
     * 返回字段名字跟字段的Map
     * @param clazz
     * @param forceAccessible 是否设置强制可访问，true：设置
     * @param <T>
     * @return
     */
    public static <T extends Object> Map<String, Field> getAllFieldNameMap(Class<T> clazz, boolean forceAccessible) {
        Field[] allFields = getAllFields(clazz);
        Map<String, Field> map = new LinkedHashMap<String, Field>();
        if (allFields != null) {
            for (Field field : allFields) {
                if (forceAccessible && !field.isAccessible()) {
                    field.setAccessible(true);
                }
                map.put(field.getName(), field);
            }
        }
        return map;
    }

    /**
     * 写字段的值的时候，同时检测字段类型和传入的值的类型匹配
     * @param field 字段
     * @param target 目标对象
     * @param value 检测前字段的值
     * @param forceAccess 是否强制转换
     * @throws IllegalAccessException
     */
    public static void writeFieldWithCheckType(final Field field, final Object target, final Object value, final boolean forceAccess) throws IllegalAccessException, ParseException {
        Object convertedValue = convertValueByField(field, value);
        if(convertedValue != null){
            writeField(field, target, convertedValue, true);
        }
    }

    /**
     * 根据字段转换跟字段类型一样，目前只支持简单类型：基本数据类型、时间类型、封装过的基本数据类型
     * @param field
     * @param value
     * @return
     */
    public static Object convertValueByField(Field field, Object value) throws ParseException {
        Class<?> fieldClass = field.getType();
        Class<?> valueClass = value.getClass();
        Object convertedValue = null;
        if(fieldClass.isAssignableFrom(valueClass)){
            convertedValue = value;
        }else if(ClassUtils.isStringClass(fieldClass)){
            convertedValue = value.toString();
        }else if (ClassUtils.isLongClass(fieldClass)){
            convertedValue = Long.parseLong(value.toString());

        }else if (ClassUtils.isBooleanClass(fieldClass)){
            convertedValue = BooleanUtils.toBoolean(value.toString());

        }else if (ClassUtils.isDoubleClass(fieldClass)){
            convertedValue = NumberUtils.toDouble(value.toString());

        }else if (ClassUtils.isFloatClass(fieldClass)){
            convertedValue = NumberUtils.toFloat(value.toString());

        }else if (ClassUtils.isShortClass(fieldClass)){
            convertedValue = NumberUtils.toShort(value.toString());

        }else if (ClassUtils.isIntegerClass(fieldClass)){
            convertedValue = NumberUtils.toInt(value.toString());

        }else if (ClassUtils.isBigDecimalClass(fieldClass)){
            convertedValue = NumberUtils.toBigDecimal(value.toString());

        }else if (ClassUtils.isTimestampClass(fieldClass)){
            convertedValue = DateTimeUtils.toTimestamp(DateTimeUtils.toDateBy_ymdhms(value.toString()));

        }else if (ClassUtils.isSqlDateClass(fieldClass)){
            convertedValue = new java.sql.Date(DateTimeUtils.toDateBy_ymdhms(value.toString()).getTime());

        }else if (ClassUtils.isByteClass(fieldClass)){
            convertedValue = NumberUtils.toByte(value.toString());

        }else{
            LOG.warn("不支持字段转换类型:{}", field.getType());
        }
        return convertedValue;
    }

    /**
     * 是不是final字段
     * @param field
     * @return
     */
    public static boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    /**
     * 是不是静态字段
     * @param field
     * @return
     */
    public static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers());
    }

}

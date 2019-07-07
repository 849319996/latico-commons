package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * 类工具
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 *
 * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @version <B>V1.0 2018年10月25日</B>
 * @since <B>JDK1.6</B>
 */
public class ClassUtils extends org.apache.commons.lang3.ClassUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtils.class);
    /**
     * 获取所有的超类和接口
     * @param cls
     * @return
     */
    public static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
        final List<Class<?>> allSuperclasses = getAllSuperclasses(cls);
        int superClassIndex = 0;
        final List<Class<?>> allInterfaces = getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() ||
                superClassIndex < allSuperclasses.size()) {
            Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if (superClassIndex >= allSuperclasses.size()) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (interfaceIndex < superClassIndex) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (superClassIndex < interfaceIndex) {
                acls = allSuperclasses.get(superClassIndex++);
            } else {
                acls = allInterfaces.get(interfaceIndex++);
            }
            allSuperClassesAndInterfaces.add(acls);
        }
        return allSuperClassesAndInterfaces;
    }
    /**
     * 检测类上面是否带有指定注解
     *
     * @param clazz           被检测的类
     * @param annotationClass 检测的注解
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresentOnClass(Class<?> clazz, Class<A> annotationClass) {
        if (clazz == null) {
            return false;
        }
        if (annotationClass == null) {
            return false;
        }
        if (clazz.isAnnotationPresent(annotationClass)) {
            return true;
        }

        return false;
    }

    /**
     * 获取类上面带有指定注解
     *
     * @param clazz           被检测的类
     * @param annotationClass 检测的注解类类型
     * @param <A>             指定注解类型
     * @return 指定注解的注解实例
     */
    public static <A extends Annotation> A getAnnotationPresentOnClass(Class<?> clazz, Class<A> annotationClass) {
        if (!isAnnotationPresentOnClass(clazz, annotationClass)) {
            return null;
        }
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * 检查cClazz是否为fClazz的子类
     * @param childClass (期望的)子类
     * @param fatherClass (期望的)父类
     * @return true:是; false:否
     */
    public static boolean isSubclass(Class<?> childClass, Class<?> fatherClass) {
        boolean isChild = false;
        try {
            childClass.asSubclass(fatherClass);
            isChild = true;

        } catch (Exception e) {
            // 报错说明不是子类
        }
        return isChild;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isStringClass(Class<?> fieldClass) {
        return fieldClass == String.class;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isSqlTimeClass(Class<?> fieldClass) {
        return fieldClass == java.sql.Time.class;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isTimestampClass(Class<?> fieldClass) {
        return fieldClass == java.sql.Timestamp.class;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isBlobClass(Class<?> fieldClass) {
        return fieldClass == java.sql.Blob.class || fieldClass.toString().matches("(?i).*blob.*");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isClobClass(Class<?> fieldClass) {
        return fieldClass == java.sql.Clob.class || fieldClass.toString().matches("(?i).*clob.*");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isBigDecimalClass(Class<?> fieldClass) {
        return fieldClass == java.math.BigDecimal.class;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isIntegerClass(Class<?> fieldClass) {
        return fieldClass == Integer.class || fieldClass.toString().matches("(?i)int");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isShortClass(Class<?> fieldClass) {
        return fieldClass == Short.class || fieldClass.toString().matches("(?i)short");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isFloatClass(Class<?> fieldClass) {
        return fieldClass == Float.class || fieldClass.toString().matches("(?i)float");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isByteClass(Class<?> fieldClass) {
        return fieldClass == Byte.class || fieldClass.toString().matches("(?i)byte");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isDoubleClass(Class<?> fieldClass) {
        return fieldClass == Double.class || fieldClass.toString().matches("(?i)double");
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isBooleanClass(Class<?> fieldClass) {
        return fieldClass == Boolean.class || fieldClass.toString().matches("(?i)boolean");
    }
    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isSqlDateClass(Class<?> fieldClass) {
        return fieldClass == java.sql.Date.class;
    }

    /**
     *
     * @param fieldClass
     * @return
     */
    public static boolean isLongClass(Class<?> fieldClass) {
        return fieldClass == Long.class || fieldClass.toString().matches("(?i)long");
    }
}
package com.latico.commons.common.util.reflect;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 注解工具
 *
 * @author: latico
 * @date: 2018/12/02 20:56
 * @version: 1.0
 */
public class AnnotationUtils extends org.apache.commons.lang3.AnnotationUtils {

    /**
     * 更新注解某个字段的值
     * @param annotation 注解
     * @param fieldName 成员字段名
     * @param newValue 新的值
     * @return
     * @throws Exception
     */
    public static boolean updateAnnotationField(Annotation annotation, String fieldName, Object newValue) throws Exception {

        //获取注解动态代理实例所持有的 InvocationHandler
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);

        // 获取 InvocationHandler 的 memberValues 字段
        Field memberValuesField = invocationHandler.getClass().getDeclaredField("memberValues");

        // 因为这个字段是 private final 修饰，所以要打开权限
        memberValuesField.setAccessible(true);

        // 获取 memberValues字段的值
        Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(invocationHandler);

        //修改注解上面成员字段的值
        memberValues.put(fieldName, newValue);

        return true;
    }

    /**
     * 获取注解类的保留时间，可选值 SOURCE（源码时），CLASS（编译时），RUNTIME（运行时），默认为 CLASS
     *
     * @param annotationType 注解类
     * @return 保留时间枚举
     */
    public static RetentionPolicy getRetentionPolicy(Class<? extends Annotation> annotationType) {
        final Retention retention = annotationType.getAnnotation(Retention.class);
        if (null == retention) {
            return RetentionPolicy.CLASS;
        }
        return retention.value();
    }

    /**
     * 获取注解类可以用来修饰哪些程序元素，如 TYPE, METHOD, CONSTRUCTOR, FIELD, PARAMETER 等
     *
     * @param annotationType 注解类
     * @return 注解修饰的程序元素数组
     */
    public static ElementType[] getTargetType(Class<? extends Annotation> annotationType) {
        final Target target = annotationType.getAnnotation(Target.class);
        if (null == target) {
            return new ElementType[] { ElementType.TYPE, //
                    ElementType.FIELD, //
                    ElementType.METHOD, //
                    ElementType.PARAMETER, //
                    ElementType.CONSTRUCTOR, //
                    ElementType.LOCAL_VARIABLE, //
                    ElementType.ANNOTATION_TYPE, //
                    ElementType.PACKAGE//
            };
        }
        return target.value();
    }

    /**
     * 是否会保存到 Javadoc 文档中
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    /**
     * 是否可以被继承，默认为 false
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }
}

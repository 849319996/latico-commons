package com.latico.commons.common.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 方法工具
 * @Author: LanDingDong
 * @Date: 2018/12/12 0:29
 * @Version: 1.0
 */
public class MethodUtils extends org.apache.commons.lang3.reflect.MethodUtils {

    /**
     * 获取一个类的所有方法名称和参数名称用横杠分割的key的Map关系,子类会覆盖父类的方法
     * @param clazz 类对象
     * @return key是一个用方法名加字段的类全名称，用横杠分割的字符串，值是方法
     */
    public static Map<String, Method> getAllMethodAtOverride(Class clazz){

        List<Method> allMethods = getAllMethods(clazz);
        if (allMethods == null) {
            return null;
        }
        //方法名唯一，假如方法上面有参数，那就带上参数的字段类型全程
        Map<String, Method> methodAndParamNameMap = new LinkedHashMap<String, Method>();
        for (Method method : allMethods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            String methodNameParam = method.getName();

            //组装方法名和所有参数的字符串
            if (parameterTypes != null && parameterTypes.length >= 1) {
                for (Class<?> parameterType : parameterTypes) {
                    methodNameParam += "-" + parameterType.getName();
                }
            }

            //如果子类已经获取过该方法，说明子类覆盖了父类，那就跳过，不获取父类的
            if(methodAndParamNameMap.containsKey(methodNameParam)){
                continue;

            }else{
                methodAndParamNameMap.put(methodNameParam, method);
            }
        }

        return methodAndParamNameMap;
    }

    /**
     * 获取一个类的所有方法，包括超类和接口的，类如果继承了超类又实现了接口，本类排在第一，接着超类的排在前面，接口的排在后面
     * @param cls 本类
     * @return
     */
    public static List<Method> getAllMethods(Class cls){
        if (cls == null) {
            return null;
        }
        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<>();
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);

        allSuperClassesAndInterfaces.add(cls);
        allSuperClassesAndInterfaces.addAll(allSuperclasses);
        allSuperClassesAndInterfaces.addAll(allInterfaces);

        List<Method> methods = new ArrayList<>();
        for (Class<?> superClassesAndInterface : allSuperClassesAndInterfaces) {
            Method[] declaredMethods = superClassesAndInterface.getDeclaredMethods();
            if (declaredMethods != null) {
                methods.addAll(Arrays.asList(declaredMethods));
            }
        }
        return methods;
    }

    /**
     * 检测方法上面是否带有指定注解
     *
     * @param method          被检测的字段
     * @param annotationClass 检测的注解
     * @return
     */
    public static <A extends Annotation> boolean isAnnotationPresentOnMethod(Method method, Class<A> annotationClass) {
        if (method == null) {
            return false;
        }
        if (annotationClass == null) {
            return false;
        }
        if (method.isAnnotationPresent(annotationClass)) {
            return true;
        }

        return false;
    }


    /**
     * 指定方法获取指定注解
     *
     * @param method          指定方法
     * @param annotationClass 指定注解
     * @param <A>
     * @return 注解实例
     */
    public static <A extends Annotation> A getAnnotationPresentOnMethod(Method method, Class<A> annotationClass) {
        if (method == null) {
            return null;
        }
        if (annotationClass == null) {
            return null;
        }

        if (method.isAnnotationPresent(annotationClass)) {
            //拿到注解实例
            return method.getAnnotation(annotationClass);
        }
        return null;
    }
}

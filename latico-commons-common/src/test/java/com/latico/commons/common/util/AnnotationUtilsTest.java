package com.latico.commons.common.util;


import com.latico.commons.common.util.example.BaseInfo;
import com.latico.commons.common.util.example.Gender;
import com.latico.commons.common.util.example.User;
import com.latico.commons.common.util.example.User3;
import com.latico.commons.common.util.reflect.FieldUtils;
import com.latico.commons.common.util.reflect.MethodUtils;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @Author: latico
 * @Date: 2018/12/05 17:26
 * @Version: 1.0
 */
public class AnnotationUtilsTest {

    @Test
    public void isAnnotationPresentOnClass() {
        assertTrue(com.latico.commons.common.util.reflect.ClassUtils.isAnnotationPresentOnClass(User.class, BaseInfo.class));
    }

    @Test
    public void isAnnotationPresentOnField() {
    }

    @Test
    public void isAnnotationPresentOnMethod() {
    }

    @Test
    public void getAnnotationPresentOnClass() {
        System.out.println(MethodUtils.getAllMethods(User3.class));
        System.out.println(MethodUtils.getAllMethodAtOverride(User3.class));
    }

    @Test
    public void getAllAnnotationPresentOnField() {
        System.out.println(FieldUtils.getFieldsListWithAnnotation(User3.class, Gender.class));
    }

    @Test
    public void getAnnotationPresentOnField() {

    }

    @Test
    public void getAllAnnotationPresentOnMethod() {
        System.out.println(MethodUtils.getMethodsListWithAnnotation(User3.class, BaseInfo.class, true, true));
    }

    @Test
    public void getAnnotationPresentOnMethod() {
        System.out.println(ClassUtils.getAllSuperclasses(User3.class));
    }
}
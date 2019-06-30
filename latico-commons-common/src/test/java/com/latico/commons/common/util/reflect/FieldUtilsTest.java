package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.compare.CompareBean;
import org.junit.Test;

import java.lang.reflect.Field;

public class FieldUtilsTest {

    private String name;
    private final String age = "";
    private static String id;
    @Test
    public void test(){
        Field[] allFields = FieldUtils.getAllFields(CompareBean.class);
        System.out.println(allFields[0].getType());
        System.out.println(allFields[0].getDeclaringClass());
    }

    @Test
    public void isFinal() throws NoSuchFieldException {
        Field age = FieldUtilsTest.class.getDeclaredField("age");
        Field id = FieldUtilsTest.class.getDeclaredField("id");
        System.out.println(FieldUtils.isFinal(age));
        System.out.println(FieldUtils.isFinal(id));
        System.out.println(FieldUtils.isStatic(age));
        System.out.println(FieldUtils.isStatic(id));
    }

    @Test
    public void isStatic() {
    }
}
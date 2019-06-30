package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.reflect.bean.AbstractGenericBean;
import com.latico.commons.common.util.reflect.bean.GenericBeanImpl;
import org.junit.Test;

public class GenericUtilsTest {

    @Test
    public void test() {
    }
    @Test
    public void getSuperClassGenricType() {
        AbstractGenericBean<GenericUtilsTest> abstractGenericBean = new AbstractGenericBean<GenericUtilsTest>() {
        };
        System.out.println(GenericUtils.getSuperClassGenricType(abstractGenericBean.getClass()));

        abstractGenericBean.test();

        GenericBeanImpl GenericBeanImpl = new GenericBeanImpl();
        GenericBeanImpl.test();
    }
}
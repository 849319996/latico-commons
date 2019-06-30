package com.latico.commons.cglib;

import org.junit.Test;

public class CglibUtilsTest {

    @Test
    public void createProxyInstance() {
        UserServiceImpl bookFacedImpl = null;
        try {
            bookFacedImpl = CglibUtils.createProxyInstance(UserServiceImpl.class, UserServiceCglibMethodInterceptor.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bookFacedImpl.addUser();
    }
}
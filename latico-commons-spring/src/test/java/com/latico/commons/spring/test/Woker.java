package com.latico.commons.spring.test;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-01 17:26
 * @version: 1.0
 */
public class Woker implements Person {
    @Override
    public String doSomeThing(String someThing) {
        return hashCode() + "工人干活com.latico.commons.spring.Woker:" + someThing;
    }
}

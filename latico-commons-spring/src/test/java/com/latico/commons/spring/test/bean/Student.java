package com.latico.commons.spring.test.bean;

import com.latico.commons.spring.test.Study;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-01 18:12
 * @Version: 1.0
 */
@Component
public class Student implements Study {
    @Override
    public String doSomeThing(String someThing) {
        return hashCode() + "学生学习com.latico.commons.spring.bean.Student：" + someThing;
    }
}

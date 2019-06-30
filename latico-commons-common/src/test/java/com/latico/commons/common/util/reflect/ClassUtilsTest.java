package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.compare.CompareBean;
import com.latico.commons.common.util.compare.annotationmode.CompareAnnotation;
import com.latico.commons.common.util.logging.LogImplAnnotation;
import com.latico.commons.common.util.logging.impl.LogbackLogImpl;
import org.junit.Test;

public class ClassUtilsTest {

    @Test
    public void isAnnotationPresentOnClass() {
        System.out.println(ClassUtils.isAnnotationPresentOnClass(CompareBean.class, CompareAnnotation.class));
        System.out.println(ClassUtils.isAnnotationPresentOnClass(LogbackLogImpl.class, LogImplAnnotation.class));
    }
}
package com.latico.commons.common.util.reflect;

import com.latico.commons.common.util.compare.CompareBean;
import com.latico.commons.common.util.compare.annotationmode.CompareAnnotation;
import org.junit.Test;

import java.util.Arrays;

public class AnnotationUtilsTest {

    @Test
    public void updateAnnotationField() {
        CompareAnnotation annotation = ClassUtils.getAnnotationPresentOnClass(CompareBean.class, CompareAnnotation.class);
        System.out.println(Arrays.toString(annotation.comapreKeyRelatedFieldNames()));

        try {
            AnnotationUtils.updateAnnotationField(annotation, "comapreKeyRelatedFieldNames", new String[]{"jagj", "ajjg"});
            System.out.println(Arrays.toString(annotation.comapreKeyRelatedFieldNames()));

            annotation = ClassUtils.getAnnotationPresentOnClass(CompareBean.class, CompareAnnotation.class);
            System.out.println(Arrays.toString(annotation.comapreKeyRelatedFieldNames()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
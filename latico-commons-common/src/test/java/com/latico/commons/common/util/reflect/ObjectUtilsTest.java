package com.latico.commons.common.util.reflect;

import org.junit.Test;

import java.sql.Timestamp;

public class ObjectUtilsTest {

    @Test
    public void getAllFieldNameValueMap() throws Exception {
        TestObjectUitlsBean2 testObjectUitlsBean2 = new TestObjectUitlsBean2();

        testObjectUitlsBean2.setAge(12);
        testObjectUitlsBean2.setName("xiaoming");
        testObjectUitlsBean2.setDate(new Timestamp(System.currentTimeMillis()));
        testObjectUitlsBean2.setNumber(23356);

        System.out.println(ObjectUtils.getAllFieldNameValueMap(testObjectUitlsBean2));
    }
}
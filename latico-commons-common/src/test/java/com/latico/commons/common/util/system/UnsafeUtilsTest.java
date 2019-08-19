package com.latico.commons.common.util.system;

import com.latico.commons.common.util.system.classloader.ClassLoaderUtils;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class UnsafeUtilsTest {

    int i = 0;
    public UnsafeUtilsTest() {
        i = 2;
    }
    /**
     *
     */
    @Test
    public void test() throws Exception {
        System.out.println(UnsafeUtils.getUnsafeObj());
    }

    @Test
    public void getUnsafeObj() {
    }

    @Test
    public void allocateInstance() {
        UnsafeUtilsTest unsafeUtils = UnsafeUtils.allocateInstance(UnsafeUtilsTest.class);
        System.out.println(unsafeUtils.i);

        unsafeUtils = new UnsafeUtilsTest();
        System.out.println(unsafeUtils.i);
    }

    @Test
    public void putInt() {
        UnsafeUtilsTest unsafeUtils = new UnsafeUtilsTest();
        System.out.println(unsafeUtils.i);
        UnsafeUtils.putInt(unsafeUtils, "i", 5);
        System.out.println(unsafeUtils.i);
    }

    /**
     *
     */
    @Test
    public void staticFieldBase() {
    }

    @Test
    public void staticFieldOffset() {

    }

    @Test
    public void allocateMemory() {
        long data = 1000;
        long memory = UnsafeUtils.allocateMemory(1);
        UnsafeUtils.putDataToAddress(memory, data);
        System.out.println(UnsafeUtils.getDataByAddress(memory));
    }

    @Test
    public void getDataByAddress() {
    }

    @Test
    public void putDataToAddress() {
    }
}
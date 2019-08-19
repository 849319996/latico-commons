package com.latico.commons.common.util.system;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.reflect.FieldUtils;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * <PRE>
 * Unsafe对象，能操作内存，{@link Unsafe}
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-15 20:18
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
public class UnsafeUtils {
    private static final Logger LOG = LoggerFactory.getLogger(UnsafeUtils.class);

    /**
     * Unsafe对象
     */
    public static final Unsafe UNSAFE = getUnsafeObj();

    /**
     * 获取能操作内存的Unsafe对象
     * 因为Unsafe对象只允许Bootstrap访问，不能通过正常方式获取{@link Unsafe#getUnsafe()}，所以这里通过反射获取，因为Unsafe中有个字段就是自身{@link Unsafe#theUnsafe}
     * @return
     * @throws Exception
     */
    public static Unsafe getUnsafeObj() {
        try {
            // 通过反射得到theUnsafe对应的Field对象
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置该Field为可访问
            field.setAccessible(true);
            // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的，所以与对象无关，不需要传入具体对象
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    /**
     * 传入一个对象的class并创建该实例对象，但不会调用构造方法
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T allocateInstance(Class<T> clazz) {
        Object obj = null;
        try {
            obj = UNSAFE.allocateInstance(clazz);
        } catch (InstantiationException e) {
            LOG.error(e);
        }
        return (T)obj;
    }

    /**
     * 因为偏移值是可以根据类对象就可以计算出来，每个实例对象的字段的偏移值跟类对象中的字段的偏移值一致。
     * @param field 类的字段
     * @return 对象的字段在对象中的偏移值
     */
    public static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    /**
     * 因为偏移值是可以根据类对象就可以计算出来，每个实例对象的字段的偏移值跟类对象中的字段的偏移值一致。
     * @param field 类的字段
     * @return 对象的字段在对象中的偏移值
     */
    public static long objectFieldOffset(Class clazz, String fieldName) {
        Field field = FieldUtils.getField(clazz, fieldName, true);
        return UNSAFE.objectFieldOffset(field);
    }

    /**
     * 因为偏移值是可以根据类对象就可以计算出来，每个实例对象的字段的偏移值跟类对象中的字段的偏移值一致。
     * @param field 类的字段
     * @return 对象的字段在对象中的偏移值
     */
    public static long staticFieldOffset(Class clazz, String fieldName) {
        Field field = FieldUtils.getField(clazz, fieldName, true);
        return UNSAFE.staticFieldOffset(field);
    }

    /**
     * 因为偏移值是可以根据类对象就可以计算出来，每个实例对象的字段的偏移值跟类对象中的字段的偏移值一致。
     * @param field 类的字段
     * @return 对象的字段在对象中的偏移值
     */
    public static long staticFieldOffset(Field field) {
        return UNSAFE.staticFieldOffset(field);
    }


    /**
     * 给对象obj，直接操作内存的方式设置value值
     * @param obj
     * @param field
     * @param value
     */
    public static void putInt(Object obj, Field field, int value) {
        UNSAFE.putInt(obj, objectFieldOffset(field), value);
    }

    /**
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void putInt(Object obj, String fieldName, int value) {
        Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
        UNSAFE.putInt(obj, objectFieldOffset(field), value);
    }

    /**
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void putObject(Object obj, String fieldName, Object value) {
        Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
        UNSAFE.putObject(obj, objectFieldOffset(field), value);
    }

    /**
     * @param obj
     * @param objectFieldOffset
     * @return
     */
    public static Object getObjectFieldValue(Object obj, long objectFieldOffset) {
        return UNSAFE.getObject(obj, objectFieldOffset);
    }

    /**
     * 分配内存
     * @param size 内存大小，单位字节
     * @return 内存首地址
     */
    public static long allocateMemory(long size) {
        return UNSAFE.allocateMemory(size);
    }

    /**
     * 把数据设置进内存中
     * @param memoryAddress 内存地址
     * @return 数据
     */
    public static void putDataToAddress(long memoryAddress, long data) {
        UNSAFE.putAddress(memoryAddress, data);
    }

    /**
     * 获取内存中的数据
     * @param memoryAddress 内存地址
     * @return 数据
     */
    public static long getDataByAddress(long memoryAddress) {
        return UNSAFE.getAddress(memoryAddress);
    }
}

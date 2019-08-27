package com.latico.commons.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.latico.commons.objenesis.ObjenesisUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <PRE>
 * Protostuff序列化工具
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-08-22 15:07
 * @Version: 1.0
 */
public class ProtostuffUtils {

    /**
     * 缓存类的模式
     */
    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    /**
     * 获取一个类的模式
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    public static <T> String serializeToString(T obj) throws UnsupportedEncodingException {
        Class<T> cls = (Class<T>) obj.getClass();
        Schema<T> schema = getSchema(cls);

        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return new String(ProtobufIOUtil.toByteArray(obj, schema, buffer), "ISO8859-1");
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserializeFromString(String data, Class<T> cls) throws UnsupportedEncodingException {
        T message = ObjenesisUtils.createObject(cls);
        Schema<T> schema = getSchema(cls);
        ProtobufIOUtil.mergeFrom(data.getBytes("ISO8859-1"), message, schema);
        return message;
    }


    public static <T> byte[] serializeToByte(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);

        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserializeFromByte(byte[] data, Class<T> cls) {
        T obj = ObjenesisUtils.createObject(cls);
        Schema<T> schema = getSchema(cls);
        ProtobufIOUtil.mergeFrom(data, obj, schema);
        return obj;
    }
}

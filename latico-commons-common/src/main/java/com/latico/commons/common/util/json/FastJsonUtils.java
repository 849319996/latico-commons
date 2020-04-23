package com.latico.commons.common.util.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;

/**
 * <PRE>
 * fastJson对于json格式字符串的解析主要用到了一下三个类：
 *
 * JSON：fastJson的解析器，用于JSON格式字符串与JSON对象及javaBean之间的转换。
 *
 * JSONObject：fastJson提供的json对象。
 *
 * JSONArray：fastJson提供json数组对象。
 * </PRE>
 *
 * @author: latico
 * @date: 2018-12-26 9:23
 * @version: 1.0
 */
public class FastJsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FastJsonUtils.class);

    /**
     * 对象类型转json字符串
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String objToJson(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return JSONObject.toJSONString(obj);
        } catch (Exception e) {
            LOG.error("JSON转换失败", e);
        }
        return null;
    }

    /**
     * json字符串转泛型对象，不支持非泛型
     *
     * @param json
     * @param valueType  new TypeReference<T>(){} 通过传入泛型，目的是让JSON能通过Type superClass = getClass().getGenericSuperclass();拿到指定泛型
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String json, TypeReference<T> valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSONObject.parseObject(json, valueType);
    }

    /**
     * json字符串转对象类型，不支持泛型类型
     *
     * @param json  json字符串
     * @param clazz 对象类型
     * @param <T>   泛型
     * @return 对象
     */
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            LOG.error("JSON转换失败", e);
        }
        return null;
    }

    /**
     * 拷贝对象
     * @param obj
     * @return
     */
    public static Object copyObj(Object obj) {
        if (obj == null) {
            return null;
        }
        String json = objToJson(obj);
        return jsonToObj(json, obj.getClass());
    }

    /**
     * 拷贝对象
     * @param obj
     * @param targetClass 目标类
     * @return
     */
    public static <T> T copyObj(Object obj, Class<T> targetClass) {
        if (obj == null) {
            return null;
        }
        String json = objToJson(obj);
        return jsonToObj(json, targetClass);
    }
}

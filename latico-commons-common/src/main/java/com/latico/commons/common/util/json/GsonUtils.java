package com.latico.commons.common.util.json;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @Author: latico
 * @Date: 2018/12/11 16:54
 * @Version: 1.0
 */
public class GsonUtils extends JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(GsonUtils.class);

    private static final Gson gson = new Gson();

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
            return gson.toJson(obj);
        } catch (Exception e) {
            LOG.error("JSON转换失败", e);
        }
        return null;
    }

    /**
     * json字符串转泛型对象，不支持非泛型
     * @param json
     * @param valueType new TypeToken<T>() {}
     * @param <T>
     * @return
     */
    public static <T> T jsonToObj(String json, TypeToken<T> valueType) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        try {
            return gson.fromJson(json, valueType.getType());
        } catch (Exception e) {
            LOG.error("JSON转换失败", e);
        }
        return null;
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
            return gson.fromJson(json, clazz);
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

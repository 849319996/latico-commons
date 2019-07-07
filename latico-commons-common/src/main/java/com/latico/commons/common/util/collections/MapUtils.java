package com.latico.commons.common.util.collections;

import com.latico.commons.common.util.string.StringUtils;

import java.util.Hashtable;
import java.util.Map;

/**
 * @Author: latico
 * @Date: 2018/12/12 22:56
 * @Version: 1.0
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {

    /**
     * 把一个带有分隔符的键值对形式的字符串转换成Map，
     * 比如 abc=1;cde=2;  通过传入toMap("abc=1;cde=2", ";", "=");
     * 得到键值对的Map
     *
     * @param str           字符串本身
     * @param elementDelimiter 元素分隔符，键值对之间的主分隔符
     * @param keyValueDelimiter  键值对自身的分隔符
     * @return
     */
    public static Map<String, String> strToMap(String str, String elementDelimiter, String keyValueDelimiter) {
        if (StringUtils.isEmpty(str)) {
            return new Hashtable<String, String>();
        }
        String[] strArray = StringUtils.split(str, elementDelimiter);
        Hashtable<String, String> hsb = new Hashtable<String, String>();
        for (int i = 0; i < strArray.length; i++) {
            String substr = strArray[i];
            int index = substr.indexOf(keyValueDelimiter);
            if (index > 0) {
                String key = substr.substring(0, index);
                if (substr.length() > index + 1) {
                    String value = substr.substring(index + 1);
                    hsb.put(key, value);
                }
            }
        }

        return hsb;
    }

    /**
     * 把Map转换成字符串，
     *
     * @param map               被转换的Map
     * @param elementDelimiter  元素分隔符，对每对key-value的分隔符
     * @param keyValueDelimiter 每对key和value的分隔符
     * @return
     */
    public static String mapToStr(Map<?, ?> map, String elementDelimiter, String keyValueDelimiter) {
        if (isEmpty(map)) {
            return "";
        }
        if (StringUtils.isEmpty(elementDelimiter)) {
            return "";
        }

        if (StringUtils.isEmpty(keyValueDelimiter)) {
            return "";
        }
        String value = null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            if(entry.getValue() != null){
                value = entry.getValue().toString();
            }else{
                value = "";
            }
            sb.append(entry.getKey().toString()).append(keyValueDelimiter).append(value).append(elementDelimiter);

        }

        return sb.toString();
    }

    /**
     * 获取第一个Key
     * @param map
     * @return
     */
    public static <K, V> K getFirstKey(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return entry.getKey();
        }
        return null;

    }

    /**
     * 获取第一个非空的Key
     *
     * @param map
     * @return
     */
    public static <K, V> K getFirstNotNullKey(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            return entry.getKey();
        }
        return null;

    }
    /**
     * 获取第一个值
     *
     * @param map
     * @return
     */
    public static <K, V> V getFirstValue(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return entry.getValue();
        }
        return null;

    }
    /**
     * 获取第一个非空的值
     * @param map
     * @return
     */
    public static <K, V> V getFirstNotNullValue(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }


    /**
     * Map.Entry
     *
     * @param map
     * @return
     */
    public static <K, V> Map.Entry<K, V> getFirstEntry(Map<K, V> map) {
        if (isEmpty(map)) {
            return null;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            return entry;
        }
        return null;
    }

    /**
     * 获取值
     * @param map
     * @param key
     * @param valueDefault
     * @param <T>
     * @return
     */
    public static <T> T getValue(Map<String, T> map, String key, T valueDefault){
        if(map == null || key == null){
            return valueDefault;
        }else{
            if(map.containsKey(key)){
                return map.get(key);
            }else{
                return valueDefault;
            }
        }
    }
}

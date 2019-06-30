package com.latico.commons.common.util.collections;

/**
 * 数组工具类
 *
 * JDK自带的数组工具类{@link java.util.Arrays}
 * @Author: LanDingDong
 * @Date: 2018/12/12 22:06
 * @Version: 1.0
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    /**
     * 指定分隔符拼接字符串
     * @param array 数组，不能为空
     * @param delimiter 分隔符,不能为空
     * @return
     */
    public static String toStringSpecialDelimiter(Object[] array, String delimiter){
        if (array == null || delimiter == null) {
            return null;
        }
        final int size = array.length;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        String value;
        for (Object o : array) {
            count++;
            if(o != null){
                value = o.toString();
            }else{
                value = "";
            }
            if(size != count){
                sb.append(value).append(delimiter);
            }else{
                sb.append(value);
            }
        }
        return sb.toString();
    }
}

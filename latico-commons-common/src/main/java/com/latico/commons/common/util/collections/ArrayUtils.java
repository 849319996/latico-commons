package com.latico.commons.common.util.collections;

import com.latico.commons.common.util.math.NumberUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 数组工具类
 *
 * JDK自带的数组工具类{@link java.util.Arrays}
 * @Author: latico
 * @Date: 2018/12/12 22:06
 * @Version: 1.0
 */
public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

    /**
     * 对等相加，每个索引位置相加
     *
     * @param arrFirst
     * @param arrSecond
     * @return
     */
    public static String[] peerAdd(String[] arrFirst, String[] arrSecond) {
        if (isEmpty(arrFirst) || isEmpty(arrSecond)) {
            throw new IllegalArgumentException("指定数组存在空");
        }
        if (arrFirst.length != arrSecond.length) {
            throw new IllegalArgumentException("指定两个数组长度不一致");
        }

        String[] arr = new String[arrFirst.length];
        for (int i = 0; i < arrFirst.length; i++) {
            if (arrFirst[i] != null && arrSecond[i] != null) {
                arr[i] = arrFirst[i] + arrSecond[i];
            } else if (arrFirst[i] != null) {
                arr[i] = arrFirst[i];
            } else {
                arr[i] = arrSecond[i];
            }
        }

        return arr;

    }

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


    /**
     * <PRE>
     * 移除array中的重复元素（其他元素保持顺序不变）
     * 此方法[不会修改]array的内容.
     * <PRE>
     *
     * @param array 需要移除重复元素的array
     * @return 移除重复元素后的array
     */
    public static <E> E[] removeDuplicate(E... array) {
        E[] ary = null;
        if (array != null) {
            ary = Arrays.copyOf(array, array.length);    // 复制源数组
            Set<E> set = new HashSet<E>();    // 唯一集
            for (int i = 0; i < ary.length; i++) {
                if (set.add(ary[i]) == false) {
                    ary[i] = null;
                }
            }
            set.clear();
            int len = cutbackNull(ary);    // 后移null元素(即被删除的元素)
            ary = Arrays.copyOfRange(ary, 0, len);    // 删除末尾空元素
        }
        return ary;
    }



    /**
     * 把数组中所有null元素后移，非null元素前移（数组实际长度不变, 非空元素顺序不变）
     *
     * @param array 原数组
     * @return 数组非空元素长度
     */
    public static <E> int cutbackNull(E... array) {
        if (array == null) {
            return 0;
        }

        int pNull = 0;    // 上次检索到的空元素指针位置
        int len = 0;        // 非空数组的实际长度（即非空元素个数）
        for (; len < array.length; len++) {
            if (array[len] != null) {
                continue;
            }

            for (int j = NumberUtils.max(len, pNull) + 1; j < array.length; j++) {
                if (array[j] == null) {
                    continue;
                }

                array[len] = array[j];
                array[j] = null;
                pNull = j;
                break;
            }

            // 说明后续所有元素均为null
            if (array[len] == null) {
                break;
            }
        }
        return len;
    }

    /**
     * <PRE>
     * 把数组转换成String字符串.
     * (空元素用"null"代替, 其他元素调用其toString()方法, 元素间用逗号分隔)
     * </PRE>
     *
     * @param array 数组
     * @return 若数组为空则返回 [], 否则返回形如 [aa, null, cc, dd]
     */
    public static String toString(Object... array) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (array != null) {
            for (Object o : array) {
                sb.append(o == null ? "null" : o.toString()).append(", ");
            }

            if (array.length > 0) {
                sb.setLength(sb.length() - 2);
            }
        }
        sb.append(']');
        return sb.toString();
    }

    /**
     * 把数组转换成队列
     *
     * @param array 数组
     * @return 队列
     */
    public static <E> Set<E> toSet(E... array) {
        if (array == null || array.length <= 0) {
            return new HashSet<>();
        }
        Set<E> set = new LinkedHashSet<>();
        for (E e : array) {
            set.add(e);
        }
        return set;
    }

}

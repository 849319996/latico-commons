package com.latico.commons.common.util.collections;

import com.latico.commons.common.util.math.NumberUtils;

import java.util.*;

/**
 * <PRE>
 * 队列/数组集合操作工具
 * jdk自带有{@link Collections}工具类，建议同时使用
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018/12/08 20:44:00
 * @Version: 1.0
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
    /**
     * 测试数组是否为空(null或长度<=0)
     *
     * @param list 被测试数组
     * @return true:是; false:否
     */
    public static <E> boolean isEmpty(List<E> list) {
        return (list == null || list.isEmpty());
    }

    /**
     * 测试数组是否为空(null或长度<=0)
     *
     * @param list 被测试数组
     * @return true:是; false:否
     */
    public static <E> boolean isNotEmpty(List<E> list) {
        return !isEmpty(list);
    }

    /**
     * 测试数组是否为空(null或长度<=0)
     *
     * @param array 被测试数组
     * @return true:是; false:否
     */
    public static <E> boolean isEmpty(E... array) {
        return (array == null || array.length <= 0);
    }

    /**
     * 测试数组是否非空(长度>0)
     *
     * @param array 被测试数组
     * @return true:是; false:否
     */
    public static <E> boolean isNotEmpty(E... array) {
        return !isEmpty(array);
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
        StringBuffer sb = new StringBuffer();
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
     * <PRE>
     * 移除list中的重复元素（其他元素保持顺序不变）
     * 此方法[会修改]list的内容.
     * <PRE>
     *
     * @param list 需要移除重复元素的list
     * @return 移除元素个数
     */
    public static <E> int removeDuplicate(List<E> list) {
        int cnt = 0;
        if (list != null) {
            Set<E> set = new HashSet<E>();    // 唯一集
            for (Iterator<E> its = list.iterator(); its.hasNext(); ) {
                E e = its.next();
                if (set.add(e) == false) {
                    its.remove();
                    cnt++;
                }
            }
            set.clear();
        }
        return cnt;
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
     * 克隆队列
     *
     * @param list 原队列
     * @return 克隆队列（与原队列属于不同对象， 但若队列元素非常量，则两个队列的【元素内部操作】会互相影响）
     */
    public static <E> List<E> copySurface(List<E> list) {
        List<E> copy = new LinkedList<E>();
        if (list != null) {
            copy.addAll(list);
        }
        return copy;
    }

    /**
     * 克隆数组队列
     *
     * @param list 原数组队列
     * @return 克隆数组队列（与原队列属于不同对象， 但若队列元素非常量，则两个队列的【元素内部操作】会互相影响）
     */
    public static <E> List<E> copyArray(List<E> list) {
        List<E> copy = new ArrayList<E>(list == null ? 1 : list.size());
        if (list != null) {
            copy.addAll(list);
        }
        return copy;
    }

    /**
     * 反转队列元素
     *
     * @param list 原队列
     * @return 反转元素队列（与原队列属于不同对象， 但若队列元素非常量，则两个队列的【元素内部操作】会互相影响）
     */
    public static <E> List<E> reverse(List<E> list) {
        List<E> reverse = copySurface(list);
        reverse(reverse);
        return reverse;
    }

    /**
     * 把数组转换成队列
     *
     * @param array 数组
     * @return 队列
     */
    public static <E> List asList(E... array) {
        if (array == null || array.length <= 0) {
            return new ArrayList();
        }
        return Arrays.asList(array);
    }

    /**
     * 把数组转换成队列
     *
     * @param array 数组
     * @return 队列
     */
    public static <E> List<E> toList(E... array) {
        if (array == null || array.length <= 0) {
            return new ArrayList();
        }
        return Arrays.asList(array);
    }

    /**
     * 把数组转换成队列
     *
     * @param coll 集合
     * @return 队列
     */
    public static <E> List<E> toList(Collection<E> coll) {
        if (coll == null || coll.size() <= 0) {
            return new ArrayList();
        }
        return new ArrayList<>(coll);
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

    /**
     * 把List转换成Set
     *
     * @param list 数组
     * @return 队列
     */
    public static <E> Set<E> toSet(List<E> list) {
        if (list == null || list.isEmpty()) {
            return new HashSet<>();
        }
        Set<E> set = new LinkedHashSet<>();
        set.addAll(list);
        return set;
    }

    /**
     * 取队列中第一个元素
     *
     * @param list 队列
     * @return 第一个元素(若队列为空则返回null)
     */
    public static <E> E getFirst(List<E> list) {
        E first = null;
        if (list != null && !list.isEmpty()) {
            first = list.get(0);
        }
        return first;
    }

    /**
     * 取队列中顺数第二个元素
     *
     * @param list 队列
     * @return 第二个元素(若队列为空或长度不足则返回null)
     */
    public static <E> E getSecond(List<E> list) {
        E second = null;
        if (list != null && list.size() > 1) {
            second = list.get(1);
        }
        return second;
    }

    /**
     * 取队列中倒数第二个元素
     *
     * @param list 队列
     * @return 倒数第二个元素(若队列为空或长度不足则返回null)
     */
    public static <E> E getSecondToLast(List<E> list) {
        E secondToLast = null;
        if (list != null && list.size() > 1) {
            secondToLast = list.get(list.size() - 2);
        }
        return secondToLast;
    }

    /**
     * 取队列中最后一个元素
     *
     * @param list 队列
     * @return 最后一个元素(若队列为空则返回null)
     */
    public static <E> E getLast(List<E> list) {
        E last = null;
        if (list != null && !list.isEmpty()) {
            last = list.get(list.size() - 1);
        }
        return last;
    }

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
     * 添加元素
     *
     * @param list
     * @param objs
     */
    public static void add(Collection<?> list, Object... objs) {
        if (list == null || objs == null) {
            return;
        }
        addAll(list, objs);
    }

    public static Object[] toArrayObj(List<Object> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new Object[list.size()]);
    }

    public static String[] toArrayStr(List<String> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    public static Integer[] toArrayInt(List<Integer> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new Integer[list.size()]);
    }

    public static Long[] toArrayLong(List<Long> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new Long[list.size()]);
    }

    public static Double[] toArrayDouble(List<Double> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new Double[list.size()]);
    }

    public static Float[] toArrayFloat(List<Float> list) {
        if (list == null) {
            return null;
        }
        return list.toArray(new Float[list.size()]);
    }
}

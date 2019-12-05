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
     * @return 只要有一个是空，那么就返回true，true:是; false:否
     */
    public static <E extends Collection> boolean isAllEmpty(E... array) {
        if(array == null || array.length <= 0){
            return true;
        }

        for (E e : array) {
            if (e == null) {
                continue;
            }
            if (!e.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 测试数组是否非空(长度>0)
     *
     * @param array 被测试数组
     * @return 所有非空，true:是; false:否
     */
    public static <E extends Collection> boolean isAllNotEmpty(E... array) {
        if(array == null || array.length <= 0){
            return false;
        }

        for (E e : array) {
            if (e == null || e.isEmpty()) {
                return false;
            }
        }
        return true;
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
        return ArrayUtils.peerAdd(arrFirst, arrSecond);
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

    /**
     * 判断大小
     * @param col
     * @return
     */
    public static int size(Collection col) {
        if (col == null) {
            return 0;
        }
        return col.size();
    }

    /**
     * 判断大小
     * @param map
     * @return
     */
    public static int size(Map map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }
}

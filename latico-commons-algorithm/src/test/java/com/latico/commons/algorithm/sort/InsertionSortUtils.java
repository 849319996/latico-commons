package com.latico.commons.algorithm.sort;

/**
 * <PRE>
 * 插入排序
 * 每次比较后最多移掉一个逆序，因此与冒泡排序的效率相同.但它在速度
 * 上还是要高点，这是因为在冒泡排序下是进行值交换，而在插入排序下是值移动,
 * 所以直接插入排序将要优于冒泡排序.直接插入法也是一种对数据的有序性非常敏感的一种算法.
 * 在有序情况下只需要经过n-1次比较,在最坏情况下,将需要n(n-1)/2次比较
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-26 9:44
 * @Version: 1.0
 */
public class InsertionSortUtils {

    /**
     * 从数组第二个数开始，每轮都把该数从该数据为起点，从后往前判断，如果前面的数据大于自己，就把后面的数后移一位
     *
     * @param array
     */
    public static void sort(int[] array) {
//        i是每轮要比较的数据索引，从第二个元素开始
        for (int i = 1; i < array.length; i++) {

//            先临时保存当前要比较的数据
            int currentValue = array[i];

//            j是i前面的数据，从后往前逐个跟i比较
            int j = i - 1;
            while (j >= 0) {
                //                找不到比当前值大的了，退出循环
                if (array[j] <= currentValue) {
                    break;
                }
//                把数据后移一位
                array[j + 1] = array[j];

                //向前一位
                j--;
            }

//            因为上面for循环中的j--，最后减多了一个1，假如是因为找不到了，退出循环，那么由于j = i - 1，也是减多了一次1，所以在这里要加上，
            array[j + 1] = currentValue;
        }
    }

    /**
     * 利用后面的数i逐个跟前面的数j比较，直到找到一个比i大的，就交换，一直跟前面的所有数据比较完
     *
     * @param array
     */
    public static void sort2(int[] array) {
//        i是后面的数
        for (int i = 1; i < array.length; i++) {

//            j是前面的数据，逐个跟i比较
            for (int j = 0; j < i; j++) {
                if (array[j] > array[i]) {
                    swap(array, i, j);
                }
            }
        }
    }

    private static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

}

package com.latico.commons.algorithm.sort;

import java.util.List;

/**
 * <PRE>
 冒泡排序
 时间

 冒泡排序（Bubble Sort，台湾译为：泡沫排序或气泡排序）是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果他们的顺序错误就把他们交换过来
 。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。

 冒泡排序算法的运作如下：

 比较相邻的元素，如果第一个比第二个大，就交换他们两个。
 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，在这一点，最后的元素应该会是最大的数。 针对所有的元素重复以上的步骤，除了最后一个。
 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。

 时间复杂度：n(n-1)/2次比较。所以一般情况下,特别是在逆序时,它很不理想。
 </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-25 23:34
 * @Version: 1.0
 */
public class BubbleSortUtils {

    /**
     * 排序一个数组，从小到大
     冒泡排序法思路

     1：外层循环：控制它要走几次。
     假设你有5个数，那就要走4次，最后一次不用走，最后那个数已经在它位置了所以就要length-1次。
     2：内层循环：控制逐一比较，如果发现前一个数比后一个数大，则交换。
     注意！因为越比较长度就越小了，所以长度要length-1-i。

     * @param arr
     * @return
     */
    public static void sortArr(int[] arr) {
        //最大的循环次数
        final int maxCyclicCount = arr.length - 1;
        for (int i = 0; i < maxCyclicCount; i++) {

            //因为每轮都选出了最大值放到后面，最大判断值每次都减少1一个，通过减i实现
            for (int j = 0; j < maxCyclicCount - i; j++) {

                //判断是否大于自己后面一位
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    /**
     * 交换AZ位置
     * @param arr
     * @param a
     * @param z
     */
    private static void swap(int[] arr, int a, int z) {
        int tmp = arr[a];
        arr[a] = arr[z];
        arr[z] = tmp;
    }

    /**
     * 从小到大
     * @param list
     * @param <T>
     */
    public static <T extends Comparable> void sortComparable(List<T> list) {
        //最大的循环次数
        final int maxCyclicCount = list.size() - 1;
        for (int i = 0; i < maxCyclicCount; i++) {
            for (int j = 0; j < maxCyclicCount - i; j++) {
                T a;
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    a = list.get(j);
                    list.set((j), list.get(j + 1));
                    list.set(j + 1, a);
                }
            }
        }
    }
}

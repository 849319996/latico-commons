package com.latico.commons.algorithm.sort;

/**
 * <PRE>
 * 快速排序
 * 1、选取一种分区中心元素；
 * 2、排序时，一个下标从左边开始，一个从右边开始，互相交换数据，根据中心元素分成左右两个区域，左边的都比中心元素小，右边的都比中心元素大；
 * 3、对中心元素左边区域（不包括中心元素）执行步骤1和2，对右边区域（不包括中心元素）也执行步骤1和2；
 * 4、不断重复步骤3；
 * <p>
 * ①以第一个关键字 K 1 为控制字，将 [K 1 ,K 2 ,…,K n ] 分成两个子区，使左区所有关键字小于等于 K 1
 * ，右区所有关键字大于等于 K 1 ，最后控制字居两个子区中间的适当位置。在子区内数据尚处于无序状态。
 * ②把左区作为一个整体，用①的步骤进行处理，右区进行相同的处理。（即递归） ③重复第①、②步，直到左区处理完毕。
 * <p>
 * 【示例】：
 * <p>
 * 初始关键字 ［49 38 65 97 76 13 27 49］ 一趟排序之后 ［27 38 13］ 49 ［76 97 65 49］ 二趟排序之后
 * ［13］ 27 ［38］ 49 ［49 65］76 ［97］ 三趟排序之后 13 27 38 49 49 ［65］76 97 最后的排序结果 13
 * 27 38 49 49 65 76 97 各趟排序之后的状态
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-26 11:12
 * @Version: 1.0
 */
public class QuickSortUtils {

    /**
     * 对指定的数组中索引从start到end之间的元素进行快速排序
     * 快速排序
     * <p>
     * ①以第一个关键字 K 1 为控制字，将 [K 1 ,K 2 ,…,K n ] 分成两个子区，使左区所有关键字小于等于 K 1
     * ，右区所有关键字大于等于 K 1 ，最后控制字居两个子区中间的适当位置。在子区内数据尚处于无序状态。
     * ②把左区作为一个整体，用①的步骤进行处理，右区进行相同的处理。（即递归） ③重复第①、②步，直到左区处理完毕。
     * <p>
     * 【示例】：
     * <p>
     * 初始关键字 ［49 38 65 97 76 13 27 49］ 一趟排序之后 ［27 38 13］ 49 ［76 97 65 49］ 二趟排序之后
     * ［13］ 27 ［38］ 49 ［49 65］76 ［97］ 三趟排序之后 13 27 38 49 49 ［65］76 97 最后的排序结果 13
     * 27 38 49 49 65 76 97 各趟排序之后的状态
     *
     * @param array 指定的数组
     * @param start 需要快速排序的数组索引起点
     * @param end   需要快速排序的数组索引终点
     */
    public static final void sort(int[] array, final int start, final int end) {
//        终止条件
        if (start >= end) {
            return;
        }
        // i相当于助手1的位置，j相当于助手2的位置
        int left = start;
        int right = end;

        // 取第1个元素为基准分区中心元素pivot
        final int pivot = array[left];

        // 表示空位的位置索引，默认为被取出的基准元素的位置
        int emptyIndex = left;

        // 如果需要排序的元素个数不止1个，就进入快速排序(只要left
        // 和right不同，就表明至少有2个数组元素需要排序)
        while (left < right) {
            // 助手2开始从右向左一个个地查找小于基准元素的元素
            while (left < right && array[right] >= pivot) {
                right--;
            }

            // 找到了，如果助手2在遇到助手1之前就找到了对应的元素，就将该元素给助手1的"空位"，right成了空位
            if (left < right) {
                array[emptyIndex] = array[right];
                emptyIndex = right;
            }

            // 助手1开始从左向右一个个地查找大于基准元素的元素
            while (left < right && array[left] <= pivot) {
                left++;
            }

            // 如果助手1在遇到助手2之前就找到了对应的元素，就将该元素给助手2的"空位"，left成了空位
            if (left < right) {
                array[emptyIndex] = array[left];
                emptyIndex = left;
            }
        }
        // 助手1和助手2相遇后会停止循环，将最初取出的基准值给最后的空位,一轮排序完毕，该位置有序
        array[emptyIndex] = pivot;

        // =====本轮快速排序完成，下面开始对左右分区进行排序，一直递归=====

//        对左边的区域排序
        sort(array, start, emptyIndex - 1);

//        对右边的区域排序
        sort(array, emptyIndex + 1, end);
    }

    public static void quickSort(int[] array, int left, int right) {
        int pivotIndex;
        if (left < right) {
            pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }

    /**
     * 快速排序的分区排序
     *
     * @param array
     * @param left
     * @param right
     * @return 中间基准数的数组下标
     */
    private static int partition(int[] array, int left, int right) {
        int pivot = array[left]; // 记住基准数
        while (left < right) {
            while (left < right && array[right] >= pivot)
                right--;
            if (left < right)
                array[left++] = array[right]; // 把右小存进左
            while (left < right && array[left] <= pivot)
                left++;
            if (left < right)
                array[right--] = array[left]; // 把左大存进右
        }
        array[left] = pivot;
        return left;
    }

}

package com.latico.commons.algorithm.sort;

/**
 * <PRE>
 * 归并排序
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-20 14:05:44
 * @Version: 1.0
 */
public class MergeSort {

    /**
     * @param arr 需要被排序的数组
     * @param start 起点
     * @param end 终点
     */
    public static void mergeSort(int[] arr, int start, int end) {
        if (start < end) {//当子序列中只有一个元素时结束递归
            int mid = (start + end) / 2;//划分子序列
            mergeSort(arr, start, mid);//对左侧子序列进行递归排序
            mergeSort(arr, mid + 1, end);//对右侧子序列进行递归排序
            merge(arr, start, mid, end);//合并
        }
    }

    //两路归并算法，两个排好序的子序列合并为一个子序列
    private static void merge(int[] arr, int left, int mid, int right) {
        int[] tmp = new int[arr.length];//辅助数组
        int p1 = left, p2 = mid + 1, k = left;//p1、p2是检测指针，k是存放指针

        while (p1 <= mid && p2 <= right) {
            if (arr[p1] <= arr[p2]) {
                tmp[k++] = arr[p1++];
            } else {
                tmp[k++] = arr[p2++];
            }
        }

        while (p1 <= mid) tmp[k++] = arr[p1++];//如果第一个序列未检测完，直接将后面所有元素加到合并的序列中
        while (p2 <= right) tmp[k++] = arr[p2++];//同上

        //复制回原素组
        for (int i = left; i <= right; i++) {
            arr[i] = tmp[i];
        }
    }


}
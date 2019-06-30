package com.latico.commons.algorithm.sort;

import org.junit.Test;

import java.util.Arrays;

public class QuickSortUtilsTest {

    @Test
    public void quickSort() {
        int[] arr = new int[]{2, 1, 3, 5, 7, 2, 7, 0, 8, 4};
        QuickSortUtils.quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void sort() {
        int[] arr = new int[]{2, 1, 3, 5, 7, 2, 7, 0, 8, 4};
        QuickSortUtils.sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void sort2() {
        int[] arr = new int[]{2, 1, 3, 5, 7, 2, 7, 0, 8, 4};
        sort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }


    public static void sort(int[] arr, final int start, final int end) {

        if (start >= end) {
            return;
        }

        int left = start;
        int right = end;

        int pivot = arr[left];
        int emptyIndex = left;

        while (left < right) {

            while (left < right && arr[right] >= pivot) {
                right--;
            }
            if (left < right) {
                arr[emptyIndex] = arr[right];
                emptyIndex = right;
            }

            while (left < right && arr[left] <= pivot) {
                left++;
            }
            if (left < right) {
                arr[emptyIndex] = arr[left];
                emptyIndex = left;
            }
        }

        arr[emptyIndex] = pivot;

        sort(arr, start, emptyIndex - 1);
        sort(arr, emptyIndex + 1, end);

    }
}
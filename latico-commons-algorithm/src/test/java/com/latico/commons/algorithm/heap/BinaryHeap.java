package com.latico.commons.algorithm.heap;

/**
 * <PRE>
 * 二叉树堆
 * <p>
 * 最大堆：根结点的键值是所有堆结点键值中最大者（不仅大于其子节点，同时大于堆中的所有节点值）。每个节点的值都>=其左右孩子（如果有的话）值的完全二叉树。
 * 最小堆：根结点的键值是所有堆结点键值中最小者（同理）。每个节点的值都<=其左右孩子值的完全二叉树。
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-21 13:52
 * @Version: 1.0
 */
public class BinaryHeap {
    private int[] data;
    private int count; //当前节点数
    private int capacity; //容量

    /**
     * 获取一个节点的左子节点在数组中的索引
     *
     * @param i
     * @return
     */
    public static int left(int i) {
        return (i + 1) * 2 - 1;
    }

    /**
     * 获取一个节点的右子节点在数组中的索引
     *
     * @param i
     * @return
     */
    public static int right(int i) {
        return (i + 1) * 2;
    }

    /**
     * 获取一个节点的父节点在数组中的索引
     *
     * @param i
     * @return
     */
    public static int parent(int i) {
        // i为根结点
        if (i == 0) {
            return -1;
        }
        return (i - 1) / 2;
    }

    public BinaryHeap(int capacity) {
        this.data = new int[capacity + 1];  //因为索引0不存节点，所以长度加一
        this.capacity = capacity;
        this.count = 0;
    }

    //将一个无序数组构造成一个最大堆：相当于堆排序
    public void MaxHeap(int[] arr, int n) {
        data = new int[n + 1];
        capacity = n;
        for (int i = 0; i < n; i++) {
            data[i + 1] = arr[i];
        }
        count = n;
        //通过for循环实现所有节点都遍历一遍，而且是从低层的最右边的叶节点的父节点一直从右到左、从下往上索引到根节点。所以，这个算是一个逆向的层次遍历
        for (int parent = count / 2; parent >= 1; parent--) {
            shiftDown(parent);
        }
    }

    //调用shiftDown只会从当前节点一直往深度走，往树叶子走而不会同层走，所以这是个深度优先遍历
    private void shiftDown(int parent) {
        int left = 2 * parent;  // 节点 left 表示 parent父节点对应的左孩子节点
        int right = 2 * parent + 1;
        int maxValueIndex = left;
        while (left <= count) {     //有左子节点
            if (right <= count && data[right] > data[left]) { // 比较左右子节点那个值更大
                maxValueIndex = right;
            }
            if (data[parent] >= data[maxValueIndex])  //拿父节点和左右子节点更大的进行比较
                break;
            //如果子节点大于父节点，则交换数据
            swap(data, parent, maxValueIndex);
            parent = maxValueIndex;       //k被赋为当前位置,为下次循环做初始化
        }
    }

    public static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

}

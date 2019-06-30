package com.latico.commons.algorithm.string;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <PRE>
 * 全字符的全排序
 * 比如给定字符串 abc
 * 输出结果为：[abc, acb, bac, bca, cab, cba]
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-30 21:23
 * @Version: 1.0
 */
public class TotalCharOrdering {
    /**
     * 时间复杂度
     */
    public static int time = 0;

    public static void order(String str){
        Set<String> set = new LinkedHashSet<>();
        recursion("", str, set);

        System.out.println(set);
        System.out.println("时间复杂度次数：" + time);
    }

    private static void recursion(String result, String str, Set<String> set){
        if (str.length() == 0) {
            set.add(result);
            return;
        }
        char[] chars = str.toCharArray();
        for (char a : chars) {

            str = "";
            for (char b : chars) {
                if (a != b) {
                    str += b;
                }
                time++;
            }
            recursion(result + a, str, set);
        }
    }
}

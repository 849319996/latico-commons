package com.latico.commons.algorithm.string;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <PRE>
 * 指定长度的随机任意全排序
 * 比如给定字符串 abc， 指定随意长度2
 * 输出结果为：[aa, ab, ac, ba, bb, bc, ca, cb, cc]
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-30 21:23
 * @Version: 1.0
 */
public class CharRandom {
    /**
     * 时间复杂度
     */
    public static int time = 0;

    public static void order(String str, int maxLen){
        Set<String> set = new LinkedHashSet<>();
        recursion("", str, set, maxLen);

        System.out.println(set);
        System.out.println("时间复杂度次数：" + time);
    }

    private static void recursion(String result, String str, Set<String> set, int maxLen){
        if (result.length() == maxLen) {
            set.add(result);
            return;
        }
        char[] chars = str.toCharArray();
        for (char a : chars) {
            time++;
            recursion(result + a, str, set, maxLen);
        }
    }
}

package com.latico.commons.algorithm.string;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <PRE>
 * 字符的所有组合
 * 比如给定字符串 abc
 * 输出结果为：[a, ab, abc, ac, b, bc, c]
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-30 21:23
 * @Version: 1.0
 */
public class CharTotalGroup {
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
        if (!"".endsWith(result)) {
            set.add(result);
        }
        char[] chars = str.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char a = chars[i];
            time++;
            recursion(result + a, str.substring(i + 1), set);
        }

    }
}

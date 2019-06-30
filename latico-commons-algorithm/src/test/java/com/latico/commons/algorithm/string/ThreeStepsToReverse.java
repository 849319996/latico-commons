package com.latico.commons.algorithm.string;

import java.util.Arrays;

/**
 * <PRE>
 * 字符串旋转
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-30 18:29
 * @Version: 1.0
 */
public class ThreeStepsToReverse {

    /**
     * 时间复杂度
     */
    public static int time = 0;
    /**
     * 对于给定字符串 abcdef，指定前面几个字符顺序不变的移到后面，转换成 defabc，
     *
     * 三步反转法:
     * 1、分成2半， abc  def， 分别单独反转  cba  fed；
     * 2、组合在一起后  cbafed；
     * 3、反转整体： defabc
     * @param str
     * @param count
     * @return
     */
    public static String threeStepsToReverse(String str, int count) {
        int length = str.length();
        char[] chars = str.toCharArray();

        reverse(chars, 0, count - 1);
        reverse(chars, count, length - 1);
        reverse(chars, 0, length - 1);

        System.out.println("时间复杂度次数:" + time);
        return getString(chars);
    }

    public static void reverse(char[] chars, int left, int right) {
        while (left < right) {
            char c = chars[left];
            chars[left] = chars[right];
            chars[right] = c;

            left++;
            right--;

            time++;
        }

        System.out.println("一轮结束：" + Arrays.toString(chars));

    }


    private static String getString(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}

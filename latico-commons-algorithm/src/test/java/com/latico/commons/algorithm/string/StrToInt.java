package com.latico.commons.algorithm.string;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-31 0:50
 * @Version: 1.0
 */
public class StrToInt {
    /**
     * 时间复杂度
     */
    public static int time = 0;

    public static void convert(String str) {
        char[] chars = str.toCharArray();

        int count = 1;
        int sum = 0;
        for (char aChar : chars) {
            sum *= count;
            int i = aChar - '0';
            sum += i;
            count = 10;

            time++;
        }

        System.out.println(sum);
        System.out.println("时间复杂度次数：" + time);

    }
}

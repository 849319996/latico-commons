package com.latico.commons.algorithm.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ThreeStepsToReverseTest {

    @Test
    public void threeStepsToReverse() {
        String str = "abcdef123456";
        ThreeStepsToReverse.threeStepsToReverse(str, 3);
    }

    @Test
    public void reverse() {
        String str = "abcdef";
        char[] chars = str.toCharArray();
        ThreeStepsToReverse.reverse(chars, 0, 2);
        System.out.println(Arrays.toString(chars));


    }

    @Test
    public void name(){
        String str = "abc";
        Set<String> set = new LinkedHashSet<>();
        name2("", str, set);
        System.out.println(set);
    }

    public void name2(String result, String str, Set<String> set){
        if (str.length() == 0) {
            set.add(result);
        }
        char[] chars = str.toCharArray();
        for (char a : chars) {

            str = "";
            for (char b : chars) {
                if (a != b) {
                    str += b;
                }
            }
            name2(result + a, str, set);
        }
    }
}
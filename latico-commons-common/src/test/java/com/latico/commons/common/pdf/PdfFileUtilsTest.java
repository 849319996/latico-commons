package com.latico.commons.common.pdf;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 制作PDF的书签功能
 */
public class PdfFileUtilsTest {

    /**
     * 增加页码
     */
    @Test
    public void test1() throws IOException {
        int addNum = 19;
        String file = "pdf/hadoop实战第二版目录书签.txt";
        makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test2() throws IOException {
        int addNum = 19;
        String file = "pdf/精通Hadoop目录书签.txt";
        makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test() throws IOException {
        int addNum = 19;
        String file = "pdf/test.txt";
        makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test3() throws IOException {
        int addNum = 19;
        String file = "pdf/test2.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    /**
     * 使书的标签的页码递增
     * @param resourceFile   书签文件
     * @param addNum 使页码递增
     * @throws IOException
     */
    private void makeBookMarkePageAdd(String resourceFile, int addNum) throws IOException {
        String[] lines = IOUtils.resourceToString(resourceFile).split("[\r\n]+");
        Pattern pat = Pattern.compile("(.+?)[\\s\t　]+(\\d+)$");
        Pattern pat1 = Pattern.compile("(.+?)[\\s\t　]+(.+?)[\\s\t　]+(\\d+)$");
        //2级标题
        Pattern pat2 = Pattern.compile("(\\d+\\.\\d+)[\\s\t　]+(.+?)[\\s\t　]+(\\d+)$");
        //3级标题
        Pattern pat3 = Pattern.compile("(\\d+\\.\\d+\\.\\d+)[\\s\t　]+(.+?)[\\s\t　]+(\\d+)$");
        Pattern pat4 = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)[\\s\t　]+(.+?)[\\s\t　]+(\\d+)$");
        for (String line : lines) {
            Matcher matcher = pat4.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3))  + addNum;
                System.out.println("\t" + "\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat3.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3))  + addNum;
                System.out.println("\t" + "\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat2.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3))  + addNum;
                System.out.println("\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }

            matcher = pat1.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3))  + addNum;
                System.out.println(group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                int group2 = Integer.parseInt(matcher.group(2))  + addNum;
                System.out.println(group1+ "　" + group2);
                continue;
            }

            System.out.println("匹配失败:" + line);

        }
    }
}
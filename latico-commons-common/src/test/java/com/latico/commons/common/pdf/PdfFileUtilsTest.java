package com.latico.commons.common.pdf;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 制作PDF的书签功能
 */
public class PdfFileUtilsTest {
    /**
     * 使书的标签的页码递增
     * @param resourceFile   书签文件
     * @param addNum 使页码递增
     * @throws IOException
     */
    private void makeBookMarkePageAdd(String resourceFile, int addNum) throws IOException {
        String[] lines = IOUtils.resourceToString(resourceFile).split("[\r\n]+");
        Pattern pat = Pattern.compile("^　*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat1 = Pattern.compile("^　*(第\\d+[章部分]+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        //2级标题
        Pattern pat2 = Pattern.compile("^　*(\\S+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        //3级标题
        Pattern pat3 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat4 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat5 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern dirPat = Pattern.compile("^　*(目\\s*录|作\\s*者|前\\s*言|序|译者序|译者简介|作者简介|评审者简介)\\s.*");
        List<String> results = new ArrayList<>();
        int count = 0;
        for (String line : lines) {
            line = line.trim();
            if ("".equals(line)) {
                continue;
            }
            count++;
            Matcher matcher = null;

            matcher = pat5.matcher(line);
            boolean isAdd = false;
            if(dirPat.matcher(line).find() && count <= 10){
                isAdd = false;
            }else{
                isAdd = true;
            }
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3));

                if(isAdd){
                    group3 = group3 + addNum;
                }
                results.add("\t" + "\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }

            matcher = pat4.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3));
                if(isAdd){
                    group3 = group3 + addNum;
                }
                results.add("\t" + "\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat3.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3));
                if(isAdd){
                    group3 = group3 + addNum;
                }
                results.add("\t" + "\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat2.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3));
                if(isAdd){
                    group3 = group3 + addNum;
                }
                results.add("\t" + group1+ "　" + group2+ "\t" + group3);
                continue;
            }

            matcher = pat1.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                String group2 = matcher.group(2);
                int group3 = Integer.parseInt(matcher.group(3));
                if(isAdd){
                    group3 = group3 + addNum;
                }
                results.add(group1+ "　" + group2+ "\t" + group3);
                continue;
            }
            matcher = pat.matcher(line);
            if (matcher.find()) {
                String group1 = matcher.group(1);
                int group2 = Integer.parseInt(matcher.group(2));
                if(isAdd){
                    group2 = group2 + addNum;
                }
                results.add(group1+ "\t" + group2);
                continue;
            }

            results.add("匹配失败:" + line);

        }

        File file = new File(resourceFile);
        FileUtils.writeLines(new File("src/test/resources/pdf/result/" + file.getName()), results);
    }

    @Test
    public void test() throws IOException {
        int addNum = 19;
        String dir = "pdf/src/";
        String file = dir + "test.txt";
        makeBookMarkePageAdd(file, addNum);
    }
    /**
     * 增加页码
     */
    @Test
    public void test1() throws IOException {
        int addNum = 19;
        String dir = "pdf/src/";
        String file = dir + "hadoop实战第二版目录书签.txt";
        makeBookMarkePageAdd(file, addNum);
    }
    @Test
    public void test2() throws IOException {
        int addNum = 19;
        String dir = "pdf/src/";
        String file = dir + "精通Hadoop目录书签.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test3() throws IOException {
        int addNum = 19;
        String dir = "pdf/src/";
        String file = dir + "test2.txt";
        makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test4() throws IOException {
        int addNum = 15;
        String dir = "pdf/src/";
        String file = dir + "深入理解hadoop第二版.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test5() throws IOException {
        int addNum = 14;
        String dir = "pdf/src/";
        String file = dir + "Java性能优化权威指南.txt";
        makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test6() throws IOException {
        int addNum = 21;
        String dir = "pdf/src/";
        String file = dir + "Java软件结构与数据结构（第4版）.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test7() throws IOException {
        int addNum = 18;
        String dir = "pdf/src/";
        String file = dir + "深入理解Elasticsearch（原书第2版）.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test8() throws IOException {
        int addNum = 19;
        String dir = "pdf/src/";
        String file = dir + "Kafka权威指南.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test9() throws IOException {
        int addNum = 13;
        String dir = "pdf/src/";
        String file = dir + "MyBatis技术内幕.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test10() throws IOException {
        int addNum = 0;
        String dir = "pdf/src/";
        String file = dir + "深入浅出Netty.txt";
        makeBookMarkePageAdd(file, addNum);
    }


    @Test
    public void test11() throws IOException {
        int addNum = 22;
        String dir = "pdf/src/";
        String file = dir + "[实战Nginx 取代Apache的高性能Web服务器].张宴.扫描版.txt";
        makeBookMarkePageAdd(file, addNum);
    }

    @Test
    public void test12() throws IOException {
        int addNum = 0;
        String dir = "pdf/src/";
        String file = dir + "Hadoop实战第2版-陆嘉恒.txt";
        makeBookMarkePageAdd(file, addNum);
    }


}
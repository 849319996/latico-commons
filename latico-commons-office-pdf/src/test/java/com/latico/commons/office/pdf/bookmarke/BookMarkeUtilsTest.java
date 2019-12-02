package com.latico.commons.office.pdf.bookmarke;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-11-27 16:57
 * @Version: 1.0
 */
public class BookMarkeUtilsTest {
    /**
     * 使书的标签的页码递增
     * @param resourceFile   书签文件
     * @param addNum 使页码递增
     * @throws IOException
     */
    public static void makeBookMarkePageAdd(String resourceFile, int addNum) throws Exception {
        String[] lines = IOUtils.resourceToString(resourceFile).split("[\r\n]+");
        Pattern pat = Pattern.compile("^　*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat1 = Pattern.compile("^　*(第\\d+[章部分]+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        //2级标题
        Pattern pat2 = Pattern.compile("^　*(\\S+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        //3级标题
        Pattern pat3 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat4 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern pat5 = Pattern.compile("^　*(\\S+\\.\\d+\\.\\d+\\.\\d+\\.\\d+)[\\s\t　]*(.+?)[\\s\t　]*(\\d+)$");
        Pattern dirPat = Pattern.compile("^　*(目\\s*录|作\\s*者|前\\s*言|序|译者序|译者简介|作者简介|作者介绍|评审者简介|第\\S版前言|使用说明|撰稿人|内容摘要)\\s.*");
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

            System.out.println("匹配失败:" + line);

            if (line.matches(".+?\\d")) {
                results.add(line);
            } else {
                results.add(line + "\t1");
            }


        }

        File file = new File(resourceFile);
        FileUtils.writeLines(new File("src/test/resources/bookmarke/result/" + file.getName()), results);
    }

}

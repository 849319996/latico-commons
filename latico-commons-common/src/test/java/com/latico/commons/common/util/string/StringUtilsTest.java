package com.latico.commons.common.util.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringUtilsTest {

    @Test
    public void test(){
        System.out.println(StringUtils.deleteWhitespace("abc jj jago \njajg\njajgla"));
    }

    @Test
    public void test1(){
        System.out.println(StringUtils.isNotBlank("abc"));
    }

    @Test
    public void replaceByPlaceholder() {
        System.out.println(StringUtils.replaceByPlaceholder("ajga{}jagj{}jajg{}", "{}", "1", "2", 3, 4));

    }

    @Test
    public void substringAfter(){
        System.out.println(StringUtils.substringAfterLast("httpclient://ws.webxml.com.cn/WebServices/WeatherWS.asmx?wsdl", "/"));
    }

    @Test
    public void splitBySpecialLengths(){
        int[] lens = new int[]{"1192  ".length(), "VLAN1192     ".length(),
                "gei_7/11                                ".length(), "gei_7/11    ".length()};
        String[] strings = StringUtils.splitBySpecialLengths("1192  VLAN1192     gei_7/11                                gei_7/11", lens);

        for (String string : strings) {
            System.out.println("|" + string + "|");
        }
    }
    @Test
    public void parseStrToTable() {
        String data = "9008_guangtongdasha-IPRAN#show vlan\n" +
                "VLAN  Name         PvidPorts           UntagPorts          TagPorts          \n" +
                "--------------------------------------------------------------------------------\n" +
                "1     VLAN0001     xgei_1/2-3,xgei_2/2                     \n" +
                "                   -3,xgei_3/3-4,xgei_                     \n" +
                "                   4/1,xgei_4/3,gei_7/                     \n" +
                "                   2-4,gei_7/6,gei_7/1                     \n" +
                "                   0,gei_7/12-24                           \n" +
                "1180  VLAN1180     xgei_3/1                                xgei_3/1\n" +
                "1181  VLAN1181     xgei_1/4                                xgei_1/4\n" +
                "1182  VLAN1182     gei_7/1                                 gei_7/1\n" +
                "1183  VLAN1183     gei_7/5                                 gei_7/5\n" +
                "1184  VLAN1184     xgei_4/4                                xgei_4/4\n" +
                "1185  VLAN1185     xgei_2/4                                xgei_2/4\n" +
                "1186  VLAN1186     gei_7/7                                 gei_7/7\n" +
                "1187  VLAN1187     gei_7/8                                 gei_7/8\n" +
                "1188  VLAN1188     gei_7/9                                 gei_7/9\n" +
                "1192  VLAN1192     gei_7/11                                gei_7/11\n" +
                "1216  VLAN1216                                             gei_7/15\n" +
                "1338  VLAN1338                                             \n" +
                "2352  VLAN2352                                             xgei_2/1,xgei_4/2,s\n" +
                "                                                           martgroup2\n" +
                "2368  VLAN2368                                             gei_7/10\n" +
                "2369  VLAN2369                                             gei_7/22\n" +
                "2370  VLAN2370                                             gei_7/2\n" +
                "2936  VLAN2936                                             xgei_1/1,xgei_3/2,s\n" +
                "                                                           martgroup1\n" +
                "2937  VLAN2937                                             xgei_2/1,xgei_4/2,s\n" +
                "                                                           martgroup2\n" +
                "4072  VLAN4072     xgei_1/1,xgei_3/2,s                     xgei_1/1,xgei_3/2,s\n" +
                "                   martgroup1                              martgroup1\n" +
                "4073  VLAN4073     xgei_2/1,xgei_4/2,s                     xgei_2/1,xgei_4/2,s\n" +
                "                   martgroup2                              martgroup2\n" +
                "                   \n" +
                "                   \n" +
                "9008_guangtongdasha-IPRAN#show lacp counters";


        int[] rowLens = new int[]{"VLAN  ".length(), "Name         ".length(), "PvidPorts           ".length(), "UntagPorts          ".length(), "TagPorts          ".length()};
        List<String[]> lines = StringUtils.parseStrToTable(data, "^\\d+ + VLAN\\d+ +.+", "\\S+?#.*", rowLens);
        for (String[] line : lines) {
            System.out.println("'"+ Arrays.toString(line)+"'");
        }

    }

    @Test
    public void equals() {
        Object obj1 = "2";
        Object obj2 = 2;
        System.out.println(StringUtils.equals(obj1, obj2));

        obj1 = null;
        obj2 = null;
        System.out.println(StringUtils.equals(obj1, obj2));


        obj1 = "bad";
        obj2 = null;
        System.out.println(StringUtils.equals(obj1, obj2));

        obj1 = "bad";
        obj2 = "bad";
        System.out.println(StringUtils.equals(obj1, obj2));
    }
}
package com.latico.commons.common.util.collections;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapUtilsTest {

    @Test
    public void toMap(){
        System.out.println(MapUtils.strToMap("abc=1;bcd=2;cdf=3", ";", "="));
    }
    @Test
    public void mapToStr(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("abc", "1");
        map.put("abc2", "2");
        map.put("abc3", "3");
        System.out.println(MapUtils.mapToStr(map, ";", " = "));
    }

    @Test
    public void getValue() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("abc", "1");
        map.put("abc2", "2");
        map.put("abc3", "3");
        System.out.println(MapUtils.getValue(map, "abc", "jg"));
    }
}
package com.latico.commons.common.util.json;

import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FastJsonUtilsTest {

    @Test
    public void jsonToObj() {
        Map<String, String> map = new HashMap<>();
        map.put("abc", "ajgj");
        map.put("abc2", "ajgj2");
        map.put("abc3", "ajgj3");
        String json = FastJsonUtils.objToJson(map);
        map = FastJsonUtils.jsonToObj(json, new TypeReference<Map<String, String>>(){});
        System.out.println(map);
    }
}
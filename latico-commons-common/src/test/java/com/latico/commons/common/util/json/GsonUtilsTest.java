package com.latico.commons.common.util.json;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class GsonUtilsTest {

    @Test
    public void objToJson() {
    }

    @Test
    public void jsonToGenericity() {
    }

    @Test
    public void jsonToObj() {
    }


    @Test
    public void jsonToObj2() {
        Map<String, String> map = new HashMap<>();
        map.put("abc", "ajgj");
        map.put("abc2", "ajgj2");
        map.put("abc3", "ajgj3");
        String json = GsonUtils.objToJson(map);
        map = GsonUtils.jsonToObj(json, new TypeToken<Map<String, String>>(){});
        System.out.println(map);
    }

}
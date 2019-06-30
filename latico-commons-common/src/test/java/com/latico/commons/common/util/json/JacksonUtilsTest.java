package com.latico.commons.common.util.json;

import com.latico.commons.common.util.compare.CompareBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtilsTest {

    @Test
    public void jsonToObj() {
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("abc1");
        list.add("abc2");

        String json = JacksonUtils.objToJson(list);
        System.out.println(json);
        list = JacksonUtils.jsonToObj(json, new TypeReference<List<String>>() {
        });

        System.out.println(list);

    }

    @Test
    public void jsonToObj2() {
        List<List<CompareBean>> lists = new ArrayList<>();
        List<CompareBean> list = new ArrayList<>();
        CompareBean obj = new CompareBean();
        obj.setAge(10);
        obj.setName("name1");
        obj.setNickName("nickName1");

        obj = new CompareBean();
        obj.setAge(101);
        obj.setName("name2");
        obj.setNickName("nickName2");
        list.add(obj);

        list.add(obj);
        lists.add(list);


        String json = JacksonUtils.objToJson(lists);
        System.out.println(json);
        lists = JacksonUtils.jsonToObj(json, new TypeReference<List<List<CompareBean>>>() {
        });

        System.out.println(lists);

    }


    @Test
    public void map() {
        Map<String, String> map = new HashMap<>();
        map.put("abc", "ajgj");
        map.put("abc2", "ajgj2");
        map.put("abc3", "ajgj3");
        String json = JacksonUtils.objToJson(map);

        map = JacksonUtils.jsonToObj(json, new TypeReference<Map<String, String>>() {
        });
        System.out.println(map);
    }

}
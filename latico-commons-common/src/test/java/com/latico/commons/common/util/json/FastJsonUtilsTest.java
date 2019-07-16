package com.latico.commons.common.util.json;

import com.alibaba.fastjson.TypeReference;
import com.latico.commons.common.util.json.bean.DataBean;
import com.latico.commons.common.util.json.bean.JsonBean;
import com.latico.commons.common.util.json.bean.RespBean;
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
        map = FastJsonUtils.jsonToObj(json, new TypeReference<Map<String, String>>() {
        });
        System.out.println(map);
    }

    /**
     *
     */
    @Test
    public void test1() {
        String json = "{\"code\":0,\"data\":[{\"code\":1,\"content\":\"123\",\"resp\":{\"errcode\":0,\"errmsg\":\"134\",\"msgid\":0}}],\"msg\":\"成功\"}";

        JsonBean jsonBean = FastJsonUtils.jsonToObj(json, JsonBean.class);
        System.out.println(jsonBean);

    }

    /**
     *
     */
    @Test
    public void test2() {
        JsonBean<DataBean> jsonBean = new JsonBean();

        DataBean dataBean = new DataBean();

        dataBean.setCode(1);
        dataBean.setContent("123");
        RespBean resp = new RespBean();
        resp.setErrmsg("134");
        dataBean.setResp(resp);

        jsonBean.setCode(0);
        jsonBean.setMsg("成功");
        jsonBean.setData(new DataBean[]{dataBean});

        System.out.println(FastJsonUtils.objToJson(jsonBean));

    }
}
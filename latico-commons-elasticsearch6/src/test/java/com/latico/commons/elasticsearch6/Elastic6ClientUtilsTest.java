package com.latico.commons.elasticsearch6;

import com.latico.commons.common.util.json.JacksonUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Elastic6ClientUtilsTest {

    @Test
    public void getClient() {
    }

    @Test
    public void close() {
    }

    @Test
    public void addToIndexAutoId() {
        Map<String, Object> source = new HashMap<String, Object>();
        source.put("code", "01");
        source.put("name", "科技");
        source.put("info", "中共十九大");
        source.put("content", "中共十九大");
        source.put("my_title", "我的标题12323abcd");
        source.put("you_title", "你的标题1efg");
        source.put("isDelete", true);
        source.put("age", 16);
        Map<String, Object> source2 = new HashMap<String, Object>();
        source2.put("code", "02");
        source2.put("name", "新闻");
        source2.put("info", "中国周恩来");
        source2.put("content", "中国周恩来");
        source2.put("my_title", "我的标题235325abcd");
        source2.put("you_title", "你的标题346565efg");
        source2.put("isDelete", false);
        source2.put("age", 17);
        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            IndexResponse indexResponse = Elastic6ClientUtils.addOneToIndexAutoId(client, "test", "map", source, source2);
            System.out.println(indexResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }

    @Test
    public void isCreated() {
    }

    @Test
    public void isIndexExists() {
    }

    @Test
    public void createIndex() {
        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            boolean test = Elastic6ClientUtils.createIndex(client, "test", "map", 5, 1, 10);
            System.out.println(test);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }

    @Test
    public void getClientLocalDefault() {
    }

    @Test
    public void getClientLocal() {
    }

    @Test
    public void addOneToIndexAutoId() {
    }

    @Test
    public void addOneJsonToIndex() {
        Map<String, Object> source3 = new HashMap<String, Object>();
        source3.put("code", "03");
        source3.put("name", "科学技术");
        source3.put("info", "用友建筑大武汉");
        source3.put("content", "用友建筑大武汉");
        source3.put("my_title", "我的标题65845abcd");
        source3.put("you_title", "你的标题237678efg");
        source3.put("isDelete", false);
        source3.put("age", 18);

        String json = JacksonUtils.objToJson(source3);

        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            IndexResponse indexResponse = Elastic6ClientUtils.addOneJsonToIndex(client, "test", "map", "source3", json);
            System.out.println(indexResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }
    @Test
    public void addOneMapToIndex() {
        Map<String, Object> source4 = new HashMap<String, Object>();
        source4.put("code", "04");
        source4.put("name", "快手视频");
        source4.put("info", "中国特色社会主义");
        source4.put("content", "中国特色社会主义");
        source4.put("my_title", "我的标题6789dfgf");
        source4.put("you_title", "你的标题67458sdfdf");
        source4.put("isDelete", true);
        source4.put("age", 19);

        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            IndexResponse indexResponse = Elastic6ClientUtils.addOneMapToIndex(client, "test", "map", "source4", source4);
            System.out.println(indexResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }

    @Test
    public void updateById() {
        Map<String, Object> source4 = new HashMap<String, Object>();
        source4.put("code", "042");
        source4.put("name", "快手视频3");
        source4.put("info", "中国特色社会主义2");
        source4.put("content", "中国特色社会主义2");
        source4.put("my_title", "我的标题6789dfgf2");
        source4.put("you_title", "你的标题67458sdfdf2");
        source4.put("isDelete", true);
        source4.put("age", 19);

        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            UpdateResponse indexResponse = Elastic6ClientUtils.updateById(client, "test", "map", "source4", source4);
            System.out.println(indexResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }

    @Test
    public void deleteById() {
        TransportClient client = null;
        try {
            client = Elastic6ClientUtils.getClientLocalDefault();
            boolean indexResponse = Elastic6ClientUtils.deleteById(client, "test", "map", "source4");
            System.out.println(indexResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Elastic6ClientUtils.close(client);
        }
    }
}
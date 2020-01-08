package com.latico.commons.elasticsearch6.test2;

import com.latico.commons.elasticsearch6.Es6Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class ElasticJavaClient {

    public TransportClient getClient() throws Exception {
        return Es6Utils.createClientLocalDefault();
    }
    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        TransportClient client = null;
        try {
            client = getClient();
            CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate("product");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 5); // 分片数
            settings.put("number_of_replicas", 1); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);

            builder.addMapping("product_plan", getIndexSource());

            CreateIndexResponse res = builder.get();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();

    }

    @Test
    public void createIndex2() {
        TransportClient client = null;
        try {
            client = getClient();
            CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate("product");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 5); // 分片数
            settings.put("number_of_replicas", 1); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);

            builder.addMapping("product_plan");

            CreateIndexResponse res = builder.get();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();

    }
    /**
     * 5.*之后，把string字段设置为了过时字段，引入text，keyword字段
     *
     * keyword：存储数据时候，不会分词建立索引
     * text：存储数据时候，会自动分词，并生成索引（这是很智能的，但在有些字段里面是没用的，所以对于有些字段使用text则浪费了空间）。
     *
     * 如果在添加分词器的字段上，把type设置为keyword，则创建索引会失败
     *
     * private String productPlanId;
     *     private String id;
     *     private String name;
     *     private String parentId;
     */
    public XContentBuilder getIndexSource() throws IOException {
        XContentBuilder source = XContentFactory.jsonBuilder().startObject().startObject("product_plan")
                .startObject("properties")
                // code字段
                .startObject("productPlanId").field("type", "keyword").field("index", true).endObject()
                // 名称字段
                .startObject("id").field("type", "keyword").field("store", true).field("index", true).endObject()
                // 信息字段
                .startObject("name").field("type", "text").field("store", true).field("index", true).endObject()
                .startObject("parentId").field("type", "keyword").field("store", true).field("index", true).endObject()

                .endObject().endObject().endObject();
        return source;
    }
    /**
     * 添加一个文档
     *
     * @throws JsonProcessingException
     */
    @Test
    public void addIndex() throws Exception {
        TransportClient client = getClient();
        List<Category> categorys = new ArrayList<>();
        Category category1 = new Category("111", "123", "意外险", null);
        Category category2 = new Category("111", "126", "财产险", "0");
        Collections.addAll(categorys, category1, category2);
        ProductPlan plan = new ProductPlan("001", "成人综合保险", "全面保障你的财产安全", 3300, categorys, "111", null);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(plan);
        IndexResponse response = client.prepareIndex("product", "product_plan").setSource(json, XContentType.JSON).get();
        System.out.println(response.status().getStatus());
        //创建成功 反会的状态码是201
        if (response.status().getStatus() == 201) {
            System.out.println(response.getId());
        }
        client.close();
    }

    /**
     * 查询一个文档
     *
     * @throws Exception
     */
    @Test
    public void getIndex() throws Exception {
        TransportClient client = getClient();
        GetResponse response = client.prepareGet("product", "product_plan", "AWG4BHScpkaDOYy0S6LV").get();
        System.out.println("1： \n " + response.getSource());
        //得到的是Map类型的结果
        Map<String, Object> map = response.getSource();
        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        ObjectMapper mapper = new ObjectMapper();
        //得到Json类型的结果 可以转成对象
        ProductPlan plan = mapper.readValue(response.getSourceAsString(), ProductPlan.class);
        System.out.println(plan.getCategorys() + "\n " + plan.getId());
        client.close();
    }

    /**
     * 删除一个文档
     *
     */
    @Test
    public void deleteById() throws Exception {
        TransportClient client = getClient();
        DeleteResponse response = client.prepareDelete("product", "product_plan", "AWG4AgRIWB5UoIZiLx5h").get();
        if (response.status().getStatus() == 200) {
            System.out.println("删除成功");
        }
        client.close();
    }


    /**
     * 删除一个文档
     *
     */
    @Test
    public void deleteIndex() throws Exception {
        TransportClient client = getClient();
        System.out.println(Es6Utils.deleteIndex(client, "product"));
    }

    /**
     * 更新
     *
     * @throws Exception
     */
    @Test
    public void updateIndex() throws Exception {
        TransportClient client = getClient();
        //方案一 使用对象进行更新的话 如果对象中值为空的属性 都会被更新为null 数值更新为0
        /* ProductPlan plan=new ProductPlan();
           plan.setName("成人");
           ObjectMapper mapper =new ObjectMapper();
           String json=mapper.writeValueAsString(plan);
         */
        //方案二 开始 更新需要更新的属性
        XContentBuilder json = XContentFactory.jsonBuilder();
        json.startObject().field("id", "005").field("name", "成人意外保险").field("price", 6000).endObject();
        //方案二 结束 
        UpdateRequest updateRequest = new UpdateRequest("product", "product_plan", "AWG4A4gypkaDOYy0S6LU").doc(json);

        UpdateResponse response = client.update(updateRequest).get();
        if (response.status().getStatus() == 200) {
            System.out.println("更新成功");
        }
        client.close();
    }

}

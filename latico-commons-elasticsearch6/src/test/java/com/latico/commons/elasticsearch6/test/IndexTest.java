package com.latico.commons.elasticsearch6.test;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引操作测试
 * 
 * @author xuwenjin
 */
public class IndexTest extends AbstractJunitTest {

    /**
     * 判断是否存在该索引
     * 
     * @param indexName
     *            索引名称
     * @return
     */
    public boolean isIndexExists(String indexName) {
        IndicesExistsRequestBuilder builder = client.admin().indices().prepareExists(indexName);
        IndicesExistsResponse res = builder.get();
        return res.isExists();
    }

    /**
     * 5.*之后，把string字段设置为了过时字段，引入text，keyword字段
     * 
     * keyword：存储数据时候，不会分词建立索引
     * text：存储数据时候，会自动分词，并生成索引（这是很智能的，但在有些字段里面是没用的，所以对于有些字段使用text则浪费了空间）。
     *
     * 如果在添加分词器的字段上，把type设置为keyword，则创建索引会失败
     */
    public XContentBuilder getIndexSource() throws IOException {
        XContentBuilder source = XContentFactory.jsonBuilder().startObject().startObject("test_type2")
                .startObject("properties")
                // code字段
                .startObject("code").field("type", "text").field("index", true).field("fielddata", true).endObject()
                // 名称字段
                .startObject("name").field("type", "keyword").field("store", false).field("index", true).endObject()
                // 信息字段
                .startObject("info").field("type", "keyword").field("store", false).field("index", true).endObject()
                // 主要内容字段
                .startObject("content").field("type", "text").field("store", true).field("index", true).field("analyzer", "ik_max_word").endObject()
                .startObject("my_title").field("type", "keyword").field("store", true).field("index", true).endObject()
                .startObject("you_title").field("type", "keyword").field("store", true).field("index", true).endObject()
                .startObject("isDelete").field("type", "boolean").field("store", true).field("index", true).endObject()
                .startObject("age").field("type", "long").field("store", true).field("index", true).endObject()
                
                .endObject().endObject().endObject();
        return source;
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        try {
            if (isIndexExists("test_index2")) {
                logger.info("索引对象已经存在，无法创建！");
                return;
            }
            CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate("test_index2");
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();
            settings.put("number_of_shards", 5); // 分片数
            settings.put("number_of_replicas", 1); // 副本数
            settings.put("refresh_interval", "10s"); // 刷新间隔
            builder.setSettings(settings);

            builder.addMapping("test_type2", getIndexSource());

            CreateIndexResponse res = builder.get();
            logger.info(res.isAcknowledged() ? "索引创建成功！" : "索引创建失败！");
        } catch (Exception e) {
            logger.error("创建索引失败！", e);
        }
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        try {
            if (!isIndexExists("test_index2")) {
                logger.info("索引对象已经不存在，无法删除！");
                return;
            }
            DeleteIndexRequestBuilder builder = client.admin().indices().prepareDelete("test_index2");
            AcknowledgedResponse res = builder.get();
            logger.info(res.isAcknowledged() ? "删除索引成功！" : "删除索引失败！");
        } catch (Exception e) {
            logger.error("删除索引失败！", e);
        }
    }

}
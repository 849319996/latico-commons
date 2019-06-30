package com.latico.commons.elasticsearch6.test;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

/**
 * 简单查询数据、删除数据
 * 
 * @author xuwenjin
 */
public class SimpleQueryTest extends AbstractJunitTest {

    private static final String INDEX = "test_index1"; // 索引
    private static final String TYPE = "test_type1"; // 类型

    @Test
    public void queryObject() {
        try {
            GetResponse res = client.prepareGet(INDEX, TYPE, "1").get();
            if (res.isExists()) {
                logger.info("根据ID查询到数据，主要内容：" + res.getSource().get("content"));
            } else {
                logger.info("根据ID未查询到数据！");
            }
        } catch (Exception e) {
            logger.error("根据ID查询记录失败！", e);
        }
    }

    @Test
    public void queryList() {
        try {
            String key = "周恩来";
            QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(key, "name", "content");

            SearchResponse res = client.prepareSearch().setIndices(INDEX).setTypes(TYPE).setQuery(queryBuilder).get();

            logger.info("查询到的总记录个数为：" + res.getHits().getTotalHits());
            for (int i = 0; i < res.getHits().getTotalHits(); i++) {
                logger.info("第" + (i + 1) + "条记录主要内容为：" + res.getHits().getAt(i).getSourceAsMap().get("content"));
            }
        } catch (Exception e) {
            logger.error("查询列表失败！", e);
        }
    }

    @Test
    public void deleteData() {
        try {
            DeleteResponse res = client.prepareDelete(INDEX, TYPE, "1").get();

            logger.info("删除动作执行状态：" + res.status());
        } catch (Exception e) {
            logger.error("删除数据失败！", e);
        }
    }

}
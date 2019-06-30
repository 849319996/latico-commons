package com.latico.commons.elasticsearch6;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-03-21 17:04
 * @Version: 1.0
 */
public class Elastic6ClientUtils {
    private static final Logger LOG = LoggerFactory.getLogger(Elastic6ClientUtils.class);

    /**
     * elasticsearch服务器的默认集群名字是elasticsearch，客户端连接端口默认是9300
     * @return
     */
    public static TransportClient getClientLocalDefault() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        TransportClient client = new PreBuiltTransportClient(settings);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), 9300);
        client.addTransportAddress(transportAddress);
        return client;
    }

    /**
     * @param clusterName 集群名称
     * @return
     */
    public static TransportClient getClientLocal(String clusterName) throws Exception {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("localhost"), 9300);
        client.addTransportAddress(transportAddress);
        return client;
    }

    /**
     * @param clusterName 集群名称
     * @param hostInfos   主机信息，如果集群有多台机器，那么都传进来，key是主机名或者ID，value是端口
     * @return
     */
    public static TransportClient getClient(String clusterName, Map<String, Integer> hostInfos) throws Exception {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(settings);
        for (Map.Entry<String, Integer> entry : hostInfos.entrySet()) {
            TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(entry.getKey()), entry.getValue());
            client.addTransportAddress(transportAddress);
        }
        return client;
    }

    /**
     * @param client
     */
    public static void close(TransportClient client) {
        if (client != null) {
            client.close();
        }
    }

    /**
     * 添加一个文档
     * 自动生成文档ID
     * @param client
     * @param index
     * @param type
     * @param source
     * @return
     */
    public static IndexResponse addOneToIndexAutoId(TransportClient client, String index, String type, Object... source) {
        IndexResponse response = client.prepareIndex(index, type).setSource(source).get();
        return response;
    }

    /**
     * 添加一个文档
     * 自动生成文档ID
     * @param client
     * @param index
     * @param type
     * @param source
     * @return
     */
    public static IndexResponse addOneMapToIndexAutoId(TransportClient client, String index, String type, Map<String, ?> source) {
        IndexResponse response = client.prepareIndex(index, type).setSource(source).get();
        return response;
    }


    /**
     * @param client
     * @param index
     * @param type
     * @param id 自定义文档的ID
     * @param source
     * @return
     */
    public static IndexResponse addOneToIndex(TransportClient client, String index, String type, String id, Object... source) {
        IndexResponse response = client.prepareIndex(index, type).setId(id).setSource(source).get();
        return response;
    }

    /**
     * @param client
     * @param index
     * @param type
     * @param id 自定义文档的ID
     * @param json
     * @return
     */
    public static IndexResponse addOneJsonToIndex(TransportClient client, String index, String type, String id, String json) {
        IndexResponse response = client.prepareIndex(index, type).setId(id).setSource(json, XContentType.JSON).get();
        return response;
    }

    /**
     * @param client
     * @param index
     * @param type
     * @param id 自定义文档的ID
     * @param source
     * @return
     */
    public static IndexResponse addOneMapToIndex(TransportClient client, String index, String type, String id, Map<String, ?> source) {
        IndexResponse response = client.prepareIndex(index, type).setId(id).setSource(source).get();
        return response;
    }

    /**
     * 判断IndexResponse响应，是否已经成功创建好了
     *
     * @param response 把数据添加到索引后的响应
     * @return
     */
    public static boolean isCreated(IndexResponse response) {
        RestStatus status = response.status();
        if (status == RestStatus.CREATED) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否存在该索引
     *
     * @param indexName 索引名称
     * @return
     */
    public static boolean isIndexExists(TransportClient client, String indexName) {
        IndicesExistsRequestBuilder builder = client.admin().indices().prepareExists(indexName);
        IndicesExistsResponse res = builder.get();
        return res.isExists();
    }

    /**
     * 创建一个索引,不创建字段
     * 版本6，一个索引只允许创建一个类型
     * @param client
     * @param indexName
     * @param type
     * @param number_of_shards 默认建议5
     * @param number_of_replicas 默认建议1
     * @param refresh_interval_second 默认建议10s
     * @return
     */
    public static boolean createIndex(TransportClient client, String indexName, String type, int number_of_shards, int number_of_replicas, int refresh_interval_second) {
        try {
            if (isIndexExists(client, indexName)) {
                LOG.warn("索引对象已经存在，无法创建!");
                return false;
            }
            CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate(indexName);
            // 直接创建Map结构的setting
            Map<String, Object> settings = new HashMap<>();

            // 分片数
            settings.put("number_of_shards", number_of_shards);
            // 副本数
            settings.put("number_of_replicas", number_of_replicas);
            // 刷新间隔
            settings.put("refresh_interval", refresh_interval_second + "s");
            builder.setSettings(settings);
            builder.addMapping(type);
            CreateIndexResponse res = builder.get();
            if (res.isAcknowledged()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOG.error("创建索引失败！", e);
        }
        return false;
    }

    /**
     * 删除一个文档
     * @param client
     * @param index
     * @return
     */
    public static boolean deleteIndex(TransportClient client, String index) {
        AcknowledgedResponse response = client.admin().indices().prepareDelete(index).get();
        return response.isAcknowledged();
    }
    /**
     * 删除一个文档
     * @param client
     * @param index
     * @param type
     * @param id
     * @return
     */
    public static boolean deleteById(TransportClient client, String index, String type, String id) {
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        if (response.status() == RestStatus.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新
     *
     * @param client
     * @param index
     * @param type
     * @param id
     * @param fieldNameValue
     * @throws Exception
     * @return
     */
    public static UpdateResponse updateById(TransportClient client, String index, String type, String id, Map<String, ?> fieldNameValue) throws Exception {

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder();
        contentBuilder.startObject();
        contentBuilder.map(fieldNameValue);
        contentBuilder.endObject();
        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(contentBuilder);

        UpdateResponse response = client.update(updateRequest).get();
        return response;
    }
}

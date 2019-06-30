package com.latico.commons.db.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * 一个程序，可以共享一个MongoClient，MongoClient内部已经实现了连接池，
 * 对于大多数应用MongoClient可以使用单例模式，连接信息使用配置文件的方式
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-02-07 0:23
 * @Version: 1.0
 */
public class MongoClientUtils {
    /**
     * id的字段名
     */
    private static final String primaryKeyFieldName = "_id";

    public static MongoClient getMongoClient() {
        return new MongoClient("localhost");
    }

    public static MongoClient getMongoClient(String host) {
        return new MongoClient(host);
    }

    public static MongoClient getMongoClient(String host, int port) {
        return new MongoClient(host, port);
    }

    /**
     * 获取DB实例 - 指定DB
     *
     * @param mongoClient
     * @param dbName
     * @return
     */
    public static MongoDatabase getDatabase(MongoClient mongoClient, String dbName) {
        if (mongoClient != null && dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    /**
     * 创建，也可以设置各种初始化参数
     * @param mongoClient
     * @param dbName
     * @param collName
     * @param createCollectionOptions 创建集合的时候的选项
     */
    public static void createCollection(MongoClient mongoClient, String dbName, String collName, CreateCollectionOptions createCollectionOptions) {
        mongoClient.getDatabase(dbName).createCollection(collName, createCollectionOptions);
    }
    /**
     * 获取collection对象 - 指定Collection
     *
     * @param collName 集合名称
     * @return
     */
    public static MongoCollection<Document> getCollection(MongoClient mongoClient, String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    /**
     * 查询DB下的所有的集合的名称
     */
    public static List<String> listCollectionNames(MongoClient mongoClient, String dbName) {
        MongoIterable<String> collNames = getDatabase(mongoClient, dbName).listCollectionNames();
        List<String> list = new ArrayList<String>();
        for (String collName : collNames) {
            list.add(collName);
        }
        return list;
    }


    /**
     * 查询DB下的所有的集合
     */
    public static List<MongoCollection<Document>> getAllCollection(MongoClient mongoClient, String dbName) {
        MongoIterable<String> collNames = getDatabase(mongoClient, dbName).listCollectionNames();
        List<MongoCollection<Document>> list = new ArrayList<MongoCollection<Document>>();
        for (String collName : collNames) {
            MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
            list.add(collection);
        }
        return list;
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(MongoClient mongoClient, String dbName) {
        getDatabase(mongoClient, dbName).drop();
    }
    /**
     * 查找第一个对象 - 根据主键_id
     *
     * @param mongoClient
     * @param dbName
     * @param collName
     * @param id
     * @return
     */
    public static Document findFirstById(MongoClient mongoClient, String dbName, String collName, String id) {
        MongoCollection<Document> coll = getCollection(mongoClient, dbName, collName);
        ObjectId _idobj = new ObjectId(id);
        Document myDoc = coll.find(Filters.eq(primaryKeyFieldName, _idobj)).first();
        return myDoc;
    }
    /**
     * 查找第一个对象 - 根据主键_id
     *
     * @param coll
     * @param id
     * @return
     */
    public static Document findFirstById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = new ObjectId(id);
        Document myDoc = coll.find(Filters.eq(primaryKeyFieldName, _idobj)).first();
        return myDoc;
    }

    /**
     * 统计文档数量
     *
     * @param coll
     * @return
     */
    public static long countDocuments(MongoCollection<Document> coll) {
        return coll.countDocuments();
    }

    /**
     * 创建一个是主键ID的Bson
     *
     * @param id
     * @return
     */
    public static Bson createBsonByPrimaryKey(String id) {
        ObjectId objectId = new ObjectId(id);
        Bson filter = Filters.eq(primaryKeyFieldName, objectId);
        return filter;
    }

    /**
     * 条件查询
     */
    public static MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /**
     * 通过一个字段查询出结果集合
     * @param coll
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public static FindIterable<Document> findByField(MongoCollection<Document> coll, String fieldName, Object fieldValue) {
        BasicDBObject queryObject = new BasicDBObject(fieldName, fieldValue);
        return coll.find(queryObject);
    }

    /**
     * @param mongoClient
     * @param dbName
     * @param collName
     * @param fieldName
     * @param fieldValue
     * @return
     */
    public static FindIterable<Document> findByField(MongoClient mongoClient, String dbName, String collName, String fieldName, Object fieldValue) {
        MongoCollection<Document> collection = getCollection(mongoClient, dbName, collName);
        BasicDBObject queryObject = new BasicDBObject(fieldName, fieldValue);
        return collection.find(queryObject);
    }

    /**
     * 分页查询
     */
    public static MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject(primaryKeyFieldName, 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     *
     * @param coll
     * @param id
     * @return
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        Bson filter = createBsonByPrimaryKey(id);
        if (filter == null) {
            return 0;
        }
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * FIXME
     *
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public static Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        Bson filter = createBsonByPrimaryKey(id);
        if (filter == null) {
            return null;
        }
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    /**
     * 删除一个集合
     *
     * @param mongoClient
     * @param dbName
     * @param collName
     */
    public static void dropCollection(MongoClient mongoClient, String dbName, String collName) {
        getDatabase(mongoClient, dbName).getCollection(collName).drop();
    }

    /**
     * 关闭Mongodb
     */
    public static void close(MongoClient mongoClient) {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}

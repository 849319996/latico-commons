package com.latico.commons.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;

import java.util.List;

public class MongoClientUtilsTest {

    @Test
    public void test() {
        MongoClient mongoClient = MongoClientUtils.getMongoClient();

        MongoDatabase testDb = mongoClient.getDatabase("test");


    }

    @Test
    public void getMongoClientConn() {
        MongoClient mongoClientConn = MongoClientUtils.getMongoClient();
    }

    @Test
    public void insertOne(){
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";
        String collName = "test_coll";
        MongoCollection<Document> coll = MongoClientUtils.getCollection(mongoClient, dbName, collName);

        // 插入，文档中包含文档
        for (int i = 1; i <= 4; i++) {
            Document doc = new Document();
            doc.put("name", "zhoulf");
            doc.put("school", "NEFU" + i);
            Document interests = new Document();
            interests.put("game", "game" + i);
            interests.put("ball", "ball" + i);
            doc.put("interests", interests);
            coll.insertOne(doc);
        }

    }
    @Test
    public void findByField() {
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";
        String collName = "test_coll";
        FindIterable<Document> byField = MongoClientUtils.findByField(mongoClient, dbName, collName, "name", "zhoulf");

        for (Document document : byField) {
            System.out.println(document);
        }
        MongoClientUtils.close(mongoClient);

    }

    @Test
    public void listCollectionNames(){
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";

        List<String> collectionNames = MongoClientUtils.listCollectionNames(mongoClient, dbName);

        System.out.println(collectionNames);
    }


    @Test
    public void getAllCollection(){
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";

        List<MongoCollection<Document>> collections = MongoClientUtils.getAllCollection(mongoClient, dbName);

        for (MongoCollection<Document> collection : collections) {
            for (Document document : collection.find()) {
                System.out.println(document);
            }
        }
    }

    @Test
    public void findFirstById(){
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";
        String collName = "test_coll";
        Document document = MongoClientUtils.findFirstById(mongoClient, dbName, collName, "5c9307ceff1640665ae65097");
        System.out.println(document);

        MongoClientUtils.close(mongoClient);
        document = MongoClientUtils.findFirstById(mongoClient, dbName, collName, "5c9307ceff1640665ae65097");
        System.out.println(document);
    }


    /**
     * 测试入口
     */
    @Test
    public void main() {
        MongoClient mongoClient = MongoClientUtils.getMongoClient();
        String dbName = "test";
        String collName = "test_coll";
        MongoCollection<Document> coll = MongoClientUtils.getCollection(mongoClient, dbName, collName);

        // 插入，文档中包含文档
        for (int i = 1; i <= 4; i++) {
            Document doc = new Document();
            doc.put("name", "zhoulf");
            doc.put("school", "NEFU" + i);
            Document interests = new Document();
            interests.put("game", "game" + i);
            interests.put("ball", "ball" + i);
            doc.put("interests", interests);
            coll.insertOne(doc);
        }

        // // 根据ID查询
        // String id = "556925f34711371df0ddfd4b";
        // Document doc = MongoClientUtils2.instance.findById(coll, id);
        // System.out.println(doc);

        // 查询多个
        // MongoCursor<Document> cursor1 = coll.find(Filters.eq("name", "zhoulf")).iterator();
        // while (cursor1.hasNext()) {
        // org.bson.Document _doc = (Document) cursor1.next();
        // System.out.println(_doc.toString());
        // }
        // cursor1.close();

        // 查询多个
        // MongoCursor<Person> cursor2 = coll.find(Person.class).iterator();

        // 删除数据库
        // MongoClientUtils2.instance.dropDB("testdb");

        // 删除表
        // MongoClientUtils2.instance.dropCollection(dbName, collName);

        // 修改数据
        // String id = "556949504711371c60601b5a";
        // Document newdoc = new Document();
        // newdoc.put("name", "时候");
        // MongoClientUtils.instance.updateById(coll, id, newdoc);

        // 统计表
        // System.out.println(MongoClientUtils.instance.getCount(coll));

        // 查询所有
        Bson filter = Filters.eq("count", 0);
        MongoClientUtils.find(coll, filter);

    }


}
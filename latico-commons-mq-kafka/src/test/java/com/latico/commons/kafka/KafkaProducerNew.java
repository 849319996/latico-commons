package com.latico.commons.kafka;

import com.latico.commons.common.util.logging.LogUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
* producer: org.apache.kafka.clients.producer.KafkaProducer;
 * */
 
public class KafkaProducerNew {
 
    private final KafkaProducer<String, String> producer;
 
    public final static String TOPIC = "didi-topic-test";
 
    private KafkaProducerNew() {
        Properties props = new Properties();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.95.177.192:9092,10.95.97.175:9092");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
 
        producer = new KafkaProducer<String, String>(props);
    }
 
    public void produce() {
        int messageNo = 1;
        final int COUNT = 10;
 
        while(messageNo < COUNT) {
            String key = String.valueOf(messageNo);
            String data = String.format("hello KafkaProducer message %s", key);
            System.out.println("发送消息:" + data);
            try {
                Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(TOPIC, data));
                System.out.println(future.get() + " isDone:" + future.isDone() + " isCancelled:" + future.isCancelled());
            } catch (Exception e) {
                e.printStackTrace();
            }
 
            messageNo++;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        producer.close();
    }
 
    public static void main(String[] args) {
        //kafka的自带debug日志量太大，必须使用info级别logback日志
        LogUtils.loadLogBackConfigDefault();
        new KafkaProducerNew().produce();
    }
 
}
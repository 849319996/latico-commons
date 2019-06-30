package com.latico.commons.kafka;

import com.latico.commons.common.util.logging.LogUtils;
import org.apache.kafka.clients.consumer.*;

import java.util.Arrays;
import java.util.Properties;

/**
* consumer: org.apache.kafka.clients.consumer.Consumer
* */
public class KafkaConsumerNew {
 
    private Consumer<String, String> consumer;
 
    private static String group = "group-1";
 
    private static String TOPIC = "didi-topic-test";
 
    private KafkaConsumerNew() {
        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.95.177.192:9092,10.95.97.175:9092");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true"); // 自动commit
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动commit的间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
    }
 
    private void consume() {
        consumer.subscribe(Arrays.asList(TOPIC)); // 可消费多个topic,组成一个list
 
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 
    public static void main(String[] args) {
        //kafka的自带debug日志量太大，必须使用info级别logback日志
        LogUtils.loadLogBackConfigDefault();
        new KafkaConsumerNew().consume();
    }
}
package com.latico.commons.kafka.consumer;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.mq.kafka.KafkaUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Arrays;

/**
 * consumer: org.apache.kafka.clients.consumer.Consumer
 */
public class KafkaConsumer {

    public static void main(String[] args) {
        //kafka的自带debug日志量太大，建议使用info级别logback日志
        LogUtils.loadLogBackConfigDefault();

        Consumer<String, String> consumer;

        String group = "group-1";

        String TOPIC = "didi-topic-test";

        // 可消费多个topic,组成一个list
        consumer = KafkaUtils.createConsumerWithString(group, Arrays.asList(TOPIC), "localhost:9092");

        Duration timeout = Duration.ofSeconds(1);
        while (true) {

            ConsumerRecords<String, String> records = KafkaUtils.consumerPoll(consumer, timeout);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s \n", record.offset(), record.key(), record.value());


//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }
}
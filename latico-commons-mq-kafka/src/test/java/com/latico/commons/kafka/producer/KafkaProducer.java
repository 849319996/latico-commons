package com.latico.commons.kafka.producer;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.common.util.thread.ThreadUtils;
import com.latico.commons.mq.kafka.KafkaUtils;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

/**
 * producer: org.apache.kafka.clients.producer.KafkaProducer;
 */

public class KafkaProducer {

    public static void main(String[] args) {
        //kafka的自带debug日志量太大，必须使用info级别logback日志
        LogUtils.loadLogBackConfigDefault();
        org.apache.kafka.clients.producer.Producer<String, String> producer = KafkaUtils.createProducerWithString(null, "localhost:9092");

        String TOPIC = "didi-topic-test";

        while (true) {
//            producer.beginTransaction();

            int messageNo = 1;
            final int COUNT = 10;

            while (messageNo < COUNT) {
                String key = String.valueOf(messageNo);
                String data = String.format("hello KafkaProducer message %s", key);
                System.out.println("发送消息:" + data);

                try {
                    Future<RecordMetadata> future = null;

//               平均发送有key和没key
                    if (messageNo % 2 == 0) {
                        future = KafkaUtils.producerSend(producer, TOPIC, data);
                    } else {
                        future = KafkaUtils.producerSend(producer, TOPIC, messageNo + "", data);
                    }

                    System.out.println("回执:" + future.get() + " isDone:" + future.isDone() + " isCancelled:" + future.isCancelled());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                messageNo++;
                ThreadUtils.sleepSecond(2);
            }

//            producer.commitTransaction();

            ThreadUtils.sleepSecond(5);
        }

//        KafkaUtils.close(producer);
    }

}
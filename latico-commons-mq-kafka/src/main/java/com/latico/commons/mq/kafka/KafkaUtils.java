package com.latico.commons.mq.kafka;

import com.latico.commons.common.util.string.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;

import java.time.Duration;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * <PRE>
 * 最初是一个日志系统，高吞吐量高可用性
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-01-26 22:05
 * @Version: 1.0
 */
public class KafkaUtils {

    /**
     * 创建一个kafka生产者
     *
     * @param props 所有属性
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Producer<K, V> createProducer(Properties props) {

//        创建
        Producer<K, V> producer = new KafkaProducer<>(props);

        return producer;
    }
    /**
     * 创建一个kafka消费者
     *
     * @param props 所有属性
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Consumer<K, V> createConsumer(Properties props) {
        Consumer<K, V> consumer = new KafkaConsumer<>(props);
        return consumer;
    }

    public static void close(Producer producer) {
        if (producer != null) {
            producer.close();
        }
    }
    public static void close(Consumer consumer) {
        if (consumer != null) {
            consumer.close();
        }
    }
    /**
     * 创建一个String数据类型的生产者
     * @param transactionalId 事务ID，可以为空
     * @param brokerSockets broker的集群所有sockets
     * @return
     */
    public static Producer<String, String> createProducerWithString(String transactionalId, String... brokerSockets) {
        String sockets = StringUtils.join(brokerSockets, ",");
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sockets);

//        判断是否添加事务
        if (StringUtils.isNotBlank(transactionalId)) {
            props.put("transactional.id", transactionalId);
        }

        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = createProducer(props);

        //        判断是否需要初始化事务功能
        if (StringUtils.isNotBlank(transactionalId)) {
            //        初始化事务
            producer.initTransactions();
        }

        return producer;
    }


    /**
     * 创建一个String数据类型的消费者
     * @param groupId 组ID，对于所有主题，broker在分发每个主题消息的时候，会对同一个组里面的只会有个消费者收到消息
     * @param topics  可消费多个topic,组成一个list
     * @param brokerSockets broker的集群所有sockets
     * @return
     */
    public static Consumer<String, String> createConsumerWithString(String groupId, Collection<String> topics, String... brokerSockets) {
        String sockets = StringUtils.join(brokerSockets, ",");
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sockets);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

//        // 拉取下来自动commit给broker说明已经消费，如果是false就需要手动提交给broker
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); // 自动commit的间隔
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

//        创建
        Consumer<String, String> consumer = createConsumer(props);
//        订阅主题
        consumer.subscribe(topics);

        return consumer;
    }

    /**
     * 生产者发送数据到主题上
     * @param producer
     * @param topic
     * @param data
     * @return
     */
    public static <K,V> Future<RecordMetadata> producerSend(Producer<K,V> producer, String topic, V data) {
        return producer.send(new ProducerRecord<K, V>(topic, data));
    }

    public static <K,V> Future<RecordMetadata> producerSend(Producer<K,V> producer, String topic, K key, V data) {
        return producer.send(new ProducerRecord<K, V>(topic, key, data));
    }

    /**
     * 消费者从broker中拉取数据
     * @param consumer 消费者客户端
     * @param timeout 超时定义
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConsumerRecords<K, V> consumerPoll(Consumer<K, V> consumer, Duration timeout) {
        return consumer.poll(timeout);
    }

    /**
     * 消费者从broker中拉取数据
     * @param consumer 消费者客户端
     * @param timeoutMs 超时，单位毫秒
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConsumerRecords<K, V> consumerPoll(Consumer<K, V> consumer, long timeoutMs) {
        Duration timeout = Duration.ofMillis(timeoutMs);
        return consumerPoll(consumer, timeout);
    }
}

package com.latico.commons.mq.activemq;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.string.StringUtils;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.fusesource.mqtt.client.*;
import org.fusesource.mqtt.client.Topic;
import org.fusesource.stomp.jms.StompJmsConnectionFactory;

import javax.jms.Message;
import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * <PRE>
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-26 22:05
 * @version: 1.0
 */
@SuppressWarnings("JavadocReference")
public class ActiveMQUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveMQUtils.class);

    /**
     * 如果有多个的话就要使用failover协议，如：failover:(tcp://localhost:61616,tcp://10.88.112.165:61617)
     * <p>
     * 1、Failover是所有协议之上的一个协议，所以其用法如下：
     * <p>
     * failover:(tcp://localhost:61616,tcp://10.88.112.165:61617)?randomize=false
     * <p>
     * 或者
     * <p>
     * failover:()?randomize=false&updateURIsURL=file:/E:/emq-4.0.0-verify/to_removed/urlist.txt
     * <p>
     * /////urlist.txt///begin////
     * <p>
     * tcp://localhost:61616,tcp://10.88.112.165:61617
     * <p>
     * /////urlist.txt///end//////
     * <p>
     * 1、  Failover协议仅用在JMS客户端和Broker间，不能用在broker和broker之间；
     * <p>
     * 3、  failover方式跟消费者是异步还是同步接收没有关系。
     * <p>
     * 4、  main方法中直接调用Failover失效的原因：根本原因：是因为没有可用的用户线程，导致JVM自动退出，因为main方法执行完后，会生成一个【DestroyJavaVM】的用户线程。
     * <p>
     * 如果没有再没有其他用户线程的话，就轮到执行【DestroyJavaVM】的用户线程了，所以JVM会自动退出，导致failover切换失败。
     * <p>
     * 5、  在main方法中合理使用failover协议，建议通过另起线程的方式消费消息；
     * <p>
     * 6、  如果所有的用户线程终止，那JVM就会终止。
     * <p>
     * 7、  使用文件记录uri列表的方式，具有很好的扩展性，因为客户端每次重连时都是从文件加载一次，所有可以随时从文件中更新uri列表。
     *
     * @param brokerURLs
     * @return
     */
    public static String combineUriSupportFailover(String... brokerURLs) {
        String uri = null;
        if (brokerURLs.length == 1) {
            uri = brokerURLs[0];
        } else {
//           有多个的话就要使用failover协议，如： failover:(tcp://localhost:61616,tcp://10.88.112.165:61617)
            String urlStrs = StringUtils.join(brokerURLs, ",");
            uri = "failover:(" + urlStrs + ")";
        }
        return uri;
    }

    /**
     * 创建完的连接工厂，此时未真正连接broker
     *
     * @param userName   连接登录用户名，可选，可以为空
     * @param password   连接登录密码，可选，可以为空
     * @param brokerURLs 支持多个URL
     * @return
     */
    public static ConnectionFactory createOpenwireConnectionFactoryByBrokerUrls(String userName, String password, String... brokerURLs) {
        String uri = combineUriSupportFailover(brokerURLs);

        if (StringUtils.isBlank(userName) && StringUtils.isBlank(password)) {
            return new ActiveMQConnectionFactory(uri);
        } else {
            return new ActiveMQConnectionFactory(userName, password, uri);
        }
    }

    /**
     * Openwire协议在TCP模式下
     *
     * @param userName 连接登录用户名，可选，可以为空
     * @param password 连接登录密码，可选，可以为空
     * @param sockets
     * @return
     */
    public static ConnectionFactory createOpenwireConnectionFactoryByTcpSockets(String userName, String password, String... sockets) {
        String uri = getUriBySocketAtTcp(sockets);
        return createOpenwireConnectionFactoryByBrokerUrls(userName, password, uri);
    }

    /**
     * Openwire协议在NIO模式下
     * 一般对于客户端特别多的话，可以使用这个，正常的话使用TCP模式即可
     *
     * @param userName 连接登录用户名，可选，可以为空
     * @param password 连接登录密码，可选，可以为空
     * @param sockets
     * @return
     */
    public static ConnectionFactory createOpenwireConnectionFactoryNio(String userName, String password, String... sockets) {
        String uri = getUriBySocketAtNio(sockets);
        return new ActiveMQConnectionFactory(userName, password, uri);
    }

    /**
     * 创建一个AMQP的连接工厂
     *
     * @param userName 连接登录用户名，可选，可以为空
     * @param password 连接登录密码，可选，可以为空
     * @param sockets
     * @return
     */
    public static ConnectionFactory createAmqpConnectionFactoryBySockets(String userName, String password, String... sockets) {
        String uri = getUriBySocketAtAmqp(sockets);
        if (StringUtils.isBlank(userName) && StringUtils.isBlank(password)) {
            return new JmsConnectionFactory(uri);
        } else {
            return new JmsConnectionFactory(userName, password, uri);
        }
    }

    /**
     * 创建一个Stomp的连接工厂
     *
     * @param userName 连接登录用户名，可选，可以为空
     * @param password 连接登录密码，可选，可以为空
     * @param sockets
     * @return
     */
    public static ConnectionFactory createStompConnectionFactoryBySockets(String userName, String password, String... sockets) {
        String uri = getUriBySocketAtStomp(sockets);
        StompJmsConnectionFactory stompJmsConnectionFactory = new StompJmsConnectionFactory();
        stompJmsConnectionFactory.setBrokerURI(uri);
        stompJmsConnectionFactory.setUsername(userName);
        stompJmsConnectionFactory.setPassword(password);

        return stompJmsConnectionFactory;
    }

    /**
     * @param userName
     * @param password
     * @param sockets
     * @return
     * @throws URISyntaxException
     */
    public static MQTT createMqttConnectionFactoryBySockets(String userName, String password, String... sockets) throws URISyntaxException {
        String uriStr = getUriBySocketAtMqtt(sockets);
        MQTT mqtt = new MQTT();
        URI uri = new URI(uriStr);
        mqtt.setHost(uri);
        mqtt.setUserName(userName);
        mqtt.setPassword(password);
        return mqtt;
    }


    /**
     * 通过多个Socket，组装Broker的URI
     *
     * @param protocol
     * @param sockets
     * @return
     */
    public static String getUriBySocket(String protocol, String[] sockets) {
        String[] brokerURLs = new String[sockets.length];
        for (int i = 0; i < sockets.length; i++) {
            brokerURLs[i] = protocol + "://" + sockets[i];
        }
        return combineUriSupportFailover(brokerURLs);
    }

    /**
     * 通过多个Socket，组装Broker的URI
     * TCP协议
     *
     * @param sockets
     * @return
     */
    public static String getUriBySocketAtTcp(String... sockets) {
        String protocol = "tcp";
        return getUriBySocket(protocol, sockets);
    }

    /**
     * NIO协议
     *
     * @param sockets
     * @return
     */
    public static String getUriBySocketAtNio(String... sockets) {
        String protocol = "nio";
        return getUriBySocket(protocol, sockets);
    }

    /**
     * amap是使用amqp协议
     *
     * @param sockets
     * @return
     */
    public static String getUriBySocketAtAmqp(String... sockets) {
        String protocol = "amqp";
        return getUriBySocket(protocol, sockets);
    }

    /**
     * stomp是使用TCP
     *
     * @param sockets
     * @return
     */
    public static String getUriBySocketAtStomp(String... sockets) {
        return getUriBySocketAtTcp(sockets);
    }

    /**
     * MQTT是使用TCP
     *
     * @param sockets
     * @return
     */
    public static String getUriBySocketAtMqtt(String... sockets) {
        return getUriBySocketAtTcp(sockets);
    }


    /**
     * 创建一个连接
     * 真正连接到broker
     *
     * @param factory
     * @return
     * @throws JMSException
     */
    public static Connection createConnection(ConnectionFactory factory) throws JMSException {
        Connection connection = factory.createConnection();
        connection.start();
        return connection;
    }

    /**
     * @param connection
     * @param transacted      是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
     * @param acknowledgeMode 消息的确认模式：
     *                        {@link Session.AUTO_ACKNOWLEDGE} ： 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中可能因为错误而丢失消息。
     *                        {@link Session.CLIENT_ACKNOWLEDGE} ： 由消息接收者确认收到消息，通过调用消息的acknowledge()方法（会通知消息提供者收到了消息）
     *                        {@link Session.DUPS_OK_ACKNOWLEDGE} ：指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）。
     * @return
     * @throws JMSException
     */
    @SuppressWarnings("JavadocReference")
    public static Session createSession(Connection connection, boolean transacted, int acknowledgeMode) throws JMSException {
        Session session = connection.createSession(transacted, acknowledgeMode);
        return session;
    }

    public static Session createSession(Connection connection) throws JMSException {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * 创建一个主题模式的生产者
     * 每个消息会被所有消费者消费
     *
     * @param session
     * @param topicName    主题名称
     * @param deliveryMode {@link DeliveryMode} 这是传输模式。ActiveMQ支持两种传输模式：持久传输和非持久传输(persistent and non-persistent delivery)，默认情况下使用的是持久传输。
     * @return
     */
    @SuppressWarnings("JavadocReference")
    public static MessageProducer createProducerByTopic(Session session, String topicName, int deliveryMode) throws JMSException {
        Destination destination = session.createTopic(topicName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(deliveryMode);
        return producer;
    }

    /**
     * 主题模式
     *
     * @param session
     * @param topicName
     * @return
     * @throws JMSException
     */
    public static MessageProducer createProducerByTopic(Session session, String topicName) throws JMSException {
        return createProducerByTopic(session, topicName, DeliveryMode.PERSISTENT);
    }

    /**
     * 队列模式，每个消息只会被消费一次
     * 若为临时队列我们可以在关闭它的同时将它删除,相反,若为永久队列则重启后还有，
     * 这里创建的是永久队列
     *
     * @param session
     * @param queueName
     * @param deliveryMode
     * @return
     * @throws JMSException
     */
    public static MessageProducer createProducerByQueue(Session session, String queueName, int deliveryMode) throws JMSException {
        Destination destination = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(deliveryMode);
        return producer;
    }

    public static MessageProducer createProducerByQueue(Session session, String queueName) throws JMSException {
        return createProducerByQueue(session, queueName, DeliveryMode.PERSISTENT);
    }

    public static TextMessage createTextMessage(Session session, String body) throws JMSException {
        return session.createTextMessage(body);
    }

    /**
     * 发送数据
     *
     * @param producer
     * @param msg
     */
    public static boolean send(MessageProducer producer, Message msg) {
        try {
            producer.send(msg);
            return true;
        } catch (JMSException e) {
            LOG.error("生产者发送数据异常", e);
        }
        return false;
    }

    /**
     * 创建一个消费者
     *
     * @param session
     * @param topicName 主题名称
     * @return
     * @throws JMSException
     */
    public static MessageConsumer createConsumerByTopic(Session session, String topicName) throws JMSException {
        Destination destination = session.createTopic(topicName);
        MessageConsumer consumer = session.createConsumer(destination);
        return consumer;
    }

    /**
     * 创建一个消费者
     *
     * @param session
     * @param queueName 队列名称
     * @return
     * @throws JMSException
     */
    public static MessageConsumer createConsumerByQueue(Session session, String queueName) throws JMSException {
        Destination destination = session.createQueue(queueName);
        MessageConsumer consumer = session.createConsumer(destination);
        return consumer;
    }

    /**
     * 接收信息，通过返回参数泛型进行强转
     *
     * @param consumer
     * @param <T>
     * @return
     * @throws JMSException
     */
    public static <T extends Message> T receive(MessageConsumer consumer) throws JMSException {
        Message message = consumer.receive();
        return (T) message;
    }

    /**
     * 断开整个连接，里面的所有Session也会被关闭
     *
     * @param connection
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                LOG.error(e);
            }
        }
    }

    /**
     * 关闭一个连接下面的session
     *
     * @param session
     */
    public static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                LOG.error(e);
            }
        }
    }

    /**
     * MQTT的响应式连接
     * @param factory
     * @return
     */
    public static CallbackConnection createCallbackConnection(MQTT factory) {
        return factory.callbackConnection();
    }
    /**
     * MQTT的响应式连接
     * @param factory
     * @return
     */
    public static FutureConnection createFutureConnection(MQTT factory) throws Exception {
        FutureConnection futureConnection = factory.futureConnection();
        futureConnection.connect().await();
        return futureConnection;
    }

    public static BlockingConnection createBlockingConnection(MQTT factory) throws Exception {
        BlockingConnection connection = factory.blockingConnection();
        connection.connect();
        return connection;
    }

    /**
     * 创建MQTT客户端监听
     * @param connection
     * @param listener
     * @param callback
     * @throws InterruptedException
     */
    public static void startMqttListener(CallbackConnection connection, Listener listener, Callback<Void> callback) throws InterruptedException {
        connection.listener(listener);
        connection.connect(callback);

        Thread thread = new Thread() {
            @Override
            public void run() {
                // Wait forever..
                synchronized (Listener.class) {
                    try {
                        while (true) {
                            Listener.class.wait();
                        }
                    } catch (InterruptedException e) {
                        LOG.error(e);
                    }
                }
            }
        };
        thread.start();

    }

    public static void startMqttListener(CallbackConnection connection, Listener listener, String topicName) throws InterruptedException {

        //创建消息回调，
        Callback<Void> callback = new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOG.info("创建连接回调成功，开始订阅:{}", topicName);
                Topic[] topics = {new Topic(topicName, QoS.AT_LEAST_ONCE)};

                connection.subscribe(topics, new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] qoses) {
                        LOG.info("订阅主题[{}]回调成功,返回状态:{}", topicName, Arrays.toString(qoses));
                    }
                    @Override
                    public void onFailure(Throwable value) {
                        LOG.error("订阅主题[{}]回调失败,返回状态", value, topicName);
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                LOG.info("创建连接回调失败,不进行订阅:{}", value, topicName);
            }
        };

        connection.listener(listener);
        connection.connect(callback);

        Thread thread = new Thread() {
            @Override
            public void run() {
                // Wait forever..
                synchronized (Listener.class) {
                    try {
                        while (true) {
                            Listener.class.wait();
                        }
                    } catch (InterruptedException e) {
                        LOG.error(e);
                    }
                }
            }
        };
        thread.start();

    }

    /**
     * @param connection
     * @param callback
     * @return
     */
    public static void disconnect(CallbackConnection connection, Callback<Void> callback) {
        if (connection != null) {
            connection.disconnect(callback);
        }
    }
}

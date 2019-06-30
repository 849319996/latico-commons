/**
 * <PRE>
 在RabbitMQ中，生产者不是直接将消息发送给消费者，生成者根本不知道这个消息要传递给哪些队列。实际上，生产者只是将消息发送到交换机。交换机收到消息到，根据交换机的类型和配置来处理消息，有如下几种情况：

 将消息传送到特定的队列
 有可能发送到多个队列中
 也有可能丢弃消息
 RabbitMQ各个组件的功能重新归纳一下如下：

 生产者：发送消息
 交换机：将收到的消息根据路由规则路由到特定队列
 队列：用于存储消息
 消费者：收到消息并消费

 另外RabbitMQ默认定义一些交换机：

 默认交换机
 amq.* exchanges
 还有一类特殊的交换机：Dead Letter Exchange（死信交换机）
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-06-27 22:17
 * @Version: 1.0
 */
package com.latico.commons.mq.rabbitmq;
package com.latico.commons.mq.rabbitmq;

/**
 * <PRE>
 * 实现AMQP协议
 * 1、下载安装Erlang；
 * 2、下载安装RabbitMQ；
 * 3、启动RabbitMQ；
 * 4、启动web管理插件；

 一、安装及基本概念

 rabbitmq是基于erlang编写的，所以安装rabbitmq之前需要安装erlang的环境，配置环境变量，百度即可。

 命令：
 1、查看服务状态：service rabbitmq-server status
 2、启动：service rabbitmq-server start
 3、停止：service rabbitmq-server stop
 4、重启：service rabbitmq-server restart
 #
 在web浏览器中输入地址：http://127.0.0.1:15672/

 connectionFactory、connection、channel不做过多介绍，基本步骤为

 1.初始化连接工厂
 2.从连接工厂获取连接
 3.在连接中打开一个通道
 4.在通道中声明交换机
 5.在通道中声明一个列队（或多个）
 6.绑定列队到交换机
 7.进行相关操作

 二、工作模式（路由模式）

 生产者--消费者模式基本的模式是生产者生成消息，投入到列队中，需要的消费者（订阅）去列队拿消息进行处理。

 rabbitmq在这里进行了一个中间处理，消息投递给哪个列队由路由器或者称交换机（Exchange）来处理，即生产者不直接将消息投递到列队中，而是投递到交换机，具体要投递到哪个列队，由交换机根据路由规则来确定。

 类似于寄信，寄送人不直接将信件投递到派件员手中，而是投递到邮局，邮局根据信件的地址来决定到送到哪位派件员手上，进而送达收件人。这也是rabbitmq的一个特点和强大之处。

 所以，rabbitmq的工作模式就是producer将消息投递到特定的exchange，queue按routing key订阅消息（例如，列队A订阅了交换机E的路由键为test的消息，那么一个生产者投递两个消息到E中两个消息，routing key分别为test和test2，那么A列队的只能收到路由键为test的消息），符合路由键的消息将被分发到具体的列队中。

 交换机常用的路由方式有四种，fanout、direct、topic、header。header用的较少,假如不手动设置，默认是：direct，可以看{@link com.rabbitmq.client.AMQP.Exchange.Declare.Builder#type}

 web管理界面用户角色有下面几种：
 超级管理员(administrator)
 可登陆管理控制台，可查看所有的信息，并且可以对用户，策略(policy)进行操作。
 监控者(monitoring)
 可登陆管理控制台，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)
 策略制定者(policymaker)
 可登陆管理控制台, 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。
 普通管理者(management)
 仅可登陆管理控制台，无法看到节点信息，也无法对策略进行管理。
 其他
 无法登陆管理控制台，通常就是普通的生产者和消费者。
 * </PRE>
 *
 * @author: latico
 * @date: 2019-01-26 22:05
 * @version: 1.0
 */
public class RabbitMQUtils {
}

/**
 * <PRE>
 * 实现JMS协议
  1、下载ActiveMQ（java编写的）；
 2、启动ActiveMQ；
  windows系统,在bin文件夹下打开,命令行 输入 activemq-admin.bat start输入此命令就可以解决activeMQ启动不了的问题
 可以编写成bat文件方便启动

 AcitveMQ window环境 61616 端口占用
 AcitveMQ无法启动，总是提示端口被占用
 ERROR | Failed to start Apache ActiveMQ (localhost, ID:latico-49977-1559372472025-0:1)
 java.io.IOException: Transport Connector could not be registered in JMX: java.io.IOException: Failed to bind to server socket: tcp://0.0.0.0:61616?maximumConnections=1000&wireFormat.maxFrameSize=104857600 due to: java.net.BindException: Address already in use: JVM_Bind

 activemq的默认端口为：61616
此端口可能被windows的某种服务占用掉，所以需要修改activemq的默认端口；
进入到%ACTIVEMQ_HOME%\conf中的actviemq.xml中修改61616位61618(此端口可为任意未被占用端口)即可；

 也可以使用如下方法：

 使用netstat -aon 查询不到这个进程占用的端口。
 解决方法：
 Windows的一个服务占用了这个端口Internet Connection Sharing (ICS)
 把这个服务关闭即可

 3、测试
 ActiveMQ WebConsole available at http://0.0.0.0:8161/
 INFO | ActiveMQ Jolokia REST API available at http://0.0.0.0:8161/api/jolokia/

　浏览器进入到127.0.0.1:8161，activemq控制台，可查看activemq中的各消息信息；
　用户名：admin
　密码：admin

　用户名和密码在%ACTIVEMQ_HOME%\conf中的jetty-realm.properties中配置。如下：

  4、启动消息监听者；
  5、启动消息发布者；

 6、性能测试
 activeMQ5.9.版本带的例子提供了5种协议的简单demo,这5种协议分别为：AMQP,MQTT,OpenWire,Stomp，ws通过运行这5种协议的的demo可以大致得出这5种协议实现在activeMQ中的性能对比：
MQTT一般用于手机android移动端

 amqp

 Received 10001 in 6.58 seconds

 mqtt

 Received 10000 in 2.69 seconds

 openwire

 Received 10001 in 8.87 seconds

 stomp

 Received 10001 in 14.17 seconds
 可以看出，mqtt协议的实现最快，stomp的协议实现最慢。

 ws没有测试，这个应该是更慢。

 7、activeMQ支持5种协议类型
 每一个“transportConnector”标记的name属性和uri属性都必须填写，name属性的值可以随便填写，它将作为一个Connector元素，显示在ActiveMQ管理界面的Connections栏目中；URI参数部分，每一种协议都有一些特定的参数，读者可参考ActiveMQ官网中，关于“协议”部分的介绍：http://activemq.apache.org/protocols.html。URI描述信息的头部是采用协议名称：例如，描述amqp协议的监听端口时，采用的URI描述格式为“amqp://……”；描述Stomp协议的监听端口时，采用的URI描述格式为“stomp://……”。唯独在进行openwire协议描述时，URI头却采用的“tcp://…..”。这是因为ActiveMQ中默认的消息协议就是openwire：
 ActiveMQ在Version 5.13.0+ 版本后，将OpenWire, STOMP, AMQP, MQTT这四种主要协议的端口监听进行了合并，并使用auto关键字进行表示。也就是说，ActiveMQ将监听这一个端口的消息状态，并自动匹配合适的协议格式。配置如下：
 <transportConnectors>
     <transportConnector name="auto" uri="auto://0.0.0.0:61617?maximumConnections=1000" />
 </transportConnectors>

 <transportConnectors>
 <!-- DOS protection, limit concurrent connections to 1000 and frame size to 100MB -->
    <transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
     <transportConnector name="openwire—nio" uri="nio://0.0.0.0:61618?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
     <transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
     <transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
     <transportConnector name="mqtt" uri="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
     <transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
 </transportConnectors>

   附这5种协议的简单介绍：

 1、AMQP协议：
    即Advanced Message Queuing Protocol,一个提供统一消息服务的应用层标准高级消息队列协议,是应用层协议的一个开放标准,为面向消息的中间件设计。基于此协议的客户端与消息中间件可传递消息，并不受客户端/中间件不同产品，不同开发语言等条件的限制。
 2、MQTT协议：
    MQTT（Message Queuing Telemetry Transport，消息队列遥测传输）是IBM开发的一个即时通讯协议，有可能成为物联网的重要组成部分。该协议支持所有平台，几乎可以把所有联网物品和外部连接起来，被用来当做传感器和致动器（比如通过Twitter让房屋联网）的通信协议。
 3、OpenWire协议：
    OpenWire协议在网上没有对应的介绍，似乎是activeMQ自己定义的一种协议，官方网站对其的介绍如下：
    OpenWire is our cross language Wire Protocol to allow native access to ActiveMQ from a number of different languages and platforms. The Java OpenWire transport is the default transport in ActiveMQ 4.x or later. For other languages see the following...
    它是activeMQ默认的协议，可以使用TCP协议或者NIO协议传输，
     <transportConnectors>
         <transportConnector name="openwire" uri="tcp://localhost:61616?trace=true" />
         <transportConnector name="openwire—nio" uri="nio://localhost:61618?trace=true" />
     </transportConnectors>
 上面的配置，示范了一个TCP协议监听61616端口，一个NIO协议监听61618端口
 4、stomp协议：
    STOMP，Streaming Text Orientated Message Protocol，是流文本定向消息协议，是一种为MOM(Message Oriented Middleware，面向消息的中间件)设计的简单文本协议。
 5、ws协议：
    即websocket协议，基于h5

 这里我们主要介绍ActiveMQ消息中间件，它有两种消息模式，一种是队列模式，一种是主题模式。下面我们分别来介绍。

 队列模式
 什么是队列模式呢？所谓队列模式，就是我们的消息生产者（Producer）向一个目的地(Desitinate)存放我们的消息，如果是多个消费者我们可以它们会分别消费这些消息，每个消息只能供一个消费者使用
 主题模式
 如果我们上面理解了队列模式，这里我们很容易理解主题模式，与之相反的，所有消费者都会收到所有消息；

 配置连接的用户名密码，在<broker>节点里面<systemUsage>节点前面添加如下：
 <plugins>
     <simpleAuthenticationPlugin>
         <users>
            <authenticationUser username="admin" password="admin" groups="users,admins"/>
         </users>
     </simpleAuthenticationPlugin>
 </plugins>
 <systemUsage>
     <systemUsage>
         <memoryUsage>
             <memoryUsage percentOfJvmHeap="70" />
         </memoryUsage>
         <storeUsage>
            <storeUsage limit="100 gb"/>
         </storeUsage>
         <tempUsage>
             <tempUsage limit="50 gb"/>
         </tempUsage>
     </systemUsage>
 </systemUsage>

 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-30 9:36
 * @Version: 1.0
 */
package com.latico.commons.mq.activemq;
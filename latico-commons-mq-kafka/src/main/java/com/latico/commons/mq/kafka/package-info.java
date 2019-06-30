/**
 * <PRE>
 一、安装JAVA JDK
 1、下载安装包

 http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

 注意：根据32/64位操作系统下载对应的安装包

 2、添加系统变量：JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_144

 二、安装ZooKeeper
 1、 下载安装包

 http://zookeeper.apache.org/releases.html#download

 2、 解压并进入ZooKeeper目录，笔者：D:\Kafka\zookeeper-3.4.9\conf

 3、 将“zoo_sample.cfg”重命名为“zoo.cfg”

 4、 打开“zoo.cfg”找到并编辑dataDir=D:\\Kafka\zookeeper-3.4.9\\tmp

 5、 添加系统变量：ZOOKEEPER_HOME=D:\Kafka\zookeeper-3.4.9

 6、 编辑path系统变量，添加路径：%ZOOKEEPER_HOME%\bin

 7、 在zoo.cfg文件中修改默认的Zookeeper端口（默认端口2181）

 8、 打开新的cmd，输入“zkServer“，运行Zookeeper

 9、 命令行提示如下：说明本地Zookeeper启动成功
 注意：不要关了这个窗口

 三、安装Kafka
 1、 下载安装包

 http://kafka.apache.org/downloads

 注意要下载二进制版本
 2、 解压并进入Kafka目录，笔者：D:\Kafka\kafka_2.12-0.11.0.0

 3、 进入config目录找到文件server.properties并打开

 4、 找到并编辑log.dirs=D:\Kafka\kafka_2.12-0.11.0.0\kafka-logs

 5、 找到并编辑zookeeper.connect=localhost:2181

 6、 Kafka会按照默认，在9092端口上运行，并连接zookeeper的默认端口：2181

 7、 进入Kafka安装目录D:\Kafka\kafka_2.12-0.11.0.0，按下Shift+右键，选择“打开命令窗口”选项，打开命令行，
 输入如下进行启动：
 .\bin\windows\kafka-server-start.bat .\config\server.properties
 可以编写成bat文件方便启动

 注意：注意：不要关了这个窗口，启用Kafka前请确保ZooKeeper实例已经准备好并开始运行
 假如windows版启动出错，提示 命令语法不正确。 系统找不到指定的路径,
 确认jdk的目录是否包含空格，建议重新安装JDK ,不要放在带空格的文件夹下，或者将kafka-run-class.bat文件中
 set COMMAND= %JAVA% %KAFKA_OPTS% %KAFKA_JMX_OPTS% -cp %CLASSPATH% %*
 修改为
 set COMMAND= %JAVA% %KAFKA_OPTS% %KAFKA_JMX_OPTS% -cp "%CLASSPATH%" %*

 此时可能还会报： 系统找不到指定的路径
 此时应该重启电脑就解决了（网上也有人这样解决，具体原因不详）。

 四、测试
 1、 创建主题，进入Kafka安装目录D:\Kafka\kafka_2.12-0.11.0.0，按下Shift+右键，选择“打开命令窗口”选项，打开命令行，输入：

 .\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

 注意：不要关了这个窗口

 查看主题输入：

 .\bin\windows\kafka-topics.bat --list --zookeeper localhost:2181

 2、 创建生产者，进入Kafka安装目录D:\Kafka\kafka_2.12-0.11.0.0，按下Shift+右键，选择“打开命令窗口”选项，打开命令行，输入：

 .\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test

 注意：不要关了这个窗口

 3、 创建消费者，进入Kafka安装目录D:\Kafka\kafka_2.12-0.11.0.0，按下Shift+右键，选择“打开命令窗口”选项，打开命令行，输入：

 .\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
 4、 大功告成

 二、介绍：
 Kafka是一个分布式流数据系统，使用Zookeeper进行集群的管理。与其他消息系统类似，整个系统由生产者、Broker Server和消费者三部分组成，生产者和消费者由开发人员编写，通过API连接到Broker Server进行数据操作。我们重点关注三个概念：

 Topic，是Kafka下消息的类别，类似于RabbitMQ中的Exchange的概念。这是逻辑上的概念，用来区分、隔离不同的消息数据，屏蔽了底层复杂的存储方式。对于大多数人来说，在开发的时候只需要关注数据写入到了哪个topic、从哪个topic取出数据。
 Partition，是Kafka下数据存储的基本单元，这个是物理上的概念。同一个topic的数据，会被分散的存储到多个partition中，这些partition可以在同一台机器上，也可以是在多台机器上，比如下图所示的topic就有4个partition，分散在两台机器上。这种方式在大多数分布式存储中都可以见到，比如MongoDB、Elasticsearch的分片技术，其优势在于：有利于水平扩展，避免单台机器在磁盘空间和性能上的限制，同时可以通过复制来增加数据冗余性，提高容灾能力。为了做到均匀分布，通常partition的数量通常是Broker Server数量的整数倍。
 Consumer Group，同样是逻辑上的概念，是Kafka实现单播和广播两种消息模型的手段。同一个topic的数据，会广播给不同的group；同一个group中的worker，只有一个worker能拿到这个数据。换句话说，对于同一个topic，每个group都可以拿到同样的所有数据，但是数据进入group后只能被其中的一个worker消费。group内的worker可以使用多线程或多进程来实现，也可以将进程分散在多台机器上，worker的数量通常不超过partition的数量，且二者最好保持整数倍关系，因为Kafka在设计时假定了一个partition只能被一个worker消费（同一group内）。
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2019-05-22 22:47
 * @Version: 1.0
 */
package com.latico.commons.mq.kafka;
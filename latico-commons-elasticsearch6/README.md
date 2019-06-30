 解读Elasticsearch：
 
 定位： ElasticSearch作为高扩展分布式搜索引擎，主要满足于海量数据实时存储与检索、全文检索与复查查询、统计分析。在如今大数据时代已经成为较popular的存储选择。
 特点： 由于Elasticsearch使用java作为开发语言、使用lucene作为核心处理索引与检索，尤其是使用简单的RestApi隐藏lucene的复杂，使得上手非常容易、海量数据索引与检索极快。es集群由于分片和副本的机制实现了自动容错、高可用、易扩展。
 开源且流行： Elasticsearch支持插件机制，社区活跃度高、官网更新频繁：提供了分析插件、同步插件、hadoop插件、es-sql插件、可视化插件、性能监控插件等，可以让我们站在巨人的肩膀上专心研究搜索需求
 不支持： 不支持频繁更新、关联查询、事务
 最优部署架构
 角色划分
 es分为三种角色： master、client、data，三种角色根据elasticsearch.yml配置中node.master、node.data区分，分别为true false、false false、true true
 
 master： 该节点不和应用创建连接，主要用于元数据(metadata)的处理，比如索引的新增、删除、分片分配等，master节点不占用io和cpu，内存使用量一般
 
 client： 该节点和检索应用创建连接、接受检索请求，但其本身不负责存储数据，可当成负载均衡节点，client节点不占用io、cpu、内存
 
 data： 该节点和索引应用创建连接、接受索引请求，该节点真正存储数据，es集群的性能取决于该节点个数（每个节点最优配置情况下），data节点会占用大量的cpu、io、内存
 
 各节点间关系： master节点具备主节点的选举权，主节点控制整个集群元数据。client节点接受检索请求后将请求转发到与查询条件相关的的data节点的分片上，data节点的分片执行查询语句获得查询结果后将结果反馈至client，在client对数据进行聚合、排序等操作将最终结果返回给上层请求
 资源规划
 master节点： 只需部署三个节点，每个节点jvm分配2-10G，根据集群大小决定
 client节点： 增加client节点可增加检索并发,但检索的速度还是取决于查询所命中的分片个数以及分片中的数据量。如果不清楚检索并发，初始节点数可设置和data节点数一致，每个节点jvm分配2-10
 data节点： ①单个索引在一个data节点上分片数保持在3个以内；②每1GB堆内存对应集群的分片保持在20个以内；③每个分片不要超过30G。
 data节点经验：
 如果单索引每个节点可支撑90G数据，依此可计算出所需data节点数 。
 如果是多索引按照单个data节点jvm内存最大30G来计算，一个节点的分片保持在600个以内，存储保持在18T以内。
 主机的cpu、io固定，建议一台主机只部署一个data节点，不同角色节点独立部署，方便扩容
 每条数据保持在2k以下索引性能大约3000-5000条/s/data节点，增加data节点数可大幅度增加索引速率，节点数与索引效率的增长关系呈抛物线形状​
 优秀的插件与工具
 ik分词器： es默认分词器只支持英文分词，ik分词器支持中文分词
 
 head数据查询工具： 类似于mysql的Navicat
 
 logstash： 数据处理管。采样各种样式、大小的数据来源，实时解析和转换数据，选择众多输出目标导出数据
 
 x-pack性能监控： 获取进程运行时资源与状态信息并存储至es中。可通过kibana查看es、logstash性能指标，试用版包括集群状态、延迟、索引速率、检索速率、内存、cpu、io、磁盘、文件量等还可以看到集群数据负载均衡时的情况。商用版还支持安全、告警等功能
 
 kibana可视化工具： es的可视化工具可制作各种图表，可在该工具上执行dsl语句灵活操作es
 
 es-sql： 用sql查询elasticsearch的工具，将封装复杂的dsl语句封装成sql
 
 beats： 轻量级的数据采集工具，可监控网络流量、日志数据、进程信息（负载、内存、磁盘等），支持docker镜像的file采集
 
 repository-hdfs： 该插件支持将es中离线数据转存至hdfs中长期存储
 
 
 windows安装elasticsearch
 一、下载地址： https://www.elastic.co/cn/downloads/
1、安装JDK,至少1.8以上。
2、下载和解压缩Elasticsearch安装包。
下载地址：https://www.elastic.co/cn/downloads
3、启动Elasticsearch：bin/elasticsearch.bat
4、检查是否启动成功：http://localhost:9200/?pretty

 二、 可视化工具：Kibana
Kibana 是一个ElasticSearch的管理工具，它也提供了对ES集群操作的API。
启动Kibana：bin/Kibana.bat
访问：http://localhost:5601
 
 三、客户端工具 ElasticSearch-Head
 关于ElasticSearch-Head官方介绍比较简单，只有一句话：
 ElasticSearch-Head 是一个与Elastic集群（Cluster）相交互的Web前台。
 ES-Head的主要作用
 
 它展现ES集群的拓扑结构，并且可以通过它来进行索引（Index）和节点（Node）级别的操作
 它提供一组针对集群的查询API，并将结果以json和表格形式返回
 它提供一些快捷菜单，用以展现集群的各种状态
 1、如果要使用elasticsearch-head
 
 ealsticsearch只是后端提供各种api，那么怎么直观的使用它呢？elasticsearch-head将是一款专门针对于elasticsearch的客户端工具

 elasticsearch-head配置包，下载地址：https://github.com/mobz/elasticsearch-head

 elasticsearch-head是一个基于node.js的前端工程，启动elasticsearch-head的步骤如下（这里针对的是elasticsearch 5.x以上的版本）：

 注意：先安装node.js环境才能执行如下命令
 选择对应系统下载node.js：https://nodejs.org/en/download/
 node -v 显示Node.js的版本说明已经安装成功
 npm -v 自带的npm已经安装成功

 　　1、进入elasticsearch-head的文件夹，如：D:\xwj_github\elasticsearch-head
 　　2、执行 npm install
 　　3、执行 npm run start
 在浏览器访问http://localhost:9100，可看到如下界面，表示启动成功，
 如果不能连接，如果是linux下，这个是因为不能将elasticsearch-head放到plugins文件夹下，
 同时，还是无法正常连接到elasticsearch服务，这是因为elasticsearch服务与elasticsearch-head之间可能存在跨越，修改elasticsearch配置即可，
 在elastichsearch.yml中添加如下命名即可： 
#allow origin
http.cors.enabled: true
http.cors.allow-origin: "*"


Elasticsearch 关键字：索引，类型，字段，索引状态，mapping，文档

1. 索引（_index）
索引：说的就是数据库的名字。
我这个说法是对应到咱经常使用的数据库。

结合es的插件 head 来看。

通过Java程序连接Elasticsearch
　　需要注意的是，上一章节我们通过浏览器http://192.168.1.140:9200访问可以正常访问，这里需要知晓，9200端口是用于Http协议访问的，如果通过客户端访问需要通过9300端口才可以访问


测试所使用的配置好的mycat在doc目录

# mycat是什么？
    从定义和分类来看，它是一个开源的分布式数据库系统，是一个实现了MySQL协议的Server，前端用户可以把它看做是一个数据库代理，用MySQL客户端工具和命令行访问，而其后端可以用MySQL原生（Native）协议与多个MySQL服务器通信，也可以用JDBC协议与大多数主流数据库服务器通信，其核心功能是分库分表，即将一个大表水平分割为N个小表，存储在后端MySQL服务器里或者其他数据库里。
    Mycat发展到目前版本，已经不在是一个单纯的MySQL代理了，它的后端可以支持MySQL、SQL Server、Oracle、DB2、PostgreSQL等主流数据库，也支持MongoDB这种新型NOSQL方式的存储，未来还会支持更多类型的存储。而在最终用户看来，无论是那种存储方式，在Mycat里，都是一个传统的数据库表，支持标准的SQL语句进行数据的操作，这样一来，对前端业务系统来说，可以大幅度降低开发难度，提升开发速度，在测试阶段，可以将一表定义为任何一种Mycat支持的存储方式，比如MySQL的MyASM表、内存表、或者MongoDB、LeveIDB以及号称是世界上最快的内存数据库MemSQL上。
    
# 数据存储的演进
    单库单表
        单库单表是最常见的数据库设计，例如，有一张用户(user)表放在数据库db中，所有的用户都可以在db库中的user表中查到。
    
    单库多表
        随着用户数量的增加，user表的数据量会越来越大，当数据量达到一定程度的时候对user表的查询会渐渐的变慢，从而影响整个DB的性能。如果使用mysql, 还有一个更严重的问题是，当需要添加一列的时候，mysql会锁表，期间所有的读写操作只能等待。
    
    可以通过某种方式将user进行水平的切分，产生两个表结构完全一样的user_0000,user_0001等表，user_0000 + user_0001 + …的数据刚好是一份完整的数据。
    
    多库多表
        随着数据量增加也许单台DB的存储空间不够，随着查询量的增加单台数据库服务器已经没办法支撑。这个时候可以再对数据库进行水平拆分。
    
    分区
    就是把一张表的数据分成N个区块，在逻辑上看最终只是一张表，但底层是由N个物理区块组成的
    分表
    就是把一张表按一定的规则分解成N个具有独立存储空间的实体表。系统读写时需要根据定义好的规则得到对应的字表明，然后操作它。
    分库
    一旦分表，一个库中的表会越来越多

# mycat安装
    1、下载安装包：
    下载地址：https://github.com/MyCATApache/Mycat-download/tree/master/1.6-RELEASE
    2、解压；
    3、配置：server.xml、schema.xml、rule.xml
    4、windows方式启动：  命令行进入bin目录执行：startup_nowrap.bat
    5、linux方式：
    mycat 支持的命令mycat { console : start : pause : resume : stop : restart : install : remove : status }
    Mycat的默认端口号为：8066
    mycat start
    mycat stop
    mycat status
    
# Mycat查表报错find no Route:select * from `db_user`.`users` limit 0, 100
    修改schema.xml的 checkSQLschema=“false”,改为true即可
    <schema name="db_store" checkSQLschema="true" sqlMaxLimit="100">
     当该值为true时，例如我们执行语句select * from TESTDB.company 。mycat会把语句修改为 select * from company 去掉TESTDB。
# 启动报错：Caused by: io.mycat.config.util.ConfigException: Illegal table conf : table [ DEMO ] rule function [ mod-long ] partition size : 3 > table datanode size : 1, please make sure table datanode size = function partition size
    是因为使用了mod-long，而里面的节点数量配置有误，把count配置正确
    <function name="mod-long" class="io.mycat.route.function.PartitionByMod">
    		<!-- how many data nodes -->
    		<property name="count">1</property>
    	</function>

# mycat的架构
    支持数据水平切片（定义为dataNode），对每个切片绑定一个逻辑物理主机dataHost，一个逻辑主机
    可以包含N个负载均衡的物理主机，支持单个切片内的主备、读写分离、集群；
# mycat存在的问题
    1、数据节点拓展的时候，由于路由算法的原因，需要备份和重新迁移数据，比如常用算法是取模算法mod，
    重新修改了节点数量后，mod得到的结果不一样。
     
package com.latico.commons.common.util.xml;

/**
 * <PRE>
 * java原生的SAX流式解析法
 *
 Java原生的XML解析方法之二——SAX方法，原理：通过parse(file,listener)函数用一个listener对xml文件进行查找，按顺序读取文档，遍历每个标签，当发现目标标签
 时，读取标签的属性、结点值等信息并返回。
 优点：
 a、无需将整个xml文档载入内存，因此消耗内存少
 b、可以继承ContentHandler创建多个执行不同查询的listener进行解析操作
 缺点：
 a、不能随机的访问xml中的节点
 b、不能修改文档

 c、查询依次就要对XML文档从头到尾遍历一次



 操作步骤：

 1：创建解析工厂：SAXParserFactory factory = SAXParserFactory.newInstance();

 2：由工厂创建解析器：SAXParser htmlparser = factory.newSAXParser();

 3：通过解析器的parse()方法，对指定xml文档以指定handler之类进行解析查询：htmlparser.parse(xmlFile, new MySaxListener())；
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-26 9:12
 * @Version: 1.0
 */
public class SaxUtils {
}

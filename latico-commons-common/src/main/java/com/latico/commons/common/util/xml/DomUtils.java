package com.latico.commons.common.util.xml;

/**
 * <PRE>
 * java原生的DOM方式解析
 *
 Java自身原生的两种解析XML方式之一——DOM方法，原理是：首先在内存中创建一个Document对象，然后把XML文档读取进来赋值给这个dom对象。由于dom对象是基于树结构的，所以对dom对象进行遍历即可。对内存中的dom对象可以进行查询、修改、删除操作，还可以写回原XML文档保存修改。

 优点：
 a、由于整棵树在内存中，因此可以对xml文档随机访问
 b、可以对xml文档进行修改操作
 缺点：
 a、整个文档必须一次性解析完
 a、由于整个文档都需要载入内存，对于大文档成本高
 * </PRE>
 *
 * @Author: latico
 * @Date: 2018-12-26 9:11
 * @Version: 1.0
 */
@Deprecated
public class DomUtils {
}

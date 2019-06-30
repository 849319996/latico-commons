package com.latico.commons.common.util.xml;

/**
 * <PRE>
 JDOM方法是根据DOM方法的众多繁琐操作进行包装得到的，上面我们看到，DOM方法解析XML文档其实是很繁琐的，而且很混乱，标签、属性、换行空格都当作结点类型来处理。JDOM方法定义了一系列通俗、好记的方法来解析XML，方法的底层封装了一系列DOM操作，但是我们不必亲自去进行这些繁琐的工作了。

 优点：

 a、DOM方式的优点:查找方便，可以修改
 缺点
 a、DOM方式的缺点:装载整个文档,对内存容量要求高

 在JDOM中，同一了根节点、普通结点、属性等全为Element类型。

 操作步骤：

 1：创建一个SAXbuilder：SAXBuilder builder = new SAXBuilder();

 2：创建文件输入流打开xml文件：InputStream in = new FileInputStream("XXX.xml");

 3：通过builder，从输入流读取xml文件创建dom对象：Document dom = builder.build(in);

 4：获取根节点：Element root=dom.getRootElement();

 5：获取子节点列表：List<Element> childNodes = node.getChildren();

 6：遍历子节点列表，获取第i个结点：Element node = childNodes.get(i);

 7：读取结点信息：

 1）结点属性值：node.getAttributeValue("属性名")；

 2）结点名：node.getName()；

 3）结点值：node.getValue();

 4）子结点文本值：node.getChildText("子结点名")；
 * </PRE>
 *
 * @Author: LanDingDong
 * @Date: 2018-12-26 9:17
 * @Version: 1.0
 */
@Deprecated
public class JdomUtils {
}

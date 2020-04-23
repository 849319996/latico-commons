package com.latico.commons.common.util.xml;

import com.thoughtworks.xstream.XStream;

import java.util.Map;

/**
 * <PRE>
 * xstream的XML和bean互转工具
 * 对命名空间和节点前缀的支持，使用起来可能比较麻烦，建议解析前，先把前缀替换掉
 *
 *
 Xstream介绍
 Xstream是一种OXMapping 技术，是用来处理XML文件序列化的框架,在将JavaBean序列化，或将XML文件反序列化的时候，不需要其它辅助类和映射文件，使得XML序列化不再繁索。Xstream也可以将JavaBean序列化成Json或反序列化，使用非常方便。
 特点:
 简化的API; 无映射文件; 高性能,低内存占用; 整洁的XML; 不需要修改对象;支持内部私有字段,不要求对private属性提供set/get方法;类不需要默认构造器
 Xstream注解常用知识：

 注解
 作用目标

 @XStreamAlias("message") 别名注解  类,字段

 @XStreamImplicit 忽略集合  集合字段

 @XStreamAsAttribute 将字段转换成节点属性  字段

 @XStreamOmitField 忽略属性  字段

 @XStreamConverter(SingleValueCalendarConverter.class) 注入转换器  对象

 重点就是学会它的注解和不通过注解直接用它API来MAPPING
 其中次点是XML节点与MODEL属性名不一样的处理

 Xstream的简单例子
 添加依赖
 compile('com.thoughtworks.xstream:xstream:1.4.7')

 //xml数据
 <bus_station>
 <aid>268216</aid>
 <latitude>34.055596</latitude>
 <longitude>-118.113722</longitude>
 <name>
 <![CDATA[ E Graves Ave - Los Angeles, CA ]]>
 </name>
 <station_city>16</station_city>
 </bus_station>

 //JavaBean
 //如果不给类起别名，则对应的是类包名，例：com.common.entry.BusStation
 @XStreamAlias("bus_station")
 public class BusStation implements Serializable {
 //属性别名注解
 @XStreamAlias("aid")
 private String aid;
 //如果和xml的节点一致就可以不使用别名
 private Double latitude;

 private Double longitude;

 private String name;
 @XStreamAlias("station_city")
 private String stationCity;
 @XStreamOmitField()
 private String time;
 }

 public class Test
 {
 public static void main(String[] args)
 {
 BusStation bean=new BusStation();
 XStream xstream = new XStream();
 xstream.ignoreUnknownElements();
 xstream.processAnnotations(BusStation.class);

 //XML序列化
 String xml = xstream.toXML(bean);
 System.out.println(xml);

 //XML反序列化
 bean=(Person)xstream.fromXML(xml);
 System.out.println(bean);
 }
 }



 xml和bean的映射mapping
 1.通过注解映射
 //设置类的别名
 @XStreamAlias("person")

 //设置字段的别名
 @XStreamAlias("username")
 private String name;

 //将此字段名在XML中去掉
 @XStreamImplicit()
 private List<Address> addressList = new ArrayList<Address>();

 2.通过api映射
 //设置类的别名
 xstream.alias("person",Person.class);
 stream.alias("address1", Address.class);

 //设置字段的别名
 stream.aliasField("username", Person.class, "name");

 //去掉集合类型生成xml的父节点
 stream.addImplicitCollection(Person.class, "addressList");

 //将name字段作为Person的属性
 //作用等同注解@XStreamAsAttribute()
 stream.useAttributeFor(Person.class, "name");

 //将此字段名在XML中去掉，去掉集合类型生成xml的父节点
 xStream.addImplicitCollection(Person.class, "addressList");

 //注册使用了注解的Person类
 stream.processAnnotations(new Class[]{People.class, Place.class});

 3.默认的命名空间
 定义一个默认的XML命名空间使得我们在子元素的开始标记中不需要使用前缀。他的语法如下所示:
 <element xmlns="namespace">

 下面的XML文档在table元素中包含了水果的信息:
 <table xmlns="http://www.w3.org/TR/html4/">
 <tr>
 <td>Apples</td>
 <td>Bananas</td>
 </tr>
 </table>

 4.XStreamImplicit注解使用
 当需要将collection或map类型的成员变量中数据转换成xml相同层次的元素时，可以在该成员变量使用该注解。
 @XStreamImplicit(itemFieldName = "List_Element")
 private List<JP> jps;
 转换的xml如下：

 <People Age_Alias="10" name="sedric">
 <List_Element>
 <gender>man</gender>
 <reason>人傻钱多</reason>
 </List_Element>
 <List_Element>
 <gender>woman</gender>
 <reason>巧言令色</reason>
 </List_Element>
 </People>

 XStreamImplicit注解有两个属性：

 itemFieldName是指当前集合数据转换为xml元素时的 elementName；
 keyFieldName在集合元素为复杂对象时，会使用集合元素的成员变量名作为元素的elementName，当集合元素为 基本数据类型及String类型时，keyFieldName指定的值将作为元素的elementName。

 工具类
 //把xml转为object
 public static <T> T toBean(String xmlStr, Class<T> cls) {
 if (TextUtils.isEmpty(xmlStr) || xmlStr.contains("Error")) {
 return null;
 }
 try {
 XStream xstream = new XStream();
 //忽略不需要的节点
 xstream.ignoreUnknownElements();
 //对指定的类使用Annotations 进行序列化
 xstream.processAnnotations(cls);
 T obj = (T) xstream.fromXML(xmlStr);
 return obj;
 } catch (Exception e) {
 e.printStackTrace();
 }
 return null;
 }

 作者：hongjay
 链接：https://www.jianshu.com/p/a17929392495
 来源：简书
 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
 * </PRE>
 *
 * @author: latico
 * @date: 2019-08-04 0:42
 * @version: 1.0
 */
public class XstreamUtils {


    /**
     * 注解方式，@XStreamAlias注解指定别名
     * @param xml
     * @param types @XStreamAlias注解指定别名
     * @param <T>
     * @return
     */
    public static <T> T xmlToBeanByAnnotation(String xml, Class<?>... types) {

        //创建xstream对象
        XStream xstream = new XStream();

        //注解方式将别名与xml名字对应，types里面的类必须使用@XStreamAlias注解
        xstream.processAnnotations(types);

        //将字符串类型的xml转换为对象
        return (T) xstream.fromXML(xml);
    }


    /**
     * 注解方式，@XStreamAlias注解指定别名
     * @param bean
     * @param types @XStreamAlias注解指定别名
     * @return
     */
    public static String beanToXmlByAnnotation(Object bean, Class<?>... types) {
        //创建xstream对象
        XStream xstream = new XStream();
        //将别名与xml名字对应
        //注解方式将别名与xml名字对应，types里面的类必须使用@XStreamAlias注解
        xstream.processAnnotations(types);

        //将字符串类型的xml转换为对象
        return xstream.toXML(bean);
    }

    /**
     * 手动指定别名
     * @param xml
     * @param classAliasMap
     * @param <T>
     * @return
     */
    public static <T> T xmlToBeanByAlias(String xml, Map<Class, String> classAliasMap) {
        //创建xstream对象
        XStream xstream = new XStream();
        //将别名与xml名字对应
        if (classAliasMap != null) {
            for (Map.Entry<Class, String> entry : classAliasMap.entrySet()) {
                xstream.alias(entry.getValue(), entry.getKey());
            }
        }

        //将字符串类型的xml转换为对象
        return (T) xstream.fromXML(xml);
    }

    /**
     * 手动指定别名
     * @param bean
     * @param classAliasMap
     * @return
     */
    public static String beanToXmlByAlias(Object bean, Map<Class, String> classAliasMap) {
        //创建xstream对象
        XStream xstream = new XStream();
        //将别名与xml名字对应
        if (classAliasMap != null) {
            for (Map.Entry<Class, String> entry : classAliasMap.entrySet()) {
                xstream.alias(entry.getValue(), entry.getKey());
            }
        }

        //将字符串类型的xml转换为对象
        return xstream.toXML(bean);
    }

}

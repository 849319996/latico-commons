package com.latico.commons.net.snmp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * SNMP采集方式枚举类型
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月21日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public enum SnmpCollectTypeEnum {
	
	Get(0, "Get", null),
	GetNext(0, "GetNext", null),
	Table(0, "Table", null),
	Walk(0, "Walk", null),
	;

	/** nameEnumMap 名字和枚举对象的映射 */
	private final static Map<String, SnmpCollectTypeEnum> nameEnumMap = new HashMap<String, SnmpCollectTypeEnum>();

	/** idEnumMap ID值和对象的映射 */
	private final static Map<Integer, SnmpCollectTypeEnum> idEnumMap = new HashMap<Integer, SnmpCollectTypeEnum>();
	
	static {
		SnmpCollectTypeEnum[] values = values();
		for (SnmpCollectTypeEnum value : values) {
			nameEnumMap.put(value.getName().toLowerCase(), value);// 统一转换成小写
			idEnumMap.put(value.id, value);
		}
	}

	/** name 枚举名称，必须有，并且统一转换成小写 */
	private String name;
	
	/** value 枚举存储的值 */
	private String value;

	/** id  */
	private int id;

	/**
	 * 构造函数
	 * 枚举参数初始化
	 * @param id
	 * @param name
	 */
	private SnmpCollectTypeEnum(int id, String name, String value) {
		this.name = name;
		this.id = id;
		this.value = value;
	}
	
	/**
	 * 获取当前枚举对象的枚举名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 获取当前对象的枚举ID
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}

	/**
	 * 通过枚举的名称获取枚举对象
	 * @param name
	 * @return
	 */
	public static SnmpCollectTypeEnum getEnumByName(String name){
		if(name != null){
			return nameEnumMap.get(name.toLowerCase());//统一转换成小写
		}else{
			return null;
		}
	}

	/**
	 * 通过枚举的ID获取枚举对象
	 * @param id
	 * @return
	 */
	public static SnmpCollectTypeEnum getEnumById(int id){
		return idEnumMap.get(id);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" [").append("id").append("=").append(id).append("]");
		sb.append(" [").append("name").append("=").append(name).append("]");
		sb.append(" [").append("value").append("=").append(value).append("]");
		return sb.toString();
	}
}

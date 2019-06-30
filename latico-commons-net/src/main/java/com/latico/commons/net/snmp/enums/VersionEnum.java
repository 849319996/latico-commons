package com.latico.commons.net.snmp.enums;


import com.latico.commons.common.util.string.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * <PRE>
 * 枚举类型
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月21日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public enum VersionEnum {
	
	V1(1, "V1", null), 
	V2C(2, "V2C", null), 
	V2(2, "V2", null), 
	V3(3, "V3", null),
	;

	/** nameEnumMap 名字和枚举对象的映射 */
	private final static Map<String, VersionEnum> nameEnumMap = new HashMap<String, VersionEnum>();

	/** idEnumMap ID值和对象的映射 */
	private final static Map<Integer, VersionEnum> idEnumMap = new HashMap<Integer, VersionEnum>();
	
	static {
		VersionEnum[] values = values();
		for (VersionEnum value : values) {
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
	private VersionEnum(int id, String name, String value) {
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
	public static VersionEnum getEnumByName(String name){
		if(name != null){
			return nameEnumMap.get(name.toLowerCase());//统一转换成小写
		}else{
			return null;
		}
	}
	
	/**
	 * 通过名称或者ID查找枚举
	 * @param nameOrId
	 * @return
	 */
	public static VersionEnum getEnumByNameOrId(Object nameOrId){
		if(nameOrId != null){
			String s = nameOrId.toString();
			VersionEnum envm = getEnumByName(s);
			if(envm == null){
				if(StringUtils.isNumber(s)){
					envm = idEnumMap.get(Integer.parseInt(s));
				}
			}
			return envm;
		}else{
			return null;
		}
	}

	/**
	 * 通过枚举的ID获取枚举对象
	 * @param id
	 * @return
	 */
	public static VersionEnum getEnumById(int id){
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
	
	public static void main(String[] args) {
		System.out.println(getEnumByNameOrId("v1"));
		System.out.println(getEnumByNameOrId("2"));
		System.out.println(getEnumByNameOrId("v2c"));
	}
}

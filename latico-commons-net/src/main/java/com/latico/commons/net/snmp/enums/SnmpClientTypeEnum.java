package com.latico.commons.net.snmp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * <PRE>
 * 终端类型枚举
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年3月21日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public enum SnmpClientTypeEnum {
	
	Snmp4j("Snmp4j", null), 
	Adventnet("Adventnet", null),
	;
	
	private final static Map<String, SnmpClientTypeEnum> nameValue = new HashMap<String, SnmpClientTypeEnum>();
	static{
		SnmpClientTypeEnum[] values = values();
		for(SnmpClientTypeEnum value : values){
			nameValue.put(value.getName().toLowerCase(), value);//统一转换成小写
		}
	}
	/** name 枚举名称，必须有，并且统一转换成小写 */
	private String name;
	
	/** value 枚举存储的值 */
	private String value;

	private SnmpClientTypeEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public static SnmpClientTypeEnum getEnumByName(String name){
		if(name != null){
			return nameValue.get(name.toLowerCase());//统一转换成小写
		}else{
			return null;
		}
	}
}

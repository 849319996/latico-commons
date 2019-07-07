package com.latico.commons.net.trap.bean;

/**
 * <PRE>
 * trap结果的OID和值的绑定
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年10月20日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class VariableBind {

	/** oid OID */
	private String oid;
	
	/** name OID的名字 */
	private String name;

	/** type OID类型 */
	private String type;

	/** value 该OID的值 */
	private String value;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "VariableBinding [oid=" + oid + ", type=" + type + ", value="
		        + value + "]";
	}

}

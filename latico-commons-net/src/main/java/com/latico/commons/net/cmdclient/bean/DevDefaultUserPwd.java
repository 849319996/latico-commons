package com.latico.commons.net.cmdclient.bean;

/**
 * <PRE>
 * 设备默认的用户名密码
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年6月26日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class DevDefaultUserPwd {

	private String manufacturer;
	
	private String connType;

	private String model;

	private boolean enable;

	private String enablePwd;

	private String username;

	private String password;

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getEnablePwd() {
		return enablePwd;
	}

	public void setEnablePwd(String enablePwd) {
		this.enablePwd = enablePwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}

	@Override
	public String toString() {
		return "DevDefaultUserPwd [manufacturer=" + manufacturer + ", connType="
		        + connType + ", model=" + model + ", enable=" + enable
		        + ", enablePwd=" + enablePwd + ", username=" + username
		        + ", password=" + password + "]";
	}

}

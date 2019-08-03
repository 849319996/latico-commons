package com.latico.commons.common.util.xml.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("account")
public class Account {
	
	private int accoutId;
	private String accountName;
	private double accountMoeny;
	private String telNum;
	public int getAccoutId() {
		return accoutId;
	}
	public void setAccoutId(int accoutId) {
		this.accoutId = accoutId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public double getAccountMoeny() {
		return accountMoeny;
	}
	public void setAccountMoeny(double accountMoeny) {
		this.accountMoeny = accountMoeny;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	
	@Override
	public String toString() {
		return "Account [accoutId=" + accoutId + ", accountName=" + accountName + ", accountMoeny=" + accountMoeny
				+ ", telNum=" + telNum + "]";
	}
}
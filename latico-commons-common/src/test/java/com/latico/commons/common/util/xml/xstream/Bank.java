package com.latico.commons.common.util.xml.xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("bank")
public class Bank {
	
	private String name;
	
	private String address;
//	@XStreamImplicit()
	private List<Account> accounts;
 
	public String getName() {
		return name;
	}
 
	public void setName(String name) {
		this.name = name;
	}
 
	public String getAddress() {
		return address;
	}
 
	public void setAddress(String address) {
		this.address = address;
	}
 
	public List<Account> getAccounts() {
		return accounts;
	}
 
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
 
	@Override
	public String toString() {
		return "Bank [name=" + name + ", address=" + address + ", accounts=" + accounts + "]";
	}
}

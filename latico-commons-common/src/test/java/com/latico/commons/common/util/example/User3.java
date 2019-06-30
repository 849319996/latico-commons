package com.latico.commons.common.util.example;


/**
 * 以注解标识的类
 * @author liuyazhuang
 *
 */
@BaseInfo(name="liuyazhuang", age = 18, hobby = {"Java", "C", "Python", "Go"})
public class User3 extends User2 {
	
	@Gender(gender = Gender.GenderEnum.GIRL)
	private String sex;

	@Gender(gender = Gender.GenderEnum.BOY)
	private String sex4;

	@Override
	@BaseInfo(name="liuyazhuang3", age = 19, hobby = {"Java", "C", "Python", "Go"})
	public String getSex() {
		return sex;
	}

	@BaseInfo(name="liuyazhuang4", age = 20, hobby = {"Java", "C", "Python", "Go"})
	public String getSex4() {
		return sex;
	}
 
	@Override
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
package com.latico.commons.common.util.example;


/**
 * 以注解标识的类
 * @author liuyazhuang
 *
 */
@BaseInfo(name="liuyazhuang", age = 18, hobby = {"Java", "C", "Python", "Go"})
public class User {
	
	@Gender(gender = Gender.GenderEnum.BOY)
	private String sex;
	@Gender(gender = Gender.GenderEnum.BOY)
	private String sex2;

	@BaseInfo(name="liuyazhuang1", age = 17, hobby = {"Java", "C", "Python", "Go"})
	protected String getSex() {
		return sex;
	}

	@BaseInfo(name="liuyazhuang2", age = 18, hobby = {"Java", "C", "Python", "Go"})
	protected String getSex2() {
		return sex;
	}
	@BaseInfo(name="liuyazhuang2", age = 18, hobby = {"Java", "C", "Python", "Go"})
	protected String getSex(Integer i) {
		return sex;
	}


	protected void setSex(String sex) {
		this.sex = sex;
	}
	
}
package com.latico.commons.freemarker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class FreemarkerTest {

    FreemarkerUtil fu = null;

    @Before
    public void init() {
        if (fu == null) {
            fu = new FreemarkerUtil();
        }
    }

    @Test
    public void test1() {
//1.创建数据模型
        Map<String, Object> root = new HashMap<String, Object>();
//2.赋值
        root.put("user_name", "胖先生");
//3.将数据模型和模版进行结合输出到控制台显示
        fu.print("demo.ftl", root);


    }

    @Test
    public void test2() {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("username", "王风");//在ftl中要赋值的变量
        /*FreemarkerUtil util=new FreemarkerUtil();
         */
        fu.fprint("01.ftl", root, "01.html");
    }

    @Test
    public void test3() {
        Map<String, Object> root = new HashMap<String, Object>();
        User user = new User();
        user.setId(1011);
        user.setName("汪峰");
        user.setAge(52);
        root.put("user", user);//在ftl中要赋值的变量
        /*FreemarkerUtil util=new FreemarkerUtil();
         */
        fu.fprint("03.ftl", root, "01.html");
    }

    @Test
    public void test4() {
        Map<String, Object> root = new HashMap<String, Object>();
        User user = new User();
        user.setId(1011);
        user.setName("汪峰");
        user.setAge(52);
        User user1 = new User();
        user1.setId(1012);
        user1.setName("章子怡");
        user1.setAge(15);
        List<User> users = new ArrayList<User>();
        users.add(user);
        users.add(user1);
        root.put("users", users);//在ftl中要赋值的变量
        fu.fprint("04.ftl", root, "04.html");
    }

    @Test
    public void test5() {
        Map<String, Object> root = new HashMap<String, Object>();
        User user = new User();
        user.setId(1011);
        user.setName("汪峰");
        user.setAge(52);
        Group group = new Group();
        group.setName("雅昌");
        user.setGroup(group);
        root.put("user", user);//在ftl中要赋值的变量
        root.put("book", "美丽说");
        fu.fprint("06.ftl", root, "06.html");
    }

    @Test
    public void test6() {
        Map<String, Object> root = new HashMap<String, Object>();
        User user = new User();
        user.setId(1011);
        user.setName("汪峰");
        user.setAge(52);
        Group group = new Group();
        group.setName("雅昌");
        user.setGroup(group);
        Date date = new Date();
        root.put("user", user);//在ftl中要赋值的变量
        root.put("book", "a美丽说");
        root.put("date", date);
        int number = 21;
        root.put("number", number);
        Boolean foo = true;
        root.put("foo", foo);
        fu.fprint("06.ftl", root, "06.html");
    }

}
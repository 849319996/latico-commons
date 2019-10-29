package com.latico.commons.common.util.other;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

public class OptionalUtilsTest {

    @Test
    public void getOptional() {
        Optional<Integer> optional = OptionalUtils.getOptional(123);

        System.out.println(optional.isPresent());
        System.out.println(optional.toString());
        System.out.println(optional.get());
    }

    /**
     *
     */
    @Test
    public void test1() {
        Optional<User> OptionalUser = Optional.ofNullable(null);

        System.out.println("-optional-ofNullable:" + OptionalUser.get());//Exception in thread "main" java.util.NoSuchElementException: No value present
    }

    /**
     *
     */
    @Test
    public void test2() {

        Optional<String> OptionalString = Optional.ofNullable("abc");
        System.out.println(OptionalString.map((v) -> v.toUpperCase()));//Optional[ABC]
    }

    /**
     *
     */
    @Test
    public void test3() {
        // Java 8之前：
//        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
//        for (String feature : features) {
//            System.out.println(feature);
//        }

// Java 8之后：
        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features.forEach((a) -> {
            System.out.println(a);
        });


// 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
// 看起来像C++的作用域解析运算符
//        features.forEach(System.out::println);
    }

    /**
     *
     */
    @Test
    public void test4() {
        Optional<Goods> goodsOptional = Optional.ofNullable(new Goods());
        Optional<List<Order>> orders1 = goodsOptional.flatMap(g -> Optional.ofNullable(g.getOrderList()));
        List<Order> orders = goodsOptional.flatMap(g -> Optional.ofNullable(g.getOrderList())).orElse(Collections.emptyList());
    }

    @Test
    public void handle() {
//        Optional<List<Order>> handle = OptionalUtils.map(new Goods(), g -> Optional.ofNullable(g.getOrderList()));
//        System.out.println(handle.get());
//        Optional<Goods> goodsOptional = Optional.ofNullable(new Goods());
//        Optional<List<Order>> orders1 = goodsOptional.flatMap(g -> Optional.ofNullable(g.getOrderList()));
//        List<Order> orders = goodsOptional.flatMap(g -> Optional.ofNullable(g.getOrderList())).orElse(Collections.emptyList());

    }

    @Test
    public void filter() {
        Goods goods = new Goods();
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setOrderName("name1");
        orders.add(order);

        order = new Order();
        order.setOrderName("nam2");
        orders.add(order);

        order = new Order();
        order.setOrderName("nm1");
        orders.add(order);

        goods.setOrderList(orders);

        Optional<List<Order>> handle = OptionalUtils.map(goods, g -> {
            System.out.println(g.getOrderList());
            for (Order order1 : g.getOrderList()) {
                if (order1.getOrderName().equals("nam2")) {
                    order1.setOrderName("name2");
                }
            }
            return g.getOrderList();});
        List<Order> orders1 = handle.get();
        List<Order> list = StreamUtils.filter(orders1, (t) -> t.getOrderName().contains("name"));
        System.out.println(list);

        Optional<List<Order>> filter = OptionalUtils.filter(handle, (t) -> t.size() > 0);
        System.out.println(filter);

    }
}


class User {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age='").append(age).append('\'');
        sb.append('}');
        return sb.toString();
    }
}


class Goods {
    private String goodsName;
    private double price;
    private List<Order> orderList;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}

class Order {
    private LocalDateTime createTime;
    private LocalDateTime finishTime;
    private String orderName;
    private String orderUser;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("createTime=").append(createTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", orderName='").append(orderName).append('\'');
        sb.append(", orderUser='").append(orderUser).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
package com.latico.commons.cglib;

public class UserServiceImpl {
    /**
     * 只要能被子类重写，那就能被代理到
     */
    public void addUser() {
        System.out.println("增加一个用户。。。");

//        为了测试方法内部调用其他被代理的方法
        deleteUser();
        queryUser();
        editUser();
    }

    /**
     * 只要能被子类重写，那就能被代理到
     */
    public void deleteUser() {
        System.out.println("删除一个用户。。。");
    }

    /**
     * 因为是继承方式，所以该方法不会被代理，调用的话就是简单的执行
     */
    private void queryUser() {
        System.out.println("查询一个用户。。。");
    }

    /**
     * 只要能被子类重写，那就能被代理到
     */
    protected void editUser() {
        System.out.println("编辑一个用户。。。");
    }
}
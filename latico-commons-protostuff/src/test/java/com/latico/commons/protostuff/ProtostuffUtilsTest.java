package com.latico.commons.protostuff;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class ProtostuffUtilsTest {

    @Test
    public void getSchema() {
    }

    @Test
    public void serializeToString() throws UnsupportedEncodingException {
        User user = new User();
        user.setName("xiaoming");
        user.setNameCn("小明");

        String str = ProtostuffUtils.serializeToString(user);
        System.out.println(str);
    }

    @Test
    public void deserializeFromString() throws UnsupportedEncodingException {
        User user = new User();
        user.setName("xiaoming");
        user.setNameCn("小明");

        String str = ProtostuffUtils.serializeToString(user);
        System.out.println(str);
        System.out.println(ProtostuffUtils.deserializeFromString(str, User.class));
    }

    @Test
    public void serializeToByte() {
        User user = new User();
        user.setName("xiaoming");
        user.setNameCn("小明");

        byte[] bytes = ProtostuffUtils.serializeToByte(user);
        System.out.println(bytes);
    }

    @Test
    public void deserializeFromByte() {
        User user = new User();
        user.setName("xiaoming");
        user.setNameCn("小明");

        byte[] bytes = ProtostuffUtils.serializeToByte(user);
        User user1 = ProtostuffUtils.deserializeFromByte(bytes, User.class);
        System.out.println(user1);
    }
}

class User{
    private String name;
    private String nameCn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", nameCn='").append(nameCn).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
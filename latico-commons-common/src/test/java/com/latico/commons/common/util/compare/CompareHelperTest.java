package com.latico.commons.common.util.compare;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: latico
 * @date: 2018/12/05 15:59
 * @version: 1.0
 */
public class CompareHelperTest {

    List<CompareBean> newObjs = new ArrayList<>();
    List<CompareBean> oldObjs = new ArrayList<>();
    
    List<CompareBean3> newObjs3 = new ArrayList<>();
    List<CompareBean3> oldObjs3 = new ArrayList<>();
    @Before
    public void setUp() throws Exception {
        //构造新对象
        makeNewObj();

        //构造旧对象
        makeOldObj();


        makeNewObj3();
        makeOldObj3();
    }

    @Test
    public void diffCompareDataByExtends() {
        try {
            System.out.println(CompareHelper.diffCompareByExtends(newObjs, oldObjs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void diffCompareDataByAnnotion() {

        try {
            System.out.println(CompareHelper.diffCompareByAnnotation(newObjs, oldObjs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void diffCompareData() {

        try {
            String autoIncrementFieldName = "id";
            String[] comapreKeyRelatedFieldNames = new String[]{"name", "nickName"};
            String[] compareValueRelatedFieldNames = new String[]{"age", "sex"};
            System.out.println(CompareHelper.diffCompareData(newObjs, oldObjs, autoIncrementFieldName, comapreKeyRelatedFieldNames, compareValueRelatedFieldNames));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeNewObj() {
        int id = 0;
        CompareBean obj = null;

        obj = new CompareBean();
        //故意加两次
        newObjs.add(obj);
        newObjs.add(obj);
        id = 11;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 12;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 13;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 14;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);
        obj.setRemark("新");

        obj = new CompareBean();
        newObjs.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(123);
        obj.setRemark("新");
    }

    private void makeOldObj() {
        int id = 0;
        CompareBean obj = null;

        obj = new CompareBean();
        oldObjs.add(obj);
        id = 11;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("旧");

        obj = new CompareBean();
        oldObjs.add(obj);
        id = 12;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);
        obj.setRemark("旧");

        obj = new CompareBean();
        oldObjs.add(obj);
        id = 13;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("旧");

        obj = new CompareBean();
        oldObjs.add(obj);
        id = 14;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);
        obj.setRemark("旧");

        obj = new CompareBean();
        oldObjs.add(obj);
        id = 16;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);
        obj.setRemark("旧");
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void diffCompareByHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        Object obj4 = new Object();
        Object obj5 = new Object();

        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3);
        System.out.println(obj4);
        System.out.println(obj5);

        List<Object> newObjs = new ArrayList<>();
        newObjs.add(obj1);
        newObjs.add(obj2);
        newObjs.add(obj2);
        newObjs.add(obj3);


        List<Object> oldObjs = new ArrayList<>();
        oldObjs.add(obj2);
        oldObjs.add(obj4);
        oldObjs.add(obj5);

        System.out.println(CompareHelper.diffCompareByHashCode(newObjs, oldObjs));
    }


    @Test
    public void diffCompareByHashCode2() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        Object obj4 = new Object();
        Object obj5 = new Object();

        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3);
        System.out.println(obj4);
        System.out.println(obj5);

        List<Object> newObjs = new ArrayList<>();
        newObjs.add(obj1);
        newObjs.add(obj2);
        newObjs.add(obj2);
        newObjs.add(obj3);


        List<Object> oldObjs = new ArrayList<>();
        oldObjs.add(obj2);
        oldObjs.add(obj4);
        oldObjs.add(obj5);

        System.out.println(CompareHelper.diffCompareByHashCode(newObjs, null));
    }

    @Test
    public void diffCompareDataByAnnotion3() {

        try {
            System.out.println(CompareHelper.diffCompareByAnnotation(newObjs3, oldObjs3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeNewObj3() {
        int id = 0;
        CompareBean3 obj = null;

        obj = new CompareBean3();
        //故意加两次
        newObjs3.add(obj);
        newObjs3.add(obj);
        id = 11;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 12;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 13;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 14;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);

        obj = new CompareBean3();
        newObjs3.add(obj);
        id = 15;
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(123);
    }

    private void makeOldObj3() {
        int id = 0;
        CompareBean3 obj = null;

        obj = new CompareBean3();
        oldObjs3.add(obj);
        id = 11;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        oldObjs3.add(obj);
        id = 12;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);

        obj = new CompareBean3();
        oldObjs3.add(obj);
        id = 13;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        oldObjs3.add(obj);
        id = 14;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("男");
        obj.setAge(id);

        obj = new CompareBean3();
        oldObjs3.add(obj);
        id = 16;
        obj.setId(id);
        obj.setName("name" + id);
        obj.setNickName("nickName" + id);
        obj.setSex("女");
        obj.setAge(id);
    }
}
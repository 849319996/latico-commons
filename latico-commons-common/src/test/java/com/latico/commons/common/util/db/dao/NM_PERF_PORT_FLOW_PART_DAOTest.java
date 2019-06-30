package com.latico.commons.common.util.db.dao;

import com.latico.commons.common.util.bean.NM_PERF_PORT_FLOW_PART;
import com.latico.commons.common.util.time.DateTimeUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

public class NM_PERF_PORT_FLOW_PART_DAOTest {

    /**
     *
     */
    @Test
    public void test1() throws ParseException {
        NM_PERF_PORT_FLOW_PART_DAO dao = new NM_PERF_PORT_FLOW_PART_DAO();

        NM_PERF_PORT_FLOW_PART obj = new NM_PERF_PORT_FLOW_PART();

        Date date = DateTimeUtils.toDateBy_ymdhms("2015-10-2 08:00:00");
        obj.setCOLLECTTIME(date);
        obj.setOBJID("1");
        obj.setSUBOBJID("1");
        obj.setDAYID(date);

        obj.setWEEKID(4L);
        obj.setMINUTECOUNT(1330L);
        obj.setSUBOBJNAME("GigabitEthernet1/1/1");

        System.out.println(obj);

        System.out.println(dao.insert(obj));

    }

    /**
     *
     */
    @Test
    public void test2() throws ParseException {
        NM_PERF_PORT_FLOW_PART_DAO dao = new NM_PERF_PORT_FLOW_PART_DAO();

        NM_PERF_PORT_FLOW_PART obj = new NM_PERF_PORT_FLOW_PART();

        Date date = DateTimeUtils.toDateBy_ymdhms("2016-10-2 08:10:00");
        obj.setCOLLECTTIME(date);
        obj.setOBJID("1");
        obj.setSUBOBJID("2");
        obj.setDAYID(date);

        obj.setWEEKID(4L);
        obj.setMINUTECOUNT(1330L);
        obj.setSUBOBJNAME("GigabitEthernet1/1/2");

        System.out.println(obj);

        System.out.println(dao.insert(obj));

    }

    /**
     *
     */
    @Test
    public void test3() throws ParseException {
        NM_PERF_PORT_FLOW_PART_DAO dao = new NM_PERF_PORT_FLOW_PART_DAO();

        NM_PERF_PORT_FLOW_PART obj = new NM_PERF_PORT_FLOW_PART();

        Date date = DateTimeUtils.toDateBy_ymdhms("2016-12-3 08:10:00");
        obj.setCOLLECTTIME(date);
        obj.setOBJID("1");
        obj.setSUBOBJID("3");
        obj.setDAYID(date);

        obj.setWEEKID(4L);
        obj.setMINUTECOUNT(1330L);
        obj.setSUBOBJNAME("GigabitEthernet1/1/3");

        System.out.println(obj);

        System.out.println(dao.insert(obj));

    }

}
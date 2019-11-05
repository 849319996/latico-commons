package com.latico.commons.net.trap.impl;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class Snmp4jTrapReceiverTest {

    /**
     *
     */
    @Test
    public void test(){
        Snmp4jTrapReceiver trapReceiver = new Snmp4jTrapReceiver();
        trapReceiver.init();

        trapReceiver.startListen();


        ThreadUtils.sleep(1000000);
    }
}
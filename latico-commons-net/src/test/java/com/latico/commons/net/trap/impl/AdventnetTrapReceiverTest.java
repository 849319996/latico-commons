package com.latico.commons.net.trap.impl;

import com.latico.commons.common.util.thread.ThreadUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdventnetTrapReceiverTest {
    /**
     *
     */
    @Test
    public void test(){
        AdventnetTrapReceiver trapReceiver = new AdventnetTrapReceiver();
        trapReceiver.init();

        trapReceiver.startListen();

        System.out.println("启动完成");


        ThreadUtils.sleep(1000000);
    }
}
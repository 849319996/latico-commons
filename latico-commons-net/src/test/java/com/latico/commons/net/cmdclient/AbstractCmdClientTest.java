package com.latico.commons.net.cmdclient;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractCmdClientTest {

    @Test
    public void name(){
        System.out.println(AbstractCmdClient.checkHasFailInfo("% Local password error\n" +
                "Username:", ""));
    }
}
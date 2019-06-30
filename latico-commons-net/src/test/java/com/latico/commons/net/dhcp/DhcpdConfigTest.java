package com.latico.commons.net.dhcp;

import com.latico.commons.common.util.logging.LogUtils;
import com.latico.commons.net.dhcp.server.DhcpdConfig;
import org.junit.Test;

public class DhcpdConfigTest {

    @Test
    public void test() {
        LogUtils.loadLogBackConfigDefault();
        System.out.println(DhcpdConfig.getInstance());
    }
}
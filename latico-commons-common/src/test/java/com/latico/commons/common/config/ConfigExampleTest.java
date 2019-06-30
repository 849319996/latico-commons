package com.latico.commons.common.config;

import com.latico.commons.common.util.logging.LogUtils;
import org.junit.Test;

public class ConfigExampleTest {

    @Test
    public void loadOrRefreshConfig() {
        LogUtils.loadSunLoggerFromResources("/config/logger.properties");
        ConfigExample.getInstance();
        System.out.println(ConfigExample.getInstance().getName2());
        System.out.println(ConfigExample.getInstance().getNameInt());
        System.out.println(ConfigExample.getInstance().getNameAnnotation());
        System.out.println(ConfigExample.getInstance().getName5());

    }

    /**
     *
     */
    @Test
    public void test(){
        System.out.println(ConfigExample.getInstance().toString());
    }
}
package com.latico.commons.common.config;

import com.latico.commons.common.config.bean.Common;
import com.latico.commons.common.config.bean.ConfigInfo;
import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.io.IOUtils;
import com.latico.commons.common.util.xml.XstreamUtils;
import com.latico.commons.common.util.xml.xstream.Account;
import com.latico.commons.common.util.xml.xstream.Bank;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigExample2Test {


    /**
     *
     */
    @Test
    public void test2(){
        System.out.println(ConfigExample2.getInstance().toString());

    }
    /**
     *
     */
    @Test
    public void test() throws IOException {
        String xml = IOUtils.resourceToString("config/configExample.xml");
        ConfigInfo bean = XstreamUtils.xmlToBeanByAnnotation(xml, ConfigInfo.class, Common.class);
        System.out.println(bean);
    }

}
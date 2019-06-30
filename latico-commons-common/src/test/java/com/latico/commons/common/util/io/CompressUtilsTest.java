package com.latico.commons.common.util.io;

import org.junit.Test;

public class CompressUtilsTest {

    @Test
    public void compress() {
        System.out.println(CompressUtils.compress("D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\latico-commons\\latico-commons-common\\logs\\BACKUP"));
    }

    @Test
    public void extract() {
        System.out.println(CompressUtils.extract("D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\latico-commons\\latico-commons-common\\logs\\bak\\log.log.zip"));
    }


    @Test
    public void extract2() {
        System.out.println(CompressUtils.extract("D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\latico-commons\\latico-commons-common\\logs\\bak\\BACKUP.zip"));
    }


    @Test
    public void toZip() {
    }

    @Test
    public void toTar() {
    }

    @Test
    public void toGZip() {
        System.out.println(CompressUtils.toGZip("D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\latico-commons\\latico-commons-common\\logs\\log.log", "D:\\eclipse\\eclipse-jee-photon-R-win32-x86_64\\eclipse-workspace\\latico-commons\\latico-commons-common\\logs\\log.log.gzip"));
    }

    @Test
    public void unZip() {

    }

    @Test
    public void unTar() {
    }

    @Test
    public void unGZip() {
    }
}
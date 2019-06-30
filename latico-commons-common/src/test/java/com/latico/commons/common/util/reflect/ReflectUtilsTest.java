package com.latico.commons.common.util.reflect;

import org.junit.Test;

public class ReflectUtilsTest {

    @Test
    public void getInterfaceImplClassUnderPackage() {
    }

    @Test
    public void getPackageAllClassName() {
    }

    @Test
    public void getClassesUnderPackage() throws Exception {
        System.out.println(ResourcesUtils.getAllClassesUnderPackage("com.latico.commons.common.util.json"));
    }

    @Test
    public void findAndAddClassesUnderPackageByFile() {
    }

    @Test
    public void getAllResourcesNameUnderPackage() throws Exception {
        System.out.println(ResourcesUtils.getAllResourcesNameUnderPackage("org.quartz.utils", "((?i)\\S+?\\.class|latico-commons-common.kotlin_module)", false));
    }
}
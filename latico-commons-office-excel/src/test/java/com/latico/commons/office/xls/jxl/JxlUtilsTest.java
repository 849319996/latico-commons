package com.latico.commons.office.xls.jxl;

import java.io.File;

import static org.junit.Assert.*;

public class JxlUtilsTest {

    public static void main(String[] args) {
        String[] columnNames = new String[]{"a", "b", "c"};
        Object[][] values = {{"21","11"},{"21","22"}}; File file = new File("./12.xls");
        String sheetName="abc";
        JxlUtils.writeExcel(file, sheetName, columnNames, values);
        System.out.println("执行完成");
    }
}
package com.latico.commons.common.util.io;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.Assert.*;

public class RafUtilsTest {

    @Test
    public void openRandomAccessFileByReadWrite() {
        try {
            System.out.println(RafUtils.readLines("src/test/resources/raf.txt", 0, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void openRandomAccessFileByReadWrite2() {
        try {
            System.out.println(RafUtils.insertData("src/test/resources/raf.txt", 10, "abcd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readBytes() {
        try {
            System.out.println(new String(RafUtils.readBytes("src/test/resources/raf.txt", 0, 0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
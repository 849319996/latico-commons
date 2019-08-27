package com.latico.commons.snappy;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class SnappyUtilsTest {

    @Test
    public void compressToStr() throws IOException {
        String str = "你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.";
        String data = SnappyUtils.compressToStr(str);

        System.out.println(data);
    }

    @Test
    public void uncompressToStr() throws IOException {

        String str = "你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.你好，abcd,.";
        byte[] compress = SnappyUtils.compress(str);

        System.out.println(new String(compress, "UTF-8"));

        byte[] uncompress = SnappyUtils.uncompress(compress);
        System.out.println(new String(uncompress, "UTF-8"));
    }
}
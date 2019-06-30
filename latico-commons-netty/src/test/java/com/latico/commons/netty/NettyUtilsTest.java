package com.latico.commons.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class NettyUtilsTest {

    /**
     * netty零拷贝场景
     */
    @Test
    public void zeroCopyDemo(){
        byte[] arr1 = new byte[2];
        arr1[0] = 1;
        arr1[1] = 2;
        System.out.println(arr1);
        byte[] arr2 = new byte[3];
        arr2[0] = 3;
        arr2[1] = 4;
        arr2[2] = 5;
        System.out.println(arr2);
        ByteBuf byteBuf = Unpooled.wrappedBuffer(arr1, arr2);

        ByteBuf slice = byteBuf.slice(0, 3);
        while (slice.isReadable()) {
            System.out.println(slice.readByte());
        }

    }
}
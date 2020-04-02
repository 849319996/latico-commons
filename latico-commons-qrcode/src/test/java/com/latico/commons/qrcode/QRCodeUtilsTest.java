package com.latico.commons.qrcode;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class QRCodeUtilsTest {

    /**
     *
     */
    @Test
    public void test(){
        try {
            // 存放在二维码中的内容
            // String text = "这里可以存储JSON数据";

            //这里设置一个地址，扫码后会自动跳转
            String text = "https://www.baidu.com";

            // 嵌入二维码的图片路径
            String imgPath = "file/img/dog.jfif";
            // 生成的二维码的路径及名称
            String destPath = "file/qrcode/qrcode.jpg";
            //生成二维码
            QRCodeUtils.encode(text, imgPath, destPath, true);
            // 解析二维码
            String str = QRCodeUtils.decode(destPath);
            // 打印出解析出的内容
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @Test
    public void test2(){
        File file = new File("file/test/abc.txt");
        file.mkdirs();
    }
}
package com.latico.commons.net.ftp.client.impl;

import com.jcraft.jsch.JSchException;
import com.latico.commons.net.ftp.client.FtpClient;
import org.junit.Test;

import static org.junit.Assert.*;

public class SftpClientImplTest {

    /**
     * 
     */
    @Test
    public void test()  {
        FtpClient ftpClient = null;
        try {
            ftpClient = new SftpClientImpl("192.168.173.128", 22, "landingdong", "landingdong", 1000000);
            //        localFiles.add("./data/test.txt");
//        localFiles.add("./data/test2.txt");
            ftpClient.upload("C:\\project\\data-push\\data/test.txt", "data");
            ftpClient.upload("C:\\project\\data-push\\data/test2.txt", "data");


        } catch (JSchException e) {
            e.printStackTrace();
        }finally {
            ftpClient.close();
        }

    }
}
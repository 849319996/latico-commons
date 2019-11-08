package com.latico.commons.net.ftp.client;

import com.jcraft.jsch.JSchException;
import com.latico.commons.net.ftp.client.impl.FtpClientImpl;
import com.latico.commons.net.ftp.client.impl.SftpClientImpl;
import org.junit.Test;

public class FtpClientTest {

    /**
     *
     */
    @Test
    public void testSftpClientImpl(){
        FtpClient ftpClient = null;
        try {
            ftpClient = new SftpClientImpl("172.168.10.7", 22, "cattsoft", "cattsoft", 15000);

            System.out.println(ftpClient.listDirs("."));
        } catch (JSchException e) {
            e.printStackTrace();
        }finally {
            if (ftpClient != null) {
                ftpClient.close();
            }

        }
    }

    /**
     * FTP模式
     */
    @Test
    public void testFtpClientImpl(){
        FtpClient ftpClient = null;
        try {
            ftpClient = new FtpClientImpl("localhost", 21, "cattsoft", "cattsoft", 15000);

            System.out.println(ftpClient.listDirs("."));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                ftpClient.close();
            }

        }
    }
}
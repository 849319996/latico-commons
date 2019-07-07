package com.latico.commons.docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * <PRE>
 * 服务注册中心
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-03-08 14:48:58
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages={"com.latico.commons.docker"})
//该注解启动一个服务注册中心提供给其他应用进行对话。
@ServletComponentScan
public class ApplicationStarter {

    /**
     * 服务器启动
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
        //开始处理业务流程
        startProcessService();
    }

    private static void startProcessService() {
        System.out.println("开始处理业务");
        int i = 0;

        while (i++ <= 5) {
            System.out.println("业务正在处理");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("处理业务完成");
    }
}
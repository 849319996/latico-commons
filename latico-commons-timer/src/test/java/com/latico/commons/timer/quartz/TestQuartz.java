package com.latico.commons.timer.quartz;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;

/**
 * <PRE>
 * 测试用例
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年11月7日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class TestQuartz {

    private static final Logger LOG = LoggerFactory.getLogger(TestQuartz.class);
    public static String JOB_NAME = "QUARTZ_JOB_NAME";

    public static String TRIGGER_NAME = "QUARTZ_TRIGGER_NAME";

    public static String JOB_GROUP_NAME = "QUARTZ_JOB_GROUP";

    public static String TRIGGER_GROUP_NAME = "QUARTZ_JOB_GROUP";

    public static void main(String[] args) {
//        test1();
        test2();
    }

    public static void test1() {
        try {
/////////////////////////使用默认调度器
            LOG.info("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.getInstance().addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, QuartzJobExample.class, "0/1 * * * * ?");
            QuartzManager.getInstance().addJob(JOB_NAME + 2, JOB_GROUP_NAME, TRIGGER_NAME + 2, TRIGGER_GROUP_NAME, QuartzJobExample.class, "0/2 * * * * ?");

            Thread.sleep(2000);
            LOG.info("【修改时间】开始(每5秒输出一次)...");
            QuartzManager.getInstance().modifyJobTime(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, "0/5 * * * * ?");

            Thread.sleep(3000);
            LOG.info("【移除定时】开始...");
            QuartzManager.getInstance().removeJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME);
            QuartzManager.getInstance().removeJob(JOB_NAME + 2, JOB_GROUP_NAME, TRIGGER_NAME + 2, TRIGGER_GROUP_NAME);
            LOG.info("【移除定时】成功");
            
///////////////////////////////////////////////////////////////////////////
            /////////////////////////创建第二个调度器，自定义线程数量
            QuartzManager.getInstance().createNewScheduler("调度器2", 20);
            LOG.info("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.getInstance().addJob("调度器2", JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, QuartzJobExample.class, "0/1 * * * * ?");
            QuartzManager.getInstance().addJob("调度器2", JOB_NAME + 2, JOB_GROUP_NAME, TRIGGER_NAME + 2, TRIGGER_GROUP_NAME, QuartzJobExample.class, "0/2 * * * * ?");
            
            Thread.sleep(2000);
            LOG.info("【修改时间】开始(每5秒输出一次)...");
            QuartzManager.getInstance().modifyJobTime("调度器2", JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, "0/5 * * * * ?");
            
            Thread.sleep(3000);
            LOG.info("【移除定时】开始...");
            QuartzManager.getInstance().removeJob("调度器2", JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME);
            QuartzManager.getInstance().removeJob("调度器2", JOB_NAME + 2, JOB_GROUP_NAME, TRIGGER_NAME + 2, TRIGGER_GROUP_NAME);
            LOG.info("【移除定时】成功");
            
///////////////////////////////////////////////////////////////////////////
            LOG.info("【停止定时】开始...");
            QuartzManager.getInstance().shutdownAllScheduler();
            LOG.info("【停止定时】成功");
            Thread.sleep(2000);
            LOG.info("【再次启动定时】开始...");
            QuartzManager.getInstance().startAllScheduler();
            LOG.info("【再次启动定时】结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void test2() {
        try {
/////////////////////////使用默认调度器
            LOG.info("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.getInstance().addJob(JOB_NAME, QuartzJobExample.class, "0/1 * * * * ?");
            QuartzManager.getInstance().addJob(JOB_NAME + 2, QuartzJobExample.class, "0/2 * * * * ?");
            
            Thread.sleep(2000);
            LOG.info("【修改时间】开始(每5秒输出一次)...");
            QuartzManager.getInstance().modifyJobTime(JOB_NAME, "0/5 * * * * ?");
            
            Thread.sleep(3000);
            LOG.info("【移除定时】开始...");
            QuartzManager.getInstance().removeJob(JOB_NAME);
            QuartzManager.getInstance().removeJob(JOB_NAME + 2);
            LOG.info("【移除定时】成功");
            
///////////////////////////////////////////////////////////////////////////
            /////////////////////////创建第二个调度器，自定义线程数量
            QuartzManager.getInstance().createNewScheduler("调度器2", 20);
            LOG.info("【系统启动】开始(每1秒输出一次)...");
            QuartzManager.getInstance().addJob("调度器2", JOB_NAME, QuartzJobExample.class, "0/1 * * * * ?");
            QuartzManager.getInstance().addJob("调度器2", JOB_NAME + 2, QuartzJobExample.class, "0/2 * * * * ?");
            
            Thread.sleep(2000);
            LOG.info("【修改时间】开始(每5秒输出一次)...");
            QuartzManager.getInstance().modifyJobTime("调度器2", JOB_NAME, "0/5 * * * * ?");
            
            Thread.sleep(3000);
            LOG.info("【移除定时】开始...");
            QuartzManager.getInstance().removeJob("调度器2");
            QuartzManager.getInstance().removeJob("调度器2", JOB_NAME + 2);
            LOG.info("【移除定时】成功");
            
///////////////////////////////////////////////////////////////////////////
            LOG.info("【停止定时】开始...");
            QuartzManager.getInstance().shutdownAllScheduler();
            LOG.info("【停止定时】成功");
            Thread.sleep(2000);
            LOG.info("【再次启动定时】开始...");
            QuartzManager.getInstance().startAllScheduler();
            LOG.info("【再次启动定时】结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.latico.commons.timer.quartz;

import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @Author: latico
 * @Date: 2019-02-06 21:33
 * @Version: 1.0
 */
public class QuartzUtils {

    public static CronTrigger createCronTrigger(String triggerName, String triggerGroupName, String cronExpression) {
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(triggerName, triggerGroupName);
        triggerBuilder.startNow();
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
        // 创建Trigger对象
        return (CronTrigger)triggerBuilder.build();
    }

    public static JobDetail createJobDetail(String jobName, String jobGroupName, Class<? extends Job> jobClass) {
        return JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
    }

    public static DirectSchedulerFactory createDirectSchedulerFactory(int threadCount) throws SchedulerException {
        DirectSchedulerFactory singletonSchedulerFactory = DirectSchedulerFactory.getInstance();
        singletonSchedulerFactory.createVolatileScheduler(threadCount);
        return singletonSchedulerFactory;
    }

    public static StdSchedulerFactory createStdSchedulerFactory() {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory;
    }

    public static void createScheduler(DirectSchedulerFactory schedulerFactory, String schedulerName, int maxWorkThreadCount) throws SchedulerException {
        //如果已经存在，则不创建
        Scheduler scheduler = getScheduler(schedulerFactory, schedulerName);
        if (scheduler != null) {
            return;
        }
        // 使用默认优先级创建线程池
        SimpleThreadPool threadPool = new SimpleThreadPool(maxWorkThreadCount, Thread.NORM_PRIORITY);
        threadPool.setInstanceName(schedulerName);
        threadPool.initialize();

        // 创建job存储器
        JobStore jobStore = new RAMJobStore();

        // 使用所有参数创建调度程序
        schedulerFactory.createScheduler(schedulerName, schedulerName, threadPool, jobStore);
    }

    /**
     * 通过名称获取调度器
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @param schedulerName 调度器名称
     * @return
     * @throws SchedulerException
     */
    public static Scheduler getScheduler(SchedulerFactory schedulerFactory, String schedulerName) throws SchedulerException {
        if (schedulerName == null) {
            return schedulerFactory.getScheduler();
        } else {
            return schedulerFactory.getScheduler(schedulerName);
        }
    }

    public static void addJobAndStart(SchedulerFactory schedulerFactory, String schedulerName, JobDetail jobDetail, CronTrigger trigger) throws SchedulerException {
        Scheduler scheduler = QuartzUtils.getScheduler(schedulerFactory, schedulerName);

        // 调度容器设置JobDetail和Trigger
        scheduler.scheduleJob(jobDetail, trigger);

        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

}

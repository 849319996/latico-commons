package com.latico.commons.timer.quartz;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <PRE>
 * 任务定时调度执行管理器
 * 触发器：任务定时器；
 * 任务工作类：执行具体任务的类；
 * 调度器：拥有属于自己的线程池、任务工作类、触发器
 * 调度器工厂：因为调度器对象的构造比较麻烦，为了方便生成调度器类而存在。
 * 
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年11月7日</B>
 * @author    <B><a href="mailto:latico@qq.com"> latico </a></B>
 * @since     <B>JDK1.6</B>
 */
public class QuartzManager {
    /** LOG 日志工具 */
    private static final Logger LOG = LoggerFactory.getLogger(QuartzManager.class);
    /** DEFAULT_JOB_GROUP_NAME 默认的工作组名称 */
    public static String DEFAULT_JOB_GROUP_NAME = "QUARTZ_DEFAULT_JOB_GROUP";

    /** DEFAULT_TRIGGER_GROUP_NAME 默认的触发器组名称 */
    public static String DEFAULT_TRIGGER_GROUP_NAME = "QUARTZ_DEFAULT_TRIGGER_GROUP";

    /** instance 单例 */
    private static volatile QuartzManager instance;

    /** schedulerFactory 调度器工厂，获取调度器用 */
    private DirectSchedulerFactory schedulerFactory;

    /** schedulerThreadCount 调度器的工作线程数量 */
    private int defaultSchedulerThreadCount = 10;

    /**
     * 构造函数
     */
    private QuartzManager() {
        try {
            initSchedulerFactory();
        } catch (Exception e) {
            LOG.error("初始化QuartzManager定时器异常", e);
        }
    }

    /**
     * @return 获取单例
     */
    public static QuartzManager getInstance() {
        if (instance == null) {
            synchronized (QuartzManager.class) {
                if (instance == null) {
                    instance = new QuartzManager();
                }
            }
        }
        return instance;
    }
    /**
     * 初始化调度工厂
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @throws SchedulerException 
     */
    private void initSchedulerFactory() throws SchedulerException {
        // 标准的实现模式
        // schedulerFactory = new StdSchedulerFactory();

        // 单例的实现模式
        schedulerFactory = QuartzUtils.createDirectSchedulerFactory(defaultSchedulerThreadCount);
    }

    /**
     * 创建新的调度器
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @param schedulerName 调度器名称
     * @param maxWorkThreadCount 最大的工作线程数量，如果小于等于0，那就使用默认的
     * @return true:创建成功,false:已经存在或者创建失败
     * @throws SchedulerException 
     */
    public boolean createNewScheduler(String schedulerName, int maxWorkThreadCount) {
        if (maxWorkThreadCount <= 0) {
            maxWorkThreadCount = defaultSchedulerThreadCount;
        }
        try {
            QuartzUtils.createScheduler(schedulerFactory, schedulerName, maxWorkThreadCount);
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }

        return true;
    }

    /**
     * 
     * @Description: 添加一个定时任务 
     *  
     * @param schedulerName 调度器名称
     * @param jobName 任务名 
     * @param jobGroupName  任务组名 
     * @param triggerName 触发器名 
     * @param triggerGroupName 触发器组名 
     * @param jobClass  任务执行类
     * @param cronExpression 时间设置，参考quartz说明文档  
     */
    public boolean addJob(String schedulerName, String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> jobClass,
            String cronExpression) {

        try {

            // 任务名，任务组，任务执行类
            JobDetail jobDetail = QuartzUtils.createJobDetail(jobName, jobGroupName, jobClass);
            CronTrigger trigger = QuartzUtils.createCronTrigger(triggerName, triggerGroupName, cronExpression);

            // 获取调度器
            QuartzUtils.addJobAndStart(schedulerFactory, schedulerName, jobDetail, trigger);
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * @Description: 修改一个任务的触发时间
     *  
     * @param schedulerName 调度器名称
     * @param jobName 任务名称
     * @param jobGroupName 任务组名称
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     * @param cronExpression   时间设置，参考quartz说明文档   
     */
    public boolean modifyJobTime(String schedulerName, String jobName, String jobGroupName, String triggerName, String triggerGroupName,
            String cronExpression) {
        try {
            // 获取调度器
            Scheduler scheduler = getScheduler(schedulerName);
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return false;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cronExpression)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                trigger = QuartzUtils.createCronTrigger(triggerName, triggerGroupName, cronExpression);
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
                /** 方式一 ：调用 rescheduleJob 结束 */

                /** 方式二：先删除，然后在创建一个新的Job  */
                // JobDetail jobDetail =
                // sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                // Class<? extends Job> jobClass = jobDetail.getJobClass();
                // removeJob(jobName, jobGroupName, triggerName,
                // triggerGroupName);
                // addJob(jobName, jobGroupName, triggerName, triggerGroupName,
                // jobClass, cron);
                /** 方式二 ：先删除，然后在创建一个新的Job */
            }
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * @Description: 移除一个任务 
     *  
     * @param schedulerName 调度器名称
     * @param jobName 任务名称
     * @param jobGroupName 任务组名称
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     */
    public boolean removeJob(String schedulerName, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            // 获取调度器
            Scheduler scheduler = getScheduler(schedulerName);
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            scheduler.pauseTrigger(triggerKey);// 停止触发器
            scheduler.unscheduleJob(triggerKey);// 移除触发器
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
            return true;
        } catch (Exception e) {
            LOG.error(e);
        }
        return false;
    }

    /**
     * 指定调度器名称启动所有定时任务 
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @param schedulerName 调度器名称
     */
    public void startScheduler(String schedulerName) {
        try {
            // 获取调度器
            Scheduler scheduler = getScheduler(schedulerName);
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 
     * 启动所有调度器的所有定时任务 
     */
    public void startAllScheduler() {
        try {
            Collection<Scheduler> schedulers = schedulerFactory.getAllSchedulers();
            if (schedulers != null) {
                for (Scheduler scheduler : schedulers) {
                    scheduler.start();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 指定调度器名称关闭调度器所有工作
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @param schedulerName 调度器名称
     */
    public void shutdownScheduler(String schedulerName) {
        try {
            // 获取调度器
            Scheduler scheduler = getScheduler(schedulerName);
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 
     * @Description:关闭所有调度器的定时任务 
     */
    public void shutdownAllScheduler() {
        try {
            Collection<Scheduler> schedulers = schedulerFactory.getAllSchedulers();
            if (schedulers != null) {
                List<Scheduler> scheds = new ArrayList<Scheduler>();
                scheds.addAll(schedulers);
                while (!scheds.isEmpty()) {
                    Scheduler scheduler = scheds.get(0);
                    if (!scheduler.isShutdown()) {
                        scheduler.shutdown();
                    }
                    scheds.remove(scheduler);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取调度器
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @return
     * @throws SchedulerException
     */
    @SuppressWarnings("unused")
    private Scheduler getScheduler() throws SchedulerException {
        return schedulerFactory.getScheduler();
    }

    /**
     * 通过名称获取调度器
     * @author <B><a href="mailto:latico@qq.com"> latico </a></B>
     * @param schedulerName 调度器名称
     * @return
     * @throws SchedulerException
     */
    private Scheduler getScheduler(String schedulerName) throws SchedulerException {
        return QuartzUtils.getScheduler(schedulerFactory, schedulerName);
    }

    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param jobName 任务名 
     * @param jobClass  任务执行类
     * @param cronExpression 时间设置，参考quartz说明文档  
     */
    public boolean addJob(String jobName, Class<? extends Job> jobClass, String cronExpression) {
        return addJob(null, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, jobClass, cronExpression);
    }

    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param schedulerName 调度器名称 
     * @param jobName 任务名 
     * @param jobClass  任务执行类
     * @param cronExpression 时间设置，参考quartz说明文档  
     */
    public boolean addJob(String schedulerName, String jobName, Class<? extends Job> jobClass, String cronExpression) {
        return addJob(schedulerName, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, jobClass, cronExpression);
    }
    
    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param jobName 任务名 
     * @param jobGroupName  任务组名 
     * @param triggerName 触发器名 
     * @param triggerGroupName 触发器组名 
     * @param jobClass  任务执行类
     * @param cronExpression 时间设置，参考quartz说明文档  
     */
    public boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class<? extends Job> jobClass, String cronExpression) {
        return addJob(null, jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cronExpression);
    }

    /** 
     * @Description: 修改一个任务的触发时间
     *  
     * @param jobName 任务名称
     * @param jobGroupName 任务组名称
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     * @param cronExpression   时间设置，参考quartz说明文档   
     */
    public boolean modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cronExpression) {
       return modifyJobTime(null, jobName, jobGroupName, triggerName, triggerGroupName, cronExpression);
    }

    /** 
     * @Description: 修改一个任务的触发时间
     *  
     * @param schedulerName 调度器名称
     * @param jobName 任务名称
     * @param cronExpression   时间设置，参考quartz说明文档   
     */
    public boolean modifyJobTime(String schedulerName, String jobName, String cronExpression) {
        return modifyJobTime(schedulerName, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, cronExpression);
    }

    /** 
     * @Description: 修改一个任务的触发时间
     *  
     * @param jobName 任务名称
     * @param cronExpression   时间设置，参考quartz说明文档   
     */
    public boolean modifyJobTime(String jobName, String cronExpression) {
        return modifyJobTime(null, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME, cronExpression);
    }

    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 任务名称
     */
    public boolean removeJob(String jobName) {
        return removeJob(null, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME);
    }

    /** 
     * @Description: 移除一个任务 
     *  
     * @param schedulerName 调度器名称
     * @param jobName 任务名称
     */
    public boolean removeJob(String schedulerName, String jobName) {
        return removeJob(schedulerName, jobName, DEFAULT_JOB_GROUP_NAME, jobName, DEFAULT_TRIGGER_GROUP_NAME);
    }

    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 任务名称
     * @param jobGroupName 任务组名称
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名 
     */
    public boolean removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        return removeJob(null, jobName, jobGroupName, triggerName, triggerGroupName);
    }

    /** 
     * 启动默认调度器的所有定时任务 
     */
    public void startScheduler() {
        startScheduler(null);
    }

    /** 
     * @Description:关闭默认调度器的所有定时任务 
     */
    public void shutdownScheduler() {
        shutdownScheduler(null);
    }

}
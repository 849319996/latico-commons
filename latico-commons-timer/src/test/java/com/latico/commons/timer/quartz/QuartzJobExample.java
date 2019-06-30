package com.latico.commons.timer.quartz;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Timestamp;

/**
 * <PRE>
 * 定时调度的实际工作者JOB，使用示例
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年11月7日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class QuartzJobExample implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzJobExample.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("=============================\r\n" + new Timestamp(System.currentTimeMillis()) + ": 任务执行doing something...\r\n=============================" );
    }

}
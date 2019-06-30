package com.latico.commons.spring.test.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class ConfigBean {

	public static void main(String[] args) {
		System.out.println(ConfigBean.getInstance().toString());
	}

	private static volatile ConfigBean instance;


	public static ConfigBean getInstance() {
		if(instance == null) {
			synchronized (ConfigBean.class) {
				if(instance == null) {
					load();
				}
			}
		}
		return instance;
	}
	
	/** resourcesConfigFile 该类对应的资源配置文件 */
	public final static String resourcesConfigFile= "test/config-bean.xml";
	
	/** autoAssignTask 自动分发任务，不需要界面启动 */
	private boolean autoAssignTask = false;
	
	/** isTestOneNeTask 测试一个网元任务 */
	private boolean testOneNeTask = false;

	/**
	 * 是否推送告警
	 */
	private boolean sendAlarm = true;
	
	/** testObjId 测试的对象ID */
	private String testObjId = null;
	
	/** testSubObjTypeId 测试的对象子类型ID */
	private String testSubObjTypeId = null;
	
	/** assignWaitTime 任务分配前，睡眠等待其他机器启动，默认2分钟 */
	private int assignWaitTime = 2;
	
	/** jobExecInterval 多久执行一次工作,默认5分钟 */
	private int jobExecInterval = 5;
	
	private ConfigBean() {
	}

	private static void load() {
	    try {
			ApplicationContext ctx=new ClassPathXmlApplicationContext(resourcesConfigFile);
			instance =(ConfigBean)ctx.getBean("ConfigBean");
			if(instance.getJobExecInterval() <= 0){
				instance.jobExecInterval = 5;
			}
		}catch(Exception ex) {
	    	ex.printStackTrace();
		}
    }


	public static String getResourcesconfigfile() {
		return resourcesConfigFile;
	}

	public static void setInstance(ConfigBean instance) {
		ConfigBean.instance = instance;
	}


	public void setAutoAssignTask(boolean autoAssignTask) {
		this.autoAssignTask = autoAssignTask;
	}

	public boolean isAutoAssignTask() {
		return autoAssignTask;
	}

	public int getAssignWaitTime() {
		return assignWaitTime;
	}

	public void setAssignWaitTime(int assignWaitTime) {
		this.assignWaitTime = assignWaitTime;
	}



	public int getJobExecInterval() {
		return jobExecInterval;
	}

	public void setJobExecInterval(int jobExecInterval) {
		this.jobExecInterval = jobExecInterval;
	}


	public boolean isTestOneNeTask() {
		return testOneNeTask;
	}

	public void setTestOneNeTask(boolean testOneNeTask) {
		this.testOneNeTask = testOneNeTask;
	}

	public String getTestObjId() {
		return testObjId;
	}

	public void setTestObjId(String testObjId) {
		this.testObjId = testObjId;
	}

	public String getTestSubObjTypeId() {
		return testSubObjTypeId;
	}

	public void setTestSubObjTypeId(String testSubObjTypeId) {
		this.testSubObjTypeId = testSubObjTypeId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ConfigBean{");
		sb.append("autoAssignTask=").append(autoAssignTask);
		sb.append(", testOneNeTask=").append(testOneNeTask);
		sb.append(", sendAlarm=").append(sendAlarm);
		sb.append(", testObjId='").append(testObjId).append('\'');
		sb.append(", testSubObjTypeId='").append(testSubObjTypeId).append('\'');
		sb.append(", assignWaitTime=").append(assignWaitTime);
		sb.append(", jobExecInterval=").append(jobExecInterval);
		sb.append('}');
		return sb.toString();
	}


	public void setSendAlarm(boolean sendAlarm) {
        this.sendAlarm = sendAlarm;
    }

    public boolean isSendAlarm() {
        return sendAlarm;
    }

}

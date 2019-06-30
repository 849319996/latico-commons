package com.latico.commons.common.util.version;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 * 抽象的版本类
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2018年4月3日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public abstract class AbstractVersion {

	/** 系统换行符 \r\n \n */
	public final static String LINE_SEPARATOR = System.getProperty("line.separator");

	/** updateHistory 更新历史 */
	private StringBuffer updateHistory = new StringBuffer();
	
	/** isRead 是否读过了 */
	private AtomicBoolean isRead = new AtomicBoolean(false);
	
	/** updateIndex 版本更新记录索引 */
	private int versionUpdateIndex = 0;
	
	/** index 当前版本的更新索引,每个版本开始都是0 */
	private int currentVersionUpdateIndex = 0;
	
	/** versionNo 版本号 */
	private String versionNo = "版本号";
	
	/** updateDatatime 更新时间 */
	private String updateDatatime = "更新开始时间";
	
	/** author 作者 */
	private String author = "作者名字1 作者名字2";
	
	/**
	 * 构造函数
	 * @param projectName 项目名称
	 * @param projectDesc 项目描述
	 */
	public AbstractVersion(String projectName, String projectDesc){
		addProjectInfo(updateHistory, projectName, projectDesc);
	}
	
	/**
	 * 增加项目信息
	 * @param updateHistory 更新历史
	 * @param projectName 项目名称
	 * @param projectDesc 项目描述
	 */
	private void addProjectInfo(StringBuffer updateHistory, String projectName, String projectDesc) {
		updateHistory.append(LINE_SEPARATOR).append("========================================================").append(LINE_SEPARATOR);
		updateHistory.append("-----项目名称:").append(projectName).append("(").append(projectDesc).append(")-----").append(LINE_SEPARATOR);
		updateHistory.append("========================================================").append(LINE_SEPARATOR);
	}
	
	/**
	 * 获取项目版本信息
	 * @return
	 */
	public String getVersionInfo(){
		if(!isRead.get()){
			synchronized (AbstractVersion.class) {
				if(isRead.get()){
					return updateHistory.toString();
				}else{
					isRead.set(true);
					addUpdateHistory();
					return updateHistory.toString();
				}
			}
			
		}else{
			return updateHistory.toString();
		}
	}

	/**
	 * 添加更新历史，实现类使用
	 */
	protected abstract void addUpdateHistory();

	/**
	 * 增加当前版本信息，实现类使用
	 * @param versionNo 版本号
	 * @param updateDatatime 更新时间
	 * @param author 作者
	 */
	protected void addCurrentVersionInfo(String versionNo, String updateDatatime, String author) {
		addEndLine(updateHistory);
		currentVersionUpdateIndex = 0;
		updateHistory.append("=====更新记录[").append(++versionUpdateIndex).append("]-[").append(versionNo).append("]-[").append(author).append("]-[").append(updateDatatime).append("]=====").append(LINE_SEPARATOR);
	}
	
	/**
	 * 增加结束行，实现类使用
	 * @param updateHistory
	 */
	private void addEndLine(StringBuffer updateHistory) {
		updateHistory.append("--------------------------------------------------------").append(LINE_SEPARATOR);
	}
	
	/**
	 * 添加更新节点
	 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
	 * @param updateTime 更新时间
	 * @param updateContent 更新内容
	 */
	protected void addUpdateNode(String updateTime, String updateContent) {
		updateHistory.append(++currentVersionUpdateIndex).append("-").append(updateTime).append("-").append(updateContent).append(LINE_SEPARATOR);
	}
	
	/**
	 * 添加更新节点
	 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
	 * @param author 更新作者
	 * @param updateTime 更新时间
	 * @param updateContent 更新内容
	 */
	protected void addUpdateNode(String author, String updateTime, String updateContent) {
		updateHistory.append(++currentVersionUpdateIndex).append("-").append(author).append("-").append(updateTime).append("-").append(updateContent).append(LINE_SEPARATOR);
	}
	
	/**
	 * 添加更新节点
	 * @author <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
	 * @param objs
	 */
	protected void addUpdateNode(Object... objs) {
		if(objs == null){
			return;
		}
		updateHistory.append(++currentVersionUpdateIndex);
		for(Object obj : objs){
			updateHistory.append("-").append(obj);
		}
		updateHistory.append(LINE_SEPARATOR);
	}
	
}

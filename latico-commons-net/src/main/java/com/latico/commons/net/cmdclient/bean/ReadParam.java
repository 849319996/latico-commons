package com.latico.commons.net.cmdclient.bean;

/**
 * <PRE>
 * 读取数据的时候的参数设定
 * </PRE>
 * <B>项	       目：</B>
 * <B>技术支持：</B>
 * @version   <B>V1.0 2017年9月18日</B>
 * @author    <B><a href="mailto:latico@qq.com"> 蓝鼎栋 </a></B>
 * @since     <B>JDK1.6</B>
 */
public class ReadParam {

	/** endStrRegex 匹配结尾正则 */
	private String endStrRegex;

	/** notEndStrRegex 匹配非结尾正则 */
	private String notEndStrRegex;

	/** isLogSrcContent 是否打印原始日志 */
	private boolean isLogSrcContent;

	/** isAnswerYes 是否自动回答Yes */
	private boolean isAutoAnswerYes;
	
	/** isAutoAnswer 是否自动回答，默认是true */
	private boolean isAutoAnswer = true;

	/** cutEndLineCount 去掉行尾行数 */
	private int cutEndLineCount;

	/** readTimeout 读取超时时间，单位毫秒 */
	private long readTimeout;

	public String getEndStrRegex() {
		return endStrRegex;
	}

	public void setEndStrRegex(String endStrRegex) {
		this.endStrRegex = endStrRegex;
	}

	public boolean isAutoAnswer() {
		return isAutoAnswer;
	}

	public void setAutoAnswer(boolean isAutoAnswer) {
		this.isAutoAnswer = isAutoAnswer;
	}

	public String getNotEndStrRegex() {
		return notEndStrRegex;
	}

	public void setNotEndStrRegex(String notEndStrRegex) {
		this.notEndStrRegex = notEndStrRegex;
	}

	public boolean isLogSrcContent() {
		return isLogSrcContent;
	}

	public void setLogSrcContent(boolean isLogSrcContent) {
		this.isLogSrcContent = isLogSrcContent;
	}


	public boolean isAutoAnswerYes() {
		return isAutoAnswerYes;
	}

	public void setAutoAnswerYes(boolean isAutoAnswerYes) {
		this.isAutoAnswerYes = isAutoAnswerYes;
	}

	public int getCutEndLineCount() {
		return cutEndLineCount;
	}

	public void setCutEndLineCount(int cutEndLineCount) {
		this.cutEndLineCount = cutEndLineCount;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	@Override
	public String toString() {
		return "ReadParam [endStrRegex=" + endStrRegex + ", notEndStrRegex="
		        + notEndStrRegex + ", isLogSrcContent=" + isLogSrcContent
		        + ", isAutoAnswerYes=" + isAutoAnswerYes + ", isAutoAnswer="
		        + isAutoAnswer + ", cutEndLineCount=" + cutEndLineCount
		        + ", readTimeout=" + readTimeout + "]";
	}

}

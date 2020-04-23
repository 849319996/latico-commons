package com.latico.commons.common.util.system.cli;


/**
 * <PRE>
 *  系统命令行返回结果.
 * </PRE>
 * @author: latico
 * @date: 2019-07-07 18:46:47
 * @version: 1.0
 */
public class CmdResult {

	/** 默认命令结果 */
	public final static CmdResult DEFAULT = new CmdResult();
	
	/** 命令执行结果 */
	protected String info;
	
	/** 命令执行异常码 */
	protected int errCode;
	
	/** 命令执行异常信息 */
	protected String err;
	
	/**
	 * 构造函数
	 */
	public CmdResult() {
		this.info = "";
		this.errCode = 0;
		this.err = "";
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n[ERR-CODE] : ").append(getErrCode());
		sb.append("\r\n[ERR-INFO] : \r\n").append(getErr());
		sb.append("\r\n[EXEC-RST] : \r\n").append(getInfo());
		return sb.toString();
	}
	
}

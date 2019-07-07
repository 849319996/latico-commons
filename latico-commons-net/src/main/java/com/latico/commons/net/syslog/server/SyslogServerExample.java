package com.latico.commons.net.syslog.server;

import com.latico.commons.common.util.io.FileUtils;
import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.thread.thread.AbstractThread;
import org.graylog2.syslog4j.server.*;
import org.graylog2.syslog4j.server.impl.AbstractSyslogServer;
import org.graylog2.syslog4j.server.impl.event.printstream.FileSyslogServerEventHandler;
import org.graylog2.syslog4j.server.impl.event.printstream.PrintStreamSyslogServerEventHandler;
import org.graylog2.syslog4j.server.impl.event.printstream.SystemOutSyslogServerEventHandler;
import org.graylog2.syslog4j.server.impl.net.tcp.TCPNetSyslogServerConfigIF;
import org.graylog2.syslog4j.util.SyslogUtility;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <PRE>
 *  如果是UDP方式启动，那么日志打印应该在{@link PrintStreamSyslogServerEventHandler#event(Object, SyslogServerIF, java.net.SocketAddress,  SyslogServerEventIF)}
 *
 *  在全局配置添加事件处理器即可，
 *  根据{@link AbstractSyslogServer#handleEvent(AbstractSyslogServer.Sessions, SyslogServerIF, java.net.Socket, java.net.SocketAddress, SyslogServerEventIF)}可知，事件处理器，可以实现如下两种接口类型：
 *  1、{@link SyslogServerSessionEventHandlerIF}
 *  2、{@link SyslogServerSessionlessEventHandlerIF}
 *
 * </PRE>
 * @Author: latico
 * @Date: 2019-01-31 18:11:00
 * @Version: 1.0
 */
@SuppressWarnings("JavadocReference")
public class SyslogServerExample extends AbstractThread {
	private static final Logger LOG = LoggerFactory.getLogger(SyslogServerExample.class);
	public static boolean CALL_SYSTEM_EXIT_ON_FAILURE = true;

	/**
	 * 开关状态
	 */
	private AtomicBoolean switchStatus = new AtomicBoolean(true);
	private SyslogServerIF syslogServer;
	private SyslogParamOptions options = null;


	public SyslogServerExample(SyslogParamOptions options, AtomicBoolean appStatus) {
		super("Syslog Server", appStatus);
		this.options = options;
	}


	/**
	 * 打印占用信息
	 * @param problem
	 */
	public static void printUsageInfo(String problem) {
		if (problem != null) {
			LOG.info("Error: " + problem);
			LOG.info("\r\n");
		}
		
		LOG.info("Usage:");
		LOG.info("\r\n");
		LOG.info("SyslogServerExample [-h <host>] [-p <port>] [-o <file>] [-a] [-q] <protocol>");
		LOG.info("\r\n");
		LOG.info("-h <host>    host or IP to bind");
		LOG.info("-p <port>    port to bind");
		LOG.info("-t <timeout> socket timeout (in milliseconds)");
		LOG.info("-o <file>    file to write entries (overwrites by default)");
		LOG.info("\r\n");
		LOG.info("-a           append to file (instead of overwrite)");
		LOG.info("-q           do not write anything to standard out");
		LOG.info("\r\n");
		LOG.info("protocol     Syslog4j protocol implementation (tcp, udp, ...)");
	}

	@Override
	protected void dealProcess() throws IOException {
		if (options.getUsage() != null) {
			printUsageInfo(options.getUsage());
			if (CALL_SYSTEM_EXIT_ON_FAILURE) { System.exit(1); } else { return; }
		}

		if (!options.isQuiet()) {
			LOG.info("SyslogServerExample " + SyslogServer.getVersion());
		}

		if (!SyslogServer.exists(options.getProtocol())) {
			printUsageInfo("Protocol \"" + options.getProtocol() + "\" not supported");
			if (CALL_SYSTEM_EXIT_ON_FAILURE) { System.exit(1); } else { return; }
		}

		this.syslogServer = SyslogServer.getInstance(options.getProtocol());

		SyslogServerConfigIF syslogServerConfig = syslogServer.getConfig();

		if (options.getHost() != null) {
			syslogServerConfig.setHost(options.getHost());
			if (!options.isQuiet()) {
				LOG.info("Listening on host: " + options.getHost());
			}
		}

		if (options.getPort() != null) {
			syslogServerConfig.setPort(Integer.parseInt(options.getPort()));
			if (!options.isQuiet()) {
				LOG.info("Listening on port: " + options.getPort());
			}
		}

		if (options.getTimeout() != null) {
			if (syslogServerConfig instanceof TCPNetSyslogServerConfigIF) {
				((TCPNetSyslogServerConfigIF) syslogServerConfig).setTimeout(Integer.parseInt(options.getTimeout()));
				if (!options.isQuiet()) {
					LOG.info("Timeout: " + options.getTimeout());
				}

			} else {
				System.err.println("Timeout not supported for protocol \"" + options.getProtocol() + "\" (ignored)");
			}
		}

		if (options.getFileName() != null) {
			File file = new File(options.getFileName());
			if(!file.exists()){
				FileUtils.createFile(options.getFileName());
			}
			SyslogServerEventHandlerIF eventHandler = new FileSyslogServerEventHandler(options.getFileName(),options.isAppend());
			//把文件监听事件处理器加进去，接收到内容后会调用该事件的event方法
			syslogServerConfig.addEventHandler(eventHandler);
			if (!options.isQuiet()) {
				LOG.info((options.isAppend() ? "Appending" : "Writing") + " to file: " + options.getFileName());
			}
		}

		if (!options.isQuiet()) {
			SyslogServerEventHandlerIF eventHandler = SystemOutSyslogServerEventHandler.create();
			//把系统SystemOut输出监听事件处理器加进去，接收到内容后会调用该事件的event方法
			syslogServerConfig.addEventHandler(eventHandler);
		}

		if (!options.isQuiet()) {
			LOG.info("\r\n");
		}

		SyslogServer.getThreadedInstance(options.getProtocol());

		//线程不能停，可以加判断逻辑，什么时候让线程停止
		while(isRunning()) {
			SyslogUtility.sleep(3000);
		}
	}

}

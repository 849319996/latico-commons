package com.latico.commons.common.util.io.serialreader;

import com.latico.commons.common.util.logging.Logger;
import com.latico.commons.common.util.logging.LoggerFactory;
import com.latico.commons.common.util.other.PathUtils;
import com.latico.commons.common.util.system.SystemUtils;

class _SerialFlow {

	/** LOG 日志工具 */
	private static final Logger LOG = LoggerFactory.getLogger(_SerialFlow.class);
	
	/** 默认序列化文件位置 */
	protected final static String DEFAULT_FILEPATH = SystemUtils.isRunByTomcat() ?
			PathUtils.getProjectCompilePath().concat("/serializable.dat") : 
			"./serializable.dat";
			
}

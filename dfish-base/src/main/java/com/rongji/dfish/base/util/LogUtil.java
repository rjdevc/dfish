package com.rongji.dfish.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description: 
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年4月20日 下午4:43:40		YuLM			1.0				1.0 Version
 */
public class LogUtil {

	public static final Log LOG = LogFactory.getLog(LogUtil.class);
	
	/**********提供几种常用日志方式**********/
	public static void debug(Object log) {
		debug(log, null);
	}
	
	public static void debug(Object log, Throwable t) {
		if (LOG.isDebugEnabled()) {
			LOG.debug(log, t);
		}
	}
	
	public static void info(Object log) {
		info(log, null);
	}
	
	public static void info(Object log, Throwable t) {
		if (LOG.isInfoEnabled()) {
			LOG.info(log, t);
		}
	}
	
	public static void warn(Object log) {
		warn(log, null);
	}
	
	public static void warn(Object log, Throwable t) {
		if (LOG.isWarnEnabled()) {
			LOG.warn(log, t);
		}
	}
	
	public static void error(Object log) {
		error(log, null);
	}
	
	public static void error(Object log, Throwable t) {
		if (LOG.isErrorEnabled()) {
			LOG.error(log, t);
		}
	}
	
}

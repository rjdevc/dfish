package com.rongji.dfish.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Description:
 * Copyright:   Copyright © 2018
 * Company:     rongji
 *
 * @author lamontYu
 * @version 1.0 Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018年4月20日 下午4:43:40		lamontYu			1.0				1.0 Version
 */
public class LogUtil {

    private static final Log LOG = LogFactory.getLog(LogUtil.class);

    private static final Map<Class<?>, Log> LOGS = Collections.synchronizedMap(new HashMap<>());

    /**
     * 获取Log对象
     *
     * @return Log
     */
    public static Log getLog(Class<?> clz) {
        // FIXME Log是否变成WrappedLog,这里来判断实现
        Log log = LOGS.get(clz);
        if (log == null) {
            log = LogFactory.getLog(clz);
            LOGS.put(clz, log);
        }
        return log;
    }

    /**********提供几种常用日志方式**********/
    public static void debug(Object logMessage) {
        debug(logMessage, null);
    }

    public static void debug(Object logMessage, Throwable t) {
        debug(LOG, logMessage, t);
    }

    public static void debug(Class<?> clz, Object logMessage, Throwable t) {
        debug(getLog(clz), logMessage, t);
    }

    public static void debug(Log log, Object logMessage, Throwable t) {
        log.debug(logMessage, t);
    }

    public static void info(Object logMessage) {
        info(logMessage, null);
    }

    public static void info(Object logMessage, Throwable t) {
        info(LOG, logMessage, t);
    }

    public static void info(Class<?> clz, Object logMessage, Throwable t) {
        info(getLog(clz), logMessage, t);
    }

    public static void info(Log log, Object logMessage, Throwable t) {
        log.info(logMessage, t);
    }

    public static void warn(Object logMessage) {
        warn(logMessage, null);
    }

    public static void warn(Object logMessage, Throwable t) {
        warn(LOG, logMessage, t);
    }

    public static void warn(Class<?> clz, Object logMessage, Throwable t) {
        warn(getLog(clz), logMessage, t);
    }

    public static void warn(Log log, Object logMessage, Throwable t) {
        log.warn(logMessage, t);
    }

    public static void error(Object logMessage) {
        if (logMessage instanceof Throwable) {
            error(null, (Throwable) logMessage);
        } else {
            error(logMessage, null);
        }
    }

    public static void error(Object logMessage, Throwable t) {
        error(LOG, logMessage, t);
    }

    public static void error(Class<?> clz, Object logMessage, Throwable t) {
        error(getLog(clz), logMessage, t);
    }

    public static void error(Log log, Object logMessage, Throwable t) {
        log.error(logMessage, t);
    }

    private static final ExecutorService SINGLE_EXECUTOR = ThreadUtil.newSingleThreadExecutor();

    public static void lazyDebug(Callable<Object> callable) {
        lazyDebug(LOG, callable);
    }

    public static void lazyDebug(Class<?> clz, Callable<Object> callable) {
        lazyDebug(getLog(clz), callable);
    }

    public static void lazyDebug(Log log, Callable<Object> callable) {
        if (log.isDebugEnabled()) {
            SINGLE_EXECUTOR.execute(() -> {
                try {
                    Object o = callable.call();
                    log.debug(o);
                } catch (Exception e) {
                    log.error(null, e);
                }
            });
        }
    }

    public static void lazyInfo(Callable<Object> callable) {
        lazyInfo(LOG, callable);
    }

    public static void lazyInfo(Class<?> clz, Callable<Object> callable) {
        lazyInfo(getLog(clz), callable);
    }

    public static void lazyInfo(Log log, Callable<Object> callable) {
        if (log.isInfoEnabled()) {
            SINGLE_EXECUTOR.execute(() -> {
                try {
                    Object o = callable.call();
                    log.info(o);
                } catch (Exception e) {
                    log.error(null, e);
                }
            });
        }
    }

}

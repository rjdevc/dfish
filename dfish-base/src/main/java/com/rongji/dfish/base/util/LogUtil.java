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
    private static final Map<Class<?>, Log> LOGS = Collections.synchronizedMap(new HashMap<>());
    private static final Log LOG = getLog(LogUtil.class);

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
    public static void debug(Object message) {
        debug(message, null);
    }

    public static void debug(Object message, Throwable t) {
        debug(LOG, message, t);
    }

    public static void debug(Class<?> clz, Object message, Throwable t) {
        debug(getLog(clz), message, t);
    }

    public static void debug(Log log, Object message, Throwable t) {
        log.debug(message, t);
    }

    public static void info(Object message) {
        info(message, null);
    }

    public static void info(Object message, Throwable t) {
        info(LOG, message, t);
    }

    public static void info(Class<?> clz, Object message, Throwable t) {
        info(getLog(clz), message, t);
    }

    public static void info(Log log, Object message, Throwable t) {
        log.info(message, t);
    }

    public static void warn(Object message) {
        warn(message, null);
    }

    public static void warn(Object message, Throwable t) {
        warn(LOG, message, t);
    }

    public static void warn(Class<?> clz, Object message, Throwable t) {
        warn(getLog(clz), message, t);
    }

    public static void warn(Log log, Object message, Throwable t) {
        log.warn(message, t);
    }

    public static void error(Object message) {
        if (message instanceof Throwable) {
            error(null, (Throwable) message);
        } else {
            error(message, null);
        }
    }

    public static void error(Object message, Throwable t) {
        error(LOG, message, t);
    }

    public static void error(Class<?> clz, Object message, Throwable t) {
        error(getLog(clz), message, t);
    }

    public static void error(Log log, Object message, Throwable t) {
        log.error(message, t);
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

    public static void lazyWarn(Callable<Object> callable) {
        lazyWarn(LOG, callable);
    }

    public static void lazyWarn(Class<?> clz, Callable<Object> callable) {
        lazyWarn(getLog(clz), callable);
    }

    public static void lazyWarn(Log log, Callable<Object> callable) {
        if (log.isWarnEnabled()) {
            SINGLE_EXECUTOR.execute(() -> {
                try {
                    Object o = callable.call();
                    log.warn(o);
                } catch (Exception e) {
                    log.error(null, e);
                }
            });
        }
    }

}

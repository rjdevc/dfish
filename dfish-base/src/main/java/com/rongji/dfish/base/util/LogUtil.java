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
     * @param clz Class
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
    /**
     * 以DEBUG级别记录信息
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Object message) {
        debug(message, null);
    }

    /**
     * 以DEBUG级别记录信息
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Object message, Throwable t) {
        debug(LOG, message, t);
    }

    /**
     * 以DEBUG级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Class<?> clz, Object message) {
        debug(clz, message, null);
    }

    /**
     * 以DEBUG级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#debug(Object)
     * @deprecated  一般来说 错误不该用debug级别输出
     */
    public static void debug(Class<?> clz, Object message, Throwable t) {
        debug(getLog(clz), message, t);
    }

    /**
     * 以DEBUG级别记录信息
     * @param log Log实体
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Log log, Object message, Throwable t) {
        log.debug(message, t);
    }

    /**
     * 以INFO级别记录信息
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Object message) {
        info(message, null);
    }

    /**
     * 以INFO级别记录信息
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Object message, Throwable t) {
        info(LOG, message, t);
    }

    /**
     * 以INFO级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Class<?> clz, Object message) {
        info(clz, message, null);
    }

    /**
     * 以INFO级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Class<?> clz, Object message, Throwable t) {
        info(getLog(clz), message, t);
    }

    /**
     * 以INFO级别记录信息
     * @param log Log实体
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Log log, Object message, Throwable t) {
        log.info(message, t);
    }

    /**
     * 以WARN级别记录信息
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Object message) {
        warn(message, null);
    }

    /**
     * 以WARN级别记录信息
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Object message, Throwable t) {
        warn(LOG, message, t);
    }

    /**
     * 以WARN级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Class<?> clz, Object message) {
        warn(clz, message, null);
    }

    /**
     * 以WARN级别记录信息
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Class<?> clz, Object message, Throwable t) {
        warn(getLog(clz), message, t);
    }

    /**
     * 以WARN级别记录信息
     * @param log Log实体
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Log log, Object message, Throwable t) {
        log.warn(message, t);
    }

    /**
     * 以ERROR级别记录信息
     * @param message 日志内容 与其他几个不一样，在ERROR级别的时候，如果这个内容是个Throwable，将直接当做异常处理
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Object message) {
        if (message instanceof Throwable) {
            error(null, (Throwable) message);
        } else {
            error(message, null);
        }
    }

    /**
     * 以ERROR级别记录信息
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Object message, Throwable t) {
        error(LOG, message, t);
    }

    /**
     * 以ERROR级别记录信息
     * @param clz  按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Class<?> clz, Object message, Throwable t) {
        error(getLog(clz), message, t);
    }

    /**
     * 以ERROR级别记录信息
     * @param log Log实体
     * @param message 日志内容
     * @param t 错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Log log, Object message, Throwable t) {
        log.error(message, t);
    }

    private static final ExecutorService SINGLE_EXECUTOR = ThreadUtil.newSingleThreadExecutor();

    /**
     * 延迟用debug级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void lazyDebug(Callable<Object> callable) {
        lazyDebug(LOG, callable);
    }

    /**
     * 延迟用debug级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void lazyDebug(Class<?> clz, Callable<Object> callable) {
        lazyDebug(getLog(clz), callable);
    }

    /**
     * 延迟用debug级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param log Log实体
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     */
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

    /**
     * 迟用info级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void lazyInfo(Callable<Object> callable) {
        lazyInfo(LOG, callable);
    }
    /**
     * 迟用info级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void lazyInfo(Class<?> clz, Callable<Object> callable) {
        lazyInfo(getLog(clz), callable);
    }
    /**
     * 迟用info级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param log Log实体
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     */
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

    /**
     * 迟用warn级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void lazyWarn(Callable<Object> callable) {
        lazyWarn(LOG, callable);
    }

    /**
     * 迟用warn级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void lazyWarn(Class<?> clz, Callable<Object> callable) {
        lazyWarn(getLog(clz), callable);
    }

    /**
     * 迟用warn级别输出日志。日志内容由callable产生
     * 通常lazy系列日志是性能敏感时使用的。callable的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     * @param log Log实体
     * @param callable 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     */
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

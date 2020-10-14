package com.rongji.dfish.base.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

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
    @Deprecated
    private static final Map<Class, Log> LOGS = Collections.synchronizedMap(new HashMap<>());
    @Deprecated
    private static final Log LOG = getLog(LogUtil.class);

    /**
     * DEBUG
     */
    protected static final int LEVEL_DEBUG = 0;
    /**
     * INFO
     */
    protected static final int LEVEL_INFO = 1;
    /**
     * WARN
     */
    protected static final int LEVEL_WARN = 2;
    /**
     * ERROR
     */
    protected static final int LEVEL_ERROR = 3;

    /**
     * 获取Log对象
     *
     * @param clz Class
     * @return Log
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static Log getLog(Class clz) {
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
     *
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Object message) {
        log(LEVEL_DEBUG, LogUtil.class, message, null);
    }

    /**
     * 以DEBUG级别记录信息
     *
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#debug(Object)
     * @deprecated 一般来说 错误不该用debug级别输出
     */
    public static void debug(Object message, Throwable t) {
        log(LEVEL_DEBUG, LogUtil.class, message, t);
    }

    /**
     * 以DEBUG级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void debug(Class clz, Object message) {
        log(LEVEL_DEBUG, clz, message, null);
    }

    /**
     * 以DEBUG级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#debug(Object)
     * @deprecated 一般来说 错误不该用debug级别输出
     */
    public static void debug(Class clz, Object message, Throwable t) {
        log(LEVEL_DEBUG, clz, message, t);
    }

    /**
     * 以DEBUG级别记录信息
     *
     * @param log     Log实体
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#debug(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void debug(Log log, Object message, Throwable t) {
        log(LEVEL_DEBUG, log, message, t);
    }

    /**
     * 以INFO级别记录信息
     *
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Object message) {
        log(LEVEL_INFO, LogUtil.class, message, null);
    }

    /**
     * 以INFO级别记录信息
     *
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Object message, Throwable t) {
        log(LEVEL_INFO, LogUtil.class, message, t);
    }

    /**
     * 以INFO级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Class clz, Object message) {
        log(LEVEL_INFO, clz, message, null);
    }

    /**
     * 以INFO级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void info(Class clz, Object message, Throwable t) {
        log(LEVEL_INFO, clz, message, t);
    }

    /**
     * 以INFO级别记录信息
     *
     * @param log     Log实体
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#info(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void info(Log log, Object message, Throwable t) {
        log(LEVEL_INFO, log, message, t);
    }

    /**
     * 以WARN级别记录信息
     *
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Object message) {
        log(LEVEL_WARN, LogUtil.class, message, null);
    }

    /**
     * 以WARN级别记录信息
     *
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Object message, Throwable t) {
        log(LEVEL_WARN, LogUtil.class, message, t);
    }

    /**
     * 以WARN级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Class clz, Object message) {
        log(LEVEL_WARN, clz, message, null);
    }

    /**
     * 以WARN级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void warn(Class clz, Object message, Throwable t) {
        log(LEVEL_WARN, clz, message, t);
    }

    /**
     * 以WARN级别记录信息
     *
     * @param log     Log实体
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#warn(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void warn(Log log, Object message, Throwable t) {
        log(LEVEL_WARN, log, message, t);
    }

    /**
     * 以ERROR级别记录信息
     *
     * @param message 日志内容 与其他几个不一样，在ERROR级别的时候，如果这个内容是个Throwable，将直接当做异常处理
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Object message) {
        if (message instanceof Throwable) {
            log(LEVEL_ERROR, LogUtil.class, null, (Throwable) message);
        } else {
            log(LEVEL_ERROR, LogUtil.class, message, null);
        }
    }

    /**
     * 以ERROR级别记录信息
     *
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Object message, Throwable t) {
        log(LEVEL_ERROR, LogUtil.class, message, t);
    }

    /**
     * 以ERROR级别记录信息
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     */
    public static void error(Class clz, Object message, Throwable t) {
        log(LEVEL_ERROR, clz, message, t);
    }

    /**
     * 以ERROR级别记录信息
     *
     * @param log     Log实体
     * @param message 日志内容
     * @param t       错误
     * @see org.apache.commons.logging.Log#error(Object, Throwable)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void error(Log log, Object message, Throwable t) {
        log(LEVEL_ERROR, log, message, t);
//        log.error(message, t);
    }

    private static final ExecutorService SINGLE_EXECUTOR = ThreadUtil.newSingleThreadExecutor();

    /**
     * 延迟用debug级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void lazyDebug(Supplier<Object> message) {
        lazyLog(LEVEL_DEBUG, LogUtil.class, message);
    }

    /**
     * 延迟用debug级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     */
    public static void lazyDebug(Class clz, Supplier<Object> message) {
        lazyLog(LEVEL_DEBUG, clz, message);
    }

    /**
     * 延迟用debug级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param log     Log实体
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#debug(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void lazyDebug(Log log, Supplier<Object> message) {
        lazyLog(LEVEL_DEBUG, log, message);
    }

    /**
     * 延迟用info级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void lazyInfo(Supplier<Object> message) {
        lazyLog(LEVEL_INFO, LogUtil.class, message);
    }

    /**
     * 延迟用info级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     */
    public static void lazyInfo(Class clz, Supplier<Object> message) {
        lazyLog(LEVEL_INFO, clz, message);
    }

    /**
     * 延迟用info级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param log     Log实体
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#info(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void lazyInfo(Log log, Supplier<Object> message) {
        lazyLog(LEVEL_INFO, log, message);
    }

    /**
     * 延迟用warn级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void lazyWarn(Supplier<Object> message) {
        lazyLog(LEVEL_WARN, LogUtil.class, message);
    }

    /**
     * 延迟用warn级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param clz     按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     */
    public static void lazyWarn(Class clz, Supplier<Object> message) {
        lazyLog(LEVEL_WARN, clz, message);
    }

    /**
     * 延迟用warn级别输出日志。日志内容由Supplier产生
     * 通常lazy系列日志是性能敏感时使用的。Supplier的内容在 log级别没有enable的时候。
     * 是不会执行的。并且它是延后排队进行的。不会阻断主线程的工作。
     *
     * @param log     Log实体
     * @param message 产生日志内容的回调
     * @see org.apache.commons.logging.Log#warn(Object)
     * @deprecated 后期版本有可能使用slf4j的 Logger。如果使用此方法，以后升级可能需要替换
     */
    @Deprecated
    public static void lazyWarn(Log log, Supplier<Object> message) {
        lazyLog(LEVEL_WARN, log, message);
    }

    /**
     * LOG具体的实现
     * @param level 日志等级
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 日志内容
     * @param t 错误
     */
    @SuppressWarnings("deprecation")
    protected static void log(int level, Class clz, Object message, Throwable t) {
        log(level, getLog(clz), message, t);
    }

    @Deprecated
    private static void log(int level, Log log, Object message, Throwable t) {
        switch (level) {
            case LEVEL_DEBUG:
                log.debug(message, t);
                break;
            case LEVEL_INFO:
                log.info(message, t);
                break;
            case LEVEL_WARN:
                log.warn(message, t);
                break;
            case LEVEL_ERROR:
                log.error(message, t);
                break;
            default:
        }
    }

    /**
     * 延迟LOG具体的实现
     * @param level 日志等级
     * @param clz 按这个类作为标识进行分类。日志配置中可以设置某个类的。某个包的输出级别
     * @param message 产生日志内容的回调
     */
    @SuppressWarnings("deprecation")
    protected static void lazyLog(int level, Class clz, Supplier<Object> message) {
        lazyLog(level, getLog(clz), message);
    }

    @Deprecated
    private static void lazyLog(int level, Log log, Supplier<Object> message) {
        switch (level) {
            case LEVEL_DEBUG:
                if (!log.isDebugEnabled()) {
                    return;
                }
                break;
            case LEVEL_INFO:
                if (!log.isInfoEnabled()) {
                    return;
                }
                break;
            case LEVEL_WARN:
                if (!log.isWarnEnabled()) {
                    return;
                }
                break;
            case LEVEL_ERROR:
                if (!log.isErrorEnabled()) {
                    return;
                }
                break;
            default:
                return;
        }
        SINGLE_EXECUTOR.execute(() -> {
            try {
                switch (level) {
                    case LEVEL_DEBUG:
                        log.debug(message.get());
                        break;
                    case LEVEL_INFO:
                        log.info(message.get());
                        break;
                    case LEVEL_WARN:
                        log.warn(message.get());
                        break;
                    case LEVEL_ERROR:
                        log.error(message.get());
                        break;
                    default:
                }
            } catch (Throwable t) {
                log.error(null, t);
            }
        });
    }

}

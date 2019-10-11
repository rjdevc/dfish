package com.rongji.dfish.base.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 多线程工具类
 * @author LinLW
 *
 */
public class ThreadUtil {
	/**
	 * 线程池实例
	 */
	public static final java.util.concurrent.ExecutorService THREAD_POOL_EXECUTOR_SERVICE =java.util.concurrent.Executors.newCachedThreadPool();
	/**
	 * 使用另一个线程运行这个runable的内容。
	 * 这样当前线程不需要等待它运行结束。
	 * @param runable 可运行的内容
	 */
	public static void execute(Runnable runable){
		THREAD_POOL_EXECUTOR_SERVICE.execute(runable);
	}
	/**
	 * 使用另一个线程运行这个runable的内容。
	 * 当前线程会等待它运行，但最多不超过maxWait毫秒。
	 * 如果超过时间，将不再等待。和invoke 不一样如果等待超时，只是当前线程离开，执行的内容还会继续。
	 * @param runable 可运行的内容
	 */
	public static void execute(Runnable runable,long maxWait){
		Future<Object> f=THREAD_POOL_EXECUTOR_SERVICE.submit(()->{
				runable.run();
				return null;
		});
		try {
			f.get(maxWait, TimeUnit.MILLISECONDS);
		} catch (Exception e) {}
	}
	/**
	 * 使用另一个线程运行这个runable的内容。
	 * 当前线程会等待它运行，但最多不超过maxWait毫秒。
	 * 如果超过时间，将不再等待。和execute 不一样如果等待超时，执行的内容将会被取消。
	 * @param runable 可运行的内容
	 */
	public static void invoke(Runnable runable,long maxWait){
		List<Callable<Object>> cs= Collections.singletonList((Callable)()->{
			runable.run();
			return null;
		});
		try {
			THREAD_POOL_EXECUTOR_SERVICE.invokeAny(cs, maxWait, TimeUnit.MILLISECONDS);
		} catch (Exception e) {}
	}
}

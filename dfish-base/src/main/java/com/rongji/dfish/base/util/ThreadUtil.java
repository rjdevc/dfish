package com.rongji.dfish.base.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程工具类
 * @author LinLW
 *
 */
public class ThreadUtil {
	/**
	 * 线程池实例
	 */
	/**
	 * 使用另一个线程运行这个runable的内容。
	 * 这样当前线程不需要等待它运行结束。
	 * @param runable 可运行的内容
	 */
	public static void execute(Runnable runable){
		getCachedThreadPool().execute(runable);
	}
	/**
	 * 使用另一个线程运行这个runable的内容。
	 * 当前线程会等待它运行，但最多不超过maxWait毫秒。
	 * 如果超过时间，将不再等待。和invoke 不一样如果等待超时，只是当前线程离开，执行的内容还会继续。
	 * @param runable 可运行的内容
	 */
	public static void execute(Runnable runable,long maxWait){
		Future<Object> f=getCachedThreadPool().submit(()->{
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
			getCachedThreadPool().invokeAny(cs, maxWait, TimeUnit.MILLISECONDS);
		} catch (Exception e) {}
	}

	private static final ExecutorService SHARED_THREAD_POOL=newCachedThreadPool();

	/**
	 * 取得到公用的线程池执行服务。
	 * @return
	 */
	public static ExecutorService getCachedThreadPool() {
		return SHARED_THREAD_POOL;
	}

	/**
	 * 新建一个线程池执行服务。
	 * 因为这个线程池没有上线设定，一般来说可以直接使用公用的线程池服务。
	 * 慎用此方法。
	 * @see #getCachedThreadPool()
	 * @return ExecutorService
	 */
	public static ExecutorService newCachedThreadPool() {
		return new ThreadPoolExecutor(0, 200,
				60L, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>());
	}

	/**
	 * 新建一个设定上限的 线程池执行服务。
	 * @param nTheads 最大线程数
	 * @return ExecutorService
	 */
	public static ExecutorService newFixedThreadPool(int nTheads) {
		return new ThreadPoolExecutor(nTheads, nTheads,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024));
	}

	/**
	 * 新建一个单线程池执行服务。
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor() {
		return newFixedThreadPool(1);
	}
}

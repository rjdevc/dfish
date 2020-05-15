package com.rongji.dfish.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程工具类
 * @author LinLW
 *
 */
public class ThreadUtil {
    private static final int MAX_THREAD_POOL_SIZE=200;
    private static final int MAX_QUEUE_SIZE=1024;
    private static final long KEEP_ALIVE=60;

	/**
	 * 使用另一个线程运行这个runnable的内容。
	 * 这样当前线程不需要等待它运行结束。
	 * @param runnable 可运行的内容
	 */
	public static void execute(Runnable runnable){
		getCachedThreadPool().execute(runnable);
	}

    /**
     * 在timeout时间内执行runnable
     * 如果执行成功并且没超时，则结束。继续执行下一个语句。
     * 如果超时，则在超时后，抛出TimeoutException
     *
     * 超时的时候，如果forceStop==true，则会强制杀死正在执行的runnable
     * 可能会有风险，详情参看 {@link Thread#stop()}
     * 超时的时候，如果forceStop==false,则尝试杀死正在执行的Runnable.这种情况下
     * Runnable的run方法中需要要自己埋结束点 如在每个可打断的点加：
     * if(Thread.currentThread().isInterrupted()){return;}
     * @param timeout 超时时间
     * @param forceStop 是否强制结束
     * @param r Runnable
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     * @throws TimeoutException if the wait timed out
     */
    public static void invoke(long timeout,boolean forceStop,Runnable r) throws InterruptedException, ExecutionException, TimeoutException{
        DelegateCallable dc=new DelegateCallable(r);
        Future<Object> f= getCachedThreadPool().submit(dc);
        try {
            f.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            if(forceStop){
                if(dc.t!=null) {
                    dc.t.stop();
                }
            }else {
                f.cancel(true);
            }
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private static class DelegateCallable implements Callable<Object>{
        Runnable r;
        Thread t;
        public DelegateCallable(Runnable r){
            this.r=r;
        }
        @Override
        public Object call(){
            t=Thread.currentThread();
            r.run();
            return null;
        }
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
		return new ThreadPoolExecutor(0, MAX_THREAD_POOL_SIZE,
                KEEP_ALIVE, TimeUnit.SECONDS,
                new SynchronousQueue<>());
	}

	/**
	 * 新建一个设定上限的 线程池执行服务。
	 * @param nTheads 最大线程数
	 * @return ExecutorService
	 */
	public static ExecutorService newFixedThreadPool(int nTheads) {
		return new ThreadPoolExecutor(0, nTheads,
                KEEP_ALIVE, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_QUEUE_SIZE));
	}

	/**
	 * 新建一个单线程池执行服务。
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor() {
		return newFixedThreadPool(1);
	}

	/**
	 * 执行所有的run 这些动作将同时被执行，全部执行完毕，或碰到异常终止后，
	 * 主线程将继续往下执行。
	 * @param runnables
	 */
	public static void execute(Collection<Runnable> runnables) throws ExecutionException, InterruptedException {
		List<Future<?>> futures=new ArrayList<>();
		for(Runnable run:runnables){
			Future<?> f= SHARED_THREAD_POOL.submit(run);
			futures.add(f);
		}
		for(Future<?> f:futures){
			f.get();
		}
	}

//	public static void main(String[] args) throws ExecutionException, InterruptedException {
//		List<Runnable> rs=new ArrayList<>();
//		rs.add(()->{
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.out.println("2000");
//		});
//		rs.add(()->{
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			System.out.println("3000");
//		});
//		execute(rs);
//		System.out.println("end");
//	}
}

package com.rongji.dfish.base.batch
;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.ThreadUtil;

/**
 * <p>可以限定访问的最大线程数，通过合并请求来提高运算的效率。</p>
 * <p>一般来说，如果需要控制某些资源不要被过于频繁的使用，会想办法让请求排队，并分组进行批量请求。
 * 可以用以下代码，进行封装</p>
 * <pre>
 * QueuedBatchAction&lt;String,Object&gt; m=new QueuedBatchAction&lt;String,Object&gt;(
 * new BatchAction&lt;String,Object&gt;(){
 * 	public Map&lt;String,Object&gt; act(Set&lt;String&gt; input) {
 * 		return null; // 实际批量获取的代码。
 * 	}
 * });
 * Object o=m.act("something");
 * Map&lt;String,Object&gt; map.=m.get( new HashSet&lt;String&gt;(Arrays.asList("str2","str3")));
 * </pre>
 * 如果调用这个方法超过3个以上的线程，或某些查询批量超过15的时候，
 * 在不同线程中的单个查询或小批量查询，有可能产生排队，并重新编程每组不超过15个的批量查询。
 * 转而调用act方法。
 * 实际使用的时候，有很大可能加大这些参数。
 * 通常这个类是单例的。
 * 
 * @author DFish Team
 * @see #QueuedBatchAction(BatchAction,int,int)
 * @param <I>
 * @param <O>
 */
public class QueuedBatchAction<I, O> implements BatchAction<I,O>{
	private BatchAction<I, O> action;//注册进来的实际的批量获取实现
	private BlockingQueue<I> waitingQueue;//等待被排队

	List<OutputHook<I, O>> outputHooks = new ArrayList<>();

	private ExecutorService exec;

	/**
	 * 构造函数
	 * @param maxThread 一般批量排队的，3-5比较合适，如果目标主机性能很好可以开大一些。
	 * @param batchSize 每次批量的大小，根据业务，20-50可能会比较合适。结果越简单，建议批量越大
	 * @param action 实际计算结果的方法。
	 */
	public QueuedBatchAction(BatchAction<I, O> action, int maxThread, int batchSize) {
		exec=ThreadUtil.newFixedThreadPool(maxThread);
		waitingQueue= new LinkedBlockingQueue<>(batchSize);
		this.action =action;
	}

	/**
	 * 构造函数 相当于 new QueuedBatchAction(act,3,15);
	 * @param act
	 */
	public QueuedBatchAction( BatchAction<I, O> act) {
		this(act,3,15);
	}

	@Override
	public Map<I, O> act(Set<I> input) {
		if (Utils.isEmpty(input)) {
			return Collections.emptyMap();
		}
		//已经存在的先获取，防止fetch期间部分元素还过期。
		Map<I, O> output= new HashMap<>(input.size());
		if(input.size()>0){
			OutputHook<I, O> oh= registerHooks(input);
			for(I item : input){
				try {
					waitingQueue.put(item);
					if(waitingQueue.remainingCapacity()<=0){
						doAct();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			doAct();
			output.putAll(oh.get()) ;
		}
		return output;
	}

	private OutputHook<I, O> registerHooks(Set<I> input) {
		OutputHook<I, O> hook= new OutputHook<>(input);
		synchronized (outputHooks) {
			if(input!=null&&input.size()>0){
				outputHooks.add(hook);}
		}
		return hook;
	}

	/**
	 * 实际执行的动作。
	 * 用一个独立线程，把阻断队列里面的内容提取出来。
	 * 分组请求。取得(或者失败)以后，通知取得的结果。
	 */
	private void doAct() {
		 this.exec.execute(() ->{
				//把所有队列内容全部放置到真正的查询线程中去当做参数。
				Set<I> input= new HashSet<>();
				waitingQueue.drainTo(input);
				if(input.size()==0){
					return;
				}

				try {
					Map<I, O> vs= action.act(input);
					for(I k:input){ // 获取的结果会有缺失,所以以获取的结果来判断
						O v = vs.get(k);
						notifyHook(k, v);
					}
				} catch (Throwable e) {
					//失败了也要通知结果。
					for(I key:input){
						notifyHook(key, null);
					}
					e.printStackTrace();
				}//可能消耗较多时间。
		 });
	}
	private void notifyHook(I k, O v) {
		synchronized (outputHooks) {
			for(Iterator<OutputHook<I, O>> iter = outputHooks.iterator(); iter.hasNext();){
				OutputHook<I, O> oh=iter.next();
				synchronized (oh.waiting) {
					if(oh.waiting.remove(k)){
						oh.output.put(k, v);
					}
					if(oh.waiting.isEmpty()){
						iter.remove();
					}
				}
				synchronized (oh) {
					oh.notify();
				}
			}
		}
	}
	

	private static class OutputHook<K,V>{
		Set<K> waiting = new HashSet<>();
		Map<K,V> output = new HashMap<>();
		OutputHook(Set<K> input){
			if(input!=null&&input.size()>0){
				waiting.addAll(input);
			}
		}
		public Map<K,V> get(){
			if(waiting !=null){
				int i=0;
				synchronized (waiting) {
					i= waiting.size();
				}
				while (i>0){
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					synchronized (waiting) {
						i= waiting.size();
					}
				}
			}
			return output;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		exec.shutdown();
		super.finalize();
	}
}

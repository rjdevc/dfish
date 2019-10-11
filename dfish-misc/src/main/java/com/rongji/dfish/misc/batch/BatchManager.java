package com.rongji.dfish.misc.batch
;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.base.util.ThreadUtil;

/**
 * <p>可以限定访问的最大线程数，通过缓存和合并请求来提高查询的效率。</p>
 * <p>一般来说，如果需要控制某些资源不要被过于频繁的使用，会想办法让请求排队，并分组进行批量请求。
 * 可以用以下代码，进行封装</p>
 * <pre>
 * BatchManager<String,Object> m=new BatchManager<String,Object>(3,15,30000L);
 * m.setFetcher(new BatchFetcher<String,Object>(){
 * 	public Map<String,Object> fetch(List<String> keys) {
 * 		return null; // 实际批量获取的代码。
 * 	}
 * });
 * m.get("something");
 * m.get(Arrays.asList("str2","str3"));
 * </pre>
 * 如果调用这个方法超过3个以上的线程，或某些查询批量超过15的时候，
 * 在不同线程中的单个查询或小批量查询，有可能产生排队，并重新编程每组不超过15个的批量查询。
 * 转而调用fetch方法。这里，查询结果将有30秒的缓存。3秒内重复参数的请求一般不会再次fetch。
 * 这里说的一般是指很大几率。但也有小几率重复的请求正好被同时放到不同的fetch线程中去。
 * 实际使用的时候，有很大可能加大这些参数。
 * 
 * @author DFish Team
 * @see #BatchManager(int, int, long)
 * @param <K>
 * @param <V>
 */
public class BatchManager<K,V> {
	private BatchFetcher<K,V> bf;//注册进来的实际的批量获取实现
	private BlockingQueue<K> waitingQueue;//等待被排队
//	private Object lock=new Object(); //用这个锁，让同一批的请求先先进队列
	
	List<ResultGetter<K, V>> resultGetters= new ArrayList<ResultGetter<K, V>> ();
//	private Lock lock2=new ReentrantLock();
//	AtomicInteger runningThreadCount=new AtomicInteger(0);//用于计算活动的线程
	//用于记录哪些元素正在请求中。因为多线程关系，可能部分数据已经在该请求中消失，但是仍旧可以在cache中获取。
//	private Map<K,Future<Map<K, V>>> futureMap=Collections.synchronizedMap(new HashMap<K,Future<Map<K, V>>>());
//	private Future<Map<K, V>> waitingFuture;

	private ExecutorService exec;
	MemoryCache<K,V> cache;//缓存

	int maxThread;
	int batchSize;
	
	/**
	 * 构造函数
	 * @param maxThread 一般批量排队的，3-5比较合适，如果目标主机性能很好可以开大一些。
	 * @param batchSize 每次批量的大小，根据业务，20-50可能会比较合适。结果越简单，建议批量越大
	 * @param alive 数据将缓存多长时间，比如300000Lms=5分钟
	 */
	public BatchManager(int maxThread,int batchSize,long alive) {
		exec=ThreadUtil.newFixedThreadPool(this.maxThread=maxThread);
		waitingQueue=new LinkedBlockingQueue<K>(this.batchSize=batchSize);
		cache=new MemoryCache<K,V>(8192,alive);
	}

	/**
	 * 获取值的信息
	 * @param key
	 * @return
	 */
	public V get(K key){
		if (key == null) {
			return null;
		}
		List<V>vs=get(Collections.singletonList(key));
		if (Utils.notEmpty(vs)) {
			return vs.get(0);
		}
		return null;
	}
	/**
	 * 批量获取值的信息。这里的批量只是业务本省的批量
	 * manager会根据自身需要将该批量拆分或合并。
	 * @param keys
	 * @return
	 */
	public List<V> get(List<K> keys){
		if (Utils.isEmpty(keys)) {
			return Collections.emptyList();
		}
		Map<K, V> fetchResult = getAsMap(keys);
		List<V>result=new ArrayList<V>(keys.size());
		for(K k : keys){
			V v=fetchResult.get(k);
			result.add(v);
		}
			
		return result;
	}

	public Map<K, V> getAsMap(List<K> keys) {
		if (Utils.isEmpty(keys)) {
			return Collections.emptyMap();
		}
		//已经存在的先获取，防止fetch期间部分元素还过期。
//		List<V>result=new ArrayList<V>(keys.size());
		Map<K, V> result=new HashMap<K,V>(keys.size());
		List<K>fetchKeys=new ArrayList<K>();
		for(K key:keys){
			V v=cache.get(key);
			if(v!=null){
				result.put(key,v);
			}else{
//				result.put(key,null);
				fetchKeys.add(key);
			}
		}
		if(fetchKeys.size()>0){
			ResultGetter<K, V> rg=registerResultGetter(fetchKeys);
			for(K k : fetchKeys){
				try {
					waitingQueue.put(k);
					if(waitingQueue.remainingCapacity()<=0){
						tryStartBatch();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}					
			}
			tryStartBatch();
			result.putAll(rg.get()) ;
		}	
		return result;
	}

	private ResultGetter<K, V> registerResultGetter(List<K> keys) {
		ResultGetter<K, V> r=new ResultGetter<K,V>(keys);
		synchronized (resultGetters) {
			if(keys!=null&&keys.size()>0){resultGetters.add(r);}
		}
		return r;
	}

	private void tryStartBatch() {
		 this.exec.execute(new Runnable(){
			@Override
			public void run() {
				//把所有队列内容全部放置到真正的查询线程中去当做参数。
				List<K> keys=new ArrayList<K>();
				waitingQueue.drainTo(keys, batchSize);
				if(keys.size()==0){
					return;
				}

				try {
					Map<K, V> vs= bf.fetch(keys);
					for(K k:keys){ // 获取的结果会有缺失,所以以获取的结果来判断
						V v = vs.get(k);
						if (v != null) {
							cache.put(k, v);
						}
						setResultToResultGetter(k, v);
					}
				} catch (Throwable e) {
					//失败了也要通知结果。
					for(int i=0;i<keys.size();i++){
						setResultToResultGetter(keys.get(i), null);
					}
					e.printStackTrace();
				}//可能消耗较多时间。
			}

		 });
	}
	private void setResultToResultGetter(K k, V v) {
		synchronized (resultGetters) {
			for(Iterator< ResultGetter<K, V>>iter= resultGetters.iterator();iter.hasNext();){
				ResultGetter<K, V>rg=iter.next();
				synchronized (rg.unfetched) {
					if(rg.unfetched.remove(k)){
						rg.result.put(k, v);
					}
					if(rg.unfetched.size()==0){
						iter.remove();
					}
				}
				synchronized (rg) {
					rg.notify();
				}
			}
		}
	}
	
	/**
	 * 需要注册这个接口来实现批量的获取数据。
	 * manager将请求合并后通过该获取器获取。
	 * @param batchFetcher
	 */
	public void setFetcher(BatchFetcher<K, V> batchFetcher) {
		this.bf=batchFetcher;
	}
	
//	public static void main(String[] args) {
//		
//		final long begin=System.currentTimeMillis();
//		final BatchManager<String,String> m=new BatchManager<String,String>(3,15,300000L);
//		final Random r=new Random();
//		m.setFetcher(new BatchFetcher<String,String>(){
//			@Override
//			public List<String> fetch(List<String> keys) {
//				List<String> result=new ArrayList<String>();
//				log("尝试合并fetch"+keys,begin);
//				for(String key:keys){
//					result.add("名称"+key);
//				}
//				try {
//					Thread.sleep(r.nextInt(1200+20*keys.size()));
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				return result;
//			}
//		});
//		
//		for(int i=0;i<20;i++){
//			final int i_=i;
//			new Thread(){
//				public void run(){
//					for(int loop=0;loop<10;loop++){
//						int size=1+r.nextInt(5);
//						List<String> keys=new ArrayList<String>(size);
//						for(int k=0;k<size;k++){
//							int seed =r.nextInt(65536);
//							keys.add(String.valueOf( seed));
//						}
//						log("thread"+i_+" loop"+loop+" 参数 "+keys+" 返回结果"+m.get(keys),begin);
//					}
//				}
//			}.start();
//		}
//	}
//	public static void log(Object v,long begin){
//		System.out.println("["+(System.currentTimeMillis()-begin)+"ms]"+v);
//	}

	private static class ResultGetter<K,V>{
		Set<K> unfetched=new HashSet<K>();
		Map<K,V> result=new HashMap<K,V>();
		ResultGetter(List<K> keys){
			if(keys==null||keys.size()==0){
				
			}else{
				unfetched.addAll(keys);
			}
		}
		public Map<K,V> get(){
			if(unfetched!=null){
				int i=0;
				synchronized (unfetched) {
					i=unfetched.size();
				}
				while (i>0){
					try {
						synchronized (this) {
							this.wait();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					synchronized (unfetched) {
						i=unfetched.size();
					}
//					this.notify();
				}
			}
			return result;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		exec.shutdown();
		super.finalize();
	}
}

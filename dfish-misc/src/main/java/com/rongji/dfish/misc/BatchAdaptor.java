package com.rongji.dfish.misc;

import com.rongji.dfish.base.batch.QueuedBatchAction;
import com.rongji.dfish.base.exception.MarkedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * <p>单个处理，转批量处理的适配器</p>
 * 
 * 功能描述：该适配器可以所有需要执行的内容收集起来，并且转为单线程的批量执行.
 * 
 * 
 * <p>向服务器发送数据的类</p>
 * 功能描述：将要发送的数据添加到阻塞队列。<br>
 * 业务介绍：如果当前为工作状态，将要发送的数据添加到队列中等待<br>
 * 如果为空闲状态，将数据添加到队列，然后发给服务器<br>
 * 如果队列已经等待最大量，抛出异常<br>
 * @author 研发中心 - IMeng
 * @deprecated 使用BatchManager
 * @see QueuedBatchAction
 *
 */
@Deprecated
public abstract class BatchAdaptor {
	
//	//单次批量处理最大数
	private int batchSize;
	//最大等待数
	private int maxPooled;
	//工作状态 - 默认为空闲
	private boolean working = false;
	//工作队列
	private final Queue<Object> workingQueue;
		
	
	/**
	 * <p>构造函数</p>
	 * @param batchSize
	 * @param maxPooled
	 */
	public BatchAdaptor(int batchSize, int maxPooled) {
		this.batchSize = batchSize;
		this.maxPooled = maxPooled;
		workingQueue = new LinkedList<>();
	}
	public BatchAdaptor() {
		this(64,1024);
	}

	/**
	 * <p>向队列中添加数据</p>
	 * 使用阻塞队列的add方法，当队列达到最大值时，抛出异常
	 * 
	 * @param object 数据值
	 * @throws Exception - 阻塞队列达到最大值还加入数据，抛出异常
	 */
	public void execute(Object object) throws Exception{

		//使用阻塞数组中的add方法 - 满了会自己抛出异常
		synchronized(workingQueue){
			if(workingQueue.size()>=maxPooled){
				throw new MarkedException("THE BATCH ADAPTOR IS BUSY");
			}
			workingQueue.add(object);
		}
		
		//空闲状态 - 执行发送
		if(!working){
			working = true;
			new Thread(){
				@Override
                public void run() {
					while(!workingQueue.isEmpty()) {
						try {
							executeBatch(findSendData());
						} catch (Exception e) {}
					}
					working = false;
				}
			}.start();
		}
	};
	
	/**
	 * 获取当前要发送给服务器的最多数据
	 * @return List
	 */
	private List<Object> findSendData(){
		//获取要发送的数据条数
		int count = batchSize;
		List<Object> list = new ArrayList<>(count);
		synchronized(workingQueue){
			count=workingQueue.size() > batchSize ? 
					batchSize : workingQueue.size();
			//获取要发送的索引值
			for (int i = 0; i < count; i++) {	
				//将索引添加到数组
				try {
					list.add(workingQueue.poll());
				} catch (Exception e) {
					e.printStackTrace();	
				}
			}
		}
		return list;
	}
	
//	/**
//	 * 发送消息的方法
//	 * @param list 存放数据内容数组
//	 */
	/*
	private void sendData(){
		//将状态标记为工作状态
		if(executeBatch(findSendData())){
			//如果队列中还存在数据，直接再次发送给服务器
			
			synchronized(workingQueue){
				if(!workingQueue.isEmpty()){		
					sendData();
				}
			}
		}
		working = false;
	}*/
	
	/**
	 * 真正实现发送数据到服务器的后台方法
	 * @param list 数据内容
	 */
	public abstract void executeBatch(List<Object> list);
	
	public int getWorkingQueueSize() {
		return workingQueue.size();
	}
	public int getBatchSize() {
		return batchSize;
	}
	public int getMaxPooled() {
		return maxPooled;
	}
	
	
}

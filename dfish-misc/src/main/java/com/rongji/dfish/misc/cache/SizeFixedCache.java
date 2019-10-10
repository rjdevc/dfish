package com.rongji.dfish.misc.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Title: 榕基I-TASK执行先锋
 * </p>
 * 
 * <p>
 * Description: 专门为提高企业执行力而设计的产品
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 榕基软件开发有限公司
 * </p>
 * 
 * SizeFixedCache 为大小受限制的一个MAP 用做缓存.如果MAP中大小太大,则会删除最久的对象. 保证内存不会溢出
 * 
 * @author I-TASK成员: LinLW
 * @version 2.0 使用了LinkedHashMap的特性.
 */
public class SizeFixedCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 8959155076803596737L;

	private int maxSize;

	public SizeFixedCache(int maxSize) {
		super(maxSize, .75f, true);
		this.maxSize = maxSize;
	}

	/**
	 * 限制大小不超过maxSize
	 * 
	 * @see LinkedHashMap#removeEldestEntry
	 * @param entry
	 *            Entry
	 * @return boolean
	 */
	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
		return size() > maxSize;
	}
}

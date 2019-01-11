//package com.rongji.dfish.framework.singletonimpl;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import com.rongji.dfish.framework.FrameworkCache;
//
//public class DefaultCache<K, V> extends LinkedHashMap<K, V>  implements FrameworkCache<K, V>{
//	private static final long serialVersionUID = 8959155076803596737L;
//
//	private int maxSize;
//	public DefaultCache() {
//		this(1024);
//	}
//	public DefaultCache(int maxSize) {
//		super(maxSize, .75f, true);
//		this.maxSize = maxSize;
//	}
//
//	/**
//	 * 限制大小不超过maxSize
//	 * 
//	 * @see LinkedHashMap#removeEldestEntry
//	 * @param entry
//	 *            Entry
//	 * @return boolean
//	 */
//	@Override
//	protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
//		return size() > maxSize;
//	}
//}

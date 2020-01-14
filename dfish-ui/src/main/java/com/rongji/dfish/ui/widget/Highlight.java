package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractNode;

/**
 * 高亮配置
 * @author DFish Team
 * @since dfish 3.0
 */
public class Highlight extends AbstractNode<Highlight> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1609037958308114536L;
	/**
	 * 高亮关键字
	 */
	private String key;
	/**
	 * 匹配高亮切词长度,&lt;=0或者为空全字匹配,其他说明按照长度切词匹配
	 */
	private Integer matchLength;
	/**
	 * 高亮样式
	 */
	private String keyCls;
	
	@Override
    public String getType() {
	    return null;
    }
	/**
	 * 构造函数
	 */
	public Highlight() {
		super();
	}

	/**
	 * 构造函数
	 * @param key String 关键字
	 */
	public Highlight(String key) {
		super();
		this.key = key;
	}
	
	/**
	 * 构造函数
	 * @param matchLength Integer 至少要几个字相同才会触发highlight
	 */
	public Highlight(Integer matchLength) {
		super();
		this.matchLength = matchLength;
	}
	
	/**
	 * 构造函数
	 * @param key String 高亮关键字
	 * @param matchLength Integer 至少要几个字相同才会触发highlight
	 */
	public Highlight(String key, Integer matchLength) {
		super();
		this.key = key;
		this.matchLength = matchLength;
	}

	/**
	 * 高亮关键字
	 * @return String
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 设置高亮关键字
	 * @param key 高亮关键字
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Highlight setKey(String key) {
		this.key = key;
		return this;
	}

	/**
	 * 匹配高亮切词长度,&lt;=0或者为空全字匹配,其他说明按照长度切词匹配
	 * @return Integer
	 */
	public Integer getMatchLength() {
		return matchLength;
	}

	/**
	 * 匹配高亮切词长度,&lt;=0或者为空全字匹配,其他说明按照长度切词匹配
	 * @param matchLength 高亮切词长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Highlight setMatchLength(Integer matchLength) {
		this.matchLength = matchLength;
		return this;
	}

	/**
	 * 关键字样式
	 * @return String
	 */
	public String getKeyCls() {
		return keyCls;
	}

	/**
	 * 关键字样式
	 * @param keyCls 关键字样式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Highlight setKeyCls(String keyCls) {
		this.keyCls = keyCls;
		return this;
	}

}

package com.rongji.dfish.ui;

/**
 * 高亮配置
 * @author DFish Team
 * @since dfish 3.0
 */
public class Highlight extends AbstractJsonObject {

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
	private Integer matchlength;
	/**
	 * 高亮样式
	 */
	private String keycls;
	
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
	 * @param key
	 */
	public Highlight(String key) {
		super();
		this.key = key;
	}
	
	/**
	 * 构造函数
	 * @param matchlength
	 */
	public Highlight(Integer matchlength) {
		super();
		this.matchlength = matchlength;
	}
	
	/**
	 * 构造函数
	 * @param key
	 * @param matchlength
	 */
	public Highlight(String key, Integer matchlength) {
		super();
		this.key = key;
		this.matchlength = matchlength;
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
	public Integer getMatchlength() {
		return matchlength;
	}

	/**
	 * 匹配高亮切词长度,&lt;=0或者为空全字匹配,其他说明按照长度切词匹配
	 * @param matchlength 高亮切词长度
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Highlight setMatchlength(Integer matchlength) {
		this.matchlength = matchlength;
		return this;
	}

	/**
	 * 关键字样式
	 * @return String
	 */
	public String getKeycls() {
		return keycls;
	}

	/**
	 * 关键字样式
	 * @param keycls 关键字样式
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Highlight setKeycls(String keycls) {
		this.keycls = keycls;
		return this;
	}

}

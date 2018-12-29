package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.form.Xbox;

/**
 * 引入一个独立页面
 * @author DFish Team
 *
 */
public class EmbedWindow extends AbstractWidget<EmbedWindow> implements HasText<EmbedWindow>{

/**
	 * 
	 */
	private static final long serialVersionUID = -2669895425202791322L;
	//	private String align;
//	private String valign;
	private String text;
	private String src;
//	private Boolean scroll;
//	private String scrollClass;
//	private Boolean escape;
	private Boolean scroll;
	
	/**
	 * 构造函数
	 * @param id 编号
	 * @param src 地址
	 */
	public EmbedWindow(String id,String src){
		super();
		this.id=id;
		this.src=src;
	}
	/**
	 * 构造函数
	 * @param src 地址
	 */
	public EmbedWindow(String src){
		this(null, src);
	}


	public String getType() {
		return "ewin";//ewin
	}


	
	
	/**
	 * 显示文本
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text 文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public EmbedWindow setText(String text) {
		this.text = text;
		return this;
	}
	
	/**
	 * 页面地址。
	 * @return src
	 */
	public String getSrc() {
		return src;
	}

	/**
	 * 页面地址。
	 * @param src 地址
	 * @return 本身，这样可以继续设置其他属性
	 */
	public EmbedWindow setSrc(String src) {
		this.src = src;
		return this;
	}
	
	
	/**
	 * 是否显示滚动条。
	 * @return Boolean
	 */
	public Boolean getScroll() {
		return scroll;
	}

	/**
	 * 是否显示滚动条。
	 * @param scroll Boolean
	 * @return this
	 */
	public EmbedWindow setScroll(Boolean scroll) {
		this.scroll=scroll;
		return this;
	}
}
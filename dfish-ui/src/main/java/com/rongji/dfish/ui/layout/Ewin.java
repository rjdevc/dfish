package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Scrollable;

/**
 * 引入一个独立页面
 * @author DFish Team
 *
 */
public class Ewin extends AbstractWidget<Ewin> implements Scrollable<Ewin>, HasText<Ewin>{

/**
	 * 
	 */
	private static final long serialVersionUID = -2669895425202791322L;
	//	private String align;
//	private String valign;
	private String text;
	private Boolean escape;
	private String src;
	private Boolean scroll;
	private String scrollClass;
//	private Boolean escape;

	/**
	 * 构造函数
	 * @param id 编号
	 * @param src 地址
	 */
	public Ewin(String id, String src){
		super();
		this.id=id;
		this.src=src;
	}
	/**
	 * 构造函数
	 * @param src 地址
	 */
	public Ewin(String src){
		this(null, src);
	}


	@Override
    public String getType() {
		return "ewin";//ewin
	}


	
	
	/**
	 * 显示文本
	 * @return text
	 */
	@Override
    public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text 文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	@Override
    public Ewin setText(String text) {
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
	public Ewin setSrc(String src) {
		this.src = src;
		return this;
	}
	@Override
    public Boolean getScroll() {
		return scroll;
	}
	@Override
    public Ewin setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}
	@Override
    public String getScrollClass() {
		return scrollClass;
	}
	@Override
    public Ewin setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return this;
	}
	@Override
    public Ewin setEscape(Boolean escape){
		this.escape=escape;
		return this;
	}
	@Override
    public Boolean getEscape(){
		return escape;
	}
}
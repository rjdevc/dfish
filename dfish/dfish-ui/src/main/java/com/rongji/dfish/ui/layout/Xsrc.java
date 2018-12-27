package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractView;
import com.rongji.dfish.ui.LazyLoad;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
public class Xsrc extends AbstractView<Xsrc> implements LazyLoad<Xsrc>{

	private static final long serialVersionUID = 4639236345195733523L;

	/**
	 * 构造函数
	 * @param id String
	 */
	public Xsrc(String id) {
		super(id);
	}
	
	/**
	 * 默认构造函数
	 */
	public Xsrc() {
		super(null);
	}
	
	private String template;
	private String src;

	@Override
	public String getType() {
		return "xsrc";
	}


	public String getTemplate() {
		return template;
	}

	public Xsrc setTemplate(String template) {
		this.template = template;
		return this;
	}
	/**
	 * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
	 * @return src
	 */
	public String getSrc() {
		return src;
	}
	/**
	 * 加载 view 的 url。访问这个url 时应当返回一个 view 的 json 字串。
	 * @param src URL
	 * @return 本身，这样可以继续设置其他属性
	 */
	public Xsrc setSrc(String src) {
		this.src = src;
		return this;
	}
	
}

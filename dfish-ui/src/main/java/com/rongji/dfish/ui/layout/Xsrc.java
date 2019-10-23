package com.rongji.dfish.ui.layout;

import com.rongji.dfish.ui.AbstractSrc;
import com.rongji.dfish.ui.LazyLoad;


/**
 * 视图对象。
 * @author DFish Team
 *
 */
public class Xsrc extends AbstractSrc<Xsrc> implements LazyLoad<Xsrc>{

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
	
	private String preload;
	private String template;
	private String src;

	@Override
	public String getType() {
		return "xsrc";
	}


	@Override
    public String getPreload() {
		return preload;
	}

	@Override
    public Xsrc setPreload(String preload) {
		this.preload = preload;
		return this;
	}
	@Override
    public String getTemplate() {
		return template;
	}

	@Override
    public Xsrc setTemplate(String template) {
		this.template = template;
		return this;
	}

	@Override
    public String getSrc() {
		return src;
	}

	@Override
    public Xsrc setSrc(String src) {
		this.src = src;
		return this;
	}
	
}

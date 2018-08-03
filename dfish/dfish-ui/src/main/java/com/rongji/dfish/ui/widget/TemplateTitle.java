package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.Scrollable;

/**
 * 
 * @author DFish Team
 *
 */
public class TemplateTitle extends AbstractWidget<TemplateTitle> implements Scrollable<TemplateTitle>,HtmlContentHolder<TemplateTitle>,HasText<TemplateTitle>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8155744934245686731L;
	private Boolean escape;
	private String text;
	private Boolean scroll;
	private String scrollClass;
	
	/**
	 * @param id
	 * @param text
	 */
	public TemplateTitle(String id,String text){
		super();
		this.id=id;
		this.text=text;
	}
	/**
	 * @param text
	 */
	public TemplateTitle(String text){
		this(null, text);
	}


	public String getType() {
		return "template/title";
	}

	public Boolean getEscape() {
		return this.escape;
	}
	
	public TemplateTitle setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	public Boolean getScroll() {
		return scroll;
	}

	public TemplateTitle setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}

	public String getScrollClass() {
		return scrollClass;
	}
	
	public TemplateTitle setScrollClass(String scrollClass) {
		this.scrollClass = scrollClass;
		return this;
	}
	
	/**
	 * 显示文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public String getText() {
		return text;
	}

	/**
	 * 显示文本
	 * @param text
	 * @return 本身，这样可以继续设置其他属性
	 */
	public TemplateTitle setText(String text) {
		this.text = text;
		return this;
	}
	
}

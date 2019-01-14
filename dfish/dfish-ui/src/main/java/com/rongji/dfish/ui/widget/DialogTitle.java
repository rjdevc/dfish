package com.rongji.dfish.ui.widget;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;
import com.rongji.dfish.ui.Scrollable;

/**
 * TemplateTitle 用于弹出窗口模板中顶部标题栏目的部分
 * 
 * @author DFish Team
 *
 */
public class DialogTitle extends AbstractWidget<DialogTitle> implements Scrollable<DialogTitle>,HtmlContentHolder<DialogTitle>,HasText<DialogTitle>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8155744934245686731L;
	private Boolean escape;
	private String text;
	private Boolean scroll;
	private String scrollClass;
	
	/**
	 * 构造函数
	 * @param id String 编号
	 * @param text String 显示文本
	 */
	public DialogTitle(String id,String text){
		super();
		this.id=id;
		this.text=text;
	}
	/**
	 * @param text 标题的文本
	 */
	public DialogTitle(String text){
		this(null, text);
	}


	public String getType() {
		return "dialog/title";
	}

	public Boolean getEscape() {
		return this.escape;
	}
	
	public DialogTitle setEscape(Boolean escape) {
		this.escape = escape;
		return this;
	}

	public Boolean getScroll() {
		return scroll;
	}

	public DialogTitle setScroll(Boolean scroll) {
		this.scroll = scroll;
		return this;
	}

	public String getScrollClass() {
		return scrollClass;
	}
	
	public DialogTitle setScrollClass(String scrollClass) {
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
	 * @param text 标题的文本
	 * @return 本身，这样可以继续设置其他属性
	 */
	public DialogTitle setText(String text) {
		this.text = text;
		return this;
	}
	
}

package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;

@SuppressWarnings("unchecked")
public abstract class AbstractFormLabel<T extends AbstractFormLabel<T>> extends AbstractWidget<T> implements HtmlContentHolder<T>,Alignable<T>,HasText<T>{

	private static final long serialVersionUID = -8829564341034469323L;
	protected String align;
	protected String text;
	protected Boolean escape;
	protected String format;
	
	public String getType() {
		return null;
	}
	
	public String getText() {
		return text;
	}

	public T setText(String text) {
		this.text = text;
		return (T)this;
	}

	public String getAlign() {
		return align;
	}

	public T setAlign(String align) {
		this.align = align;
		return (T)this;
	}
	protected void copyProperties(AbstractFormLabel<?> to,AbstractFormLabel<?> from){
		super.copyProperties(to, from);
		to.align=from.align;
		to.escape=from.escape;
		to.text=from.text;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @return Boolean
	 */
	public Boolean getEscape() {
		return this.escape;
	}

	/**
	 * 用于显示文本是否需要转义,不设置默认是true
	 * @param escape Boolean
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T)this;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @return String 格式化内容
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 格式化内容。"$字段名"形式的变量将被解析替换。支持"javascript:"开头的js语句(需return返回值)。
	 * @param format String 格式化内容
	 * @return 本身，这样可以继续设置其他属性
	 */
	public T setFormat(String format) {
		this.format = format;
		return (T) this;
	}
	
}

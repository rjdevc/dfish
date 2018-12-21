package com.rongji.dfish.ui.form;

import com.rongji.dfish.ui.AbstractWidget;
import com.rongji.dfish.ui.Alignable;
import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.HtmlContentHolder;

public class AbstractFormLabel<T extends AbstractFormLabel<T>> extends AbstractWidget<T> implements HtmlContentHolder<T>,Alignable<T>,HasText<T>{

	private static final long serialVersionUID = -8829564341034469323L;
	protected String align;
	protected String text;
	protected Boolean escape;
	
	public String getType() {
		return null;
	}
	public Boolean getEscape() {
		return this.escape;
	}
	
	@SuppressWarnings("unchecked")
	public T setEscape(Boolean escape) {
		this.escape = escape;
		return (T)this;
	}
	public String getText() {
		return text;
	}

	@SuppressWarnings("unchecked")
	public T setText(String text) {
		this.text = text;
		return (T)this;
	}
	public String getAlign() {
		return align;
	}
	@SuppressWarnings("unchecked")
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
	
}

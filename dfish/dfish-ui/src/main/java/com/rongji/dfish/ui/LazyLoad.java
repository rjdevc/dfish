package com.rongji.dfish.ui;

public interface LazyLoad<T extends LazyLoad<T>> {
	String getSrc();
	T setSrc(String src);
	String getTemplate();
	T setTemplate(String template);
}

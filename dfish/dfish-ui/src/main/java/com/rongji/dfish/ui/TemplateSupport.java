package com.rongji.dfish.ui;

import java.util.Map;

public interface TemplateSupport<T extends TemplateSupport<T>> {
	T at(String prop,String expr);
	
	Map<String,String> ats();
	
	void ats(Map<String,String> ats);
}

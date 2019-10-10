package com.rongji.dfish.framework.config;

public interface SystemConfigHolder {

	String getProperty(String key);

	void setProperty(String key, String value);

	void reset();

}

package com.rongji.dfish.framework;

public interface PersonalConfigHolder {

	String getProperty(String userId, String key);

	void setProperty(String userId, String key, String value);

	void reset();
}

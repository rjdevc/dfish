package com.rongji.dfish.framework.config.impl;

import java.io.File;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.framework.FrameworkContext;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.config.SystemConfigHolder;

public class DefaultSystemConfig implements SystemConfigHolder{
	private String configFile;
	private JsonConfigHelper xmltool;
	
	private Cache<String, String> propertyCache = new MemoryCache<String, String>();
	@Override
	public String getProperty(String name) {
		
		String propValue = propertyCache.get(name);
		if (Utils.isEmpty(propValue)) {
			propValue = xmltool.getProperty(name);
		}
		
		return propValue;
	}

	@Override
	public void reset() {
		@SuppressWarnings("deprecation")
        String configPath=FrameworkContext.getInstance().getServletInfo().getServletRealPath()+"WEB-INF/config/"+configFile;
		FrameworkHelper.LOG.info("load file : "+configPath);
			xmltool=new JsonConfigHelper(new File(configPath));
			propertyCache.clear();
	}

	@Override
	public void setProperty(String key, String value) {
		xmltool.setProperty(key, value);
		// 设置缓存
		propertyCache.put(key, value);
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
		reset();
	}
	

}

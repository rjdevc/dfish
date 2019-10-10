package com.rongji.dfish.framework.singletonimpl;

import java.io.File;

import org.dom4j.DocumentException;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.framework.FrameworkHelper;
import com.rongji.dfish.framework.SystemConfigHolder;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.misc.util.XMLUtil;

public class DefaultSystemConfig implements SystemConfigHolder{
	private String configFile;
	private JsonConfigHelper xmltool;
	
	private Cache<String, String> propertyCache = new MemoryCache<String, String>();
	public String getProperty(String name) {
		
		String propValue = propertyCache.get(name);
		if (Utils.isEmpty(propValue)) {
			propValue = xmltool.getProperty(name);
		}
		
		return propValue;
	}

	public void reset() {
		@SuppressWarnings("deprecation")
        String configPath=SystemData.getInstance().getServletInfo().getServletRealPath()+"WEB-INF/config/"+configFile;
		FrameworkHelper.LOG.info("load file : "+configPath);
			xmltool=new JsonConfigHelper(new File(configPath));
			propertyCache.clear();
	}

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

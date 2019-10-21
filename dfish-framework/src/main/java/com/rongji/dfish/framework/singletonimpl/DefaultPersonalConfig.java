package com.rongji.dfish.framework.singletonimpl;

import static com.rongji.dfish.framework.FrameworkHelper.ENCODING;

import java.io.File;
import java.util.Map;

import org.dom4j.DocumentException;

import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.PersonalConfigHolder;
import com.rongji.dfish.framework.SystemData;
import com.rongji.dfish.misc.cache.SizeFixedCache;
import com.rongji.dfish.misc.util.XMLUtil;

public class DefaultPersonalConfig implements PersonalConfigHolder{
	private static Map<String, String> propertiesPersonMap=new SizeFixedCache<String, String>(256);

	@Override
    public String getProperty(String userId, String argStr) {
		String tempStr = userId + "." + argStr;
		synchronized (propertiesPersonMap) {
	
			String value = propertiesPersonMap.get(tempStr);
			if (value != null) {
				return value;
			}
		}
		try {
			@SuppressWarnings("deprecation")
			JsonConfigHelper tool = new JsonConfigHelper(new File(SystemData.getInstance().getServletInfo()
					.getServletRealPath()
					+ "WEB-INF/config/person" + java.io.File.separator + userId + ".json"));
			String result = tool.getProperty(argStr);
			if (result != null && !result.equals("")) {
				synchronized (propertiesPersonMap) {
					propertiesPersonMap.put(tempStr, result);
				}
				return result;
			}
		} catch (Exception ex) {
		}
		if (!"default".equals(userId)) { // 防止死循环
			return getProperty("default", argStr);
		}
		return null;
	}

	@Override
    public void setProperty(String userId, String argStr, String value) {
		String tempStr = userId + "." + argStr;
		synchronized (propertiesPersonMap) {
	
			if (propertiesPersonMap.containsKey(tempStr)) {
				if ((value == null && propertiesPersonMap.get(tempStr) == null)
						|| (value != null && value.equals(propertiesPersonMap.get(tempStr)))) {
					return;
				}
			} // 如果相同就不要存了.
			propertiesPersonMap.put(tempStr, value);
		}
		// 如果文件不存在,则新建文件.
		@SuppressWarnings("deprecation")
        String fileFullName = SystemData.getInstance().getServletInfo().getServletRealPath()
				+ "WEB-INF/config/person" + java.io.File.separator + userId + ".json";
		File f = new File(fileFullName);
		JsonConfigHelper tool;
		tool = new JsonConfigHelper(f);
		tool.setProperty(argStr, value);
		return;
	}

	@Override
    public void reset() {
		propertiesPersonMap.clear();
	}

}

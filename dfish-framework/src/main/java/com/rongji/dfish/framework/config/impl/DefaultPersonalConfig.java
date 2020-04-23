package com.rongji.dfish.framework.config.impl;

import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.framework.config.PersonalConfigHolder;
import com.rongji.dfish.framework.info.ServletInfo;

import java.io.File;

/**
 * 默认个人配置实现类
 * 该实现类将系统配置保存在配置文件中
 * @since DFish2.x
 *
 * @author DFish Team
 */
public class DefaultPersonalConfig implements PersonalConfigHolder {
    private static MemoryCache<String, String> propsCache = new MemoryCache<>();

    @Override
    public String getProperty(String userId, String argStr) {
        String tempStr = userId + "." + argStr;
        synchronized (propsCache) {
            String value = propsCache.get(tempStr);
            if (value != null) {
                return value;
            }
        }
        try {
            @SuppressWarnings("deprecation")
            JsonConfigHelper tool = new JsonConfigHelper(new File(SystemContext.getInstance().get(ServletInfo.class)
                    .getServletRealPath()
                    + "WEB-INF/config/person" + java.io.File.separator + userId + ".json"));
            String result = tool.getProperty(argStr);
            if (result != null && !"".equals(result)) {
                synchronized (propsCache) {
                    propsCache.put(tempStr, result);
                }
                return result;
            }
        } catch (Exception ex) {
        }
        if (!"default".equals(userId)) {
        	// 防止死循环
            return getProperty("default", argStr);
        }
        return null;
    }

    @Override
    public void setProperty(String userId, String argStr, String value) {
        String tempStr = userId + "." + argStr;
        synchronized (propsCache) {

            if (propsCache.containsKey(tempStr)) {
                if ((value == null && propsCache.get(tempStr) == null)
                        || (value != null && value.equals(propsCache.get(tempStr)))) {
                    return;
                }
            } // 如果相同就不要存了.
            propsCache.put(tempStr, value);
        }
        // 如果文件不存在,则新建文件.
        @SuppressWarnings("deprecation")
        String fileFullName = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath()
                + "WEB-INF/config/person" + java.io.File.separator + userId + ".json";
        File f = new File(fileFullName);
        JsonConfigHelper tool;
        tool = new JsonConfigHelper(f);
        tool.setProperty(argStr, value);
        return;
    }

    @Override
    public void reset() {
        propsCache.clear();
    }

}

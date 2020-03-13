package com.rongji.dfish.framework.config.impl;

import com.rongji.dfish.base.cache.Cache;
import com.rongji.dfish.base.cache.impl.MemoryCache;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.config.SystemConfigHolder;
import com.rongji.dfish.framework.info.ServletInfo;

import java.io.File;

/**
 * 默认系统配置实现类
 * 该实现类将系统配置保存在配置文件中
 * @since 2.x
 * @date 2018-08-03 before
 * @author DFish Team
 */
public class DefaultSystemConfig implements SystemConfigHolder {
    private String configFile;
    private JsonConfigHelper xmltool;

    private Cache<String, String> propertyCache = new MemoryCache<>();

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
        String configPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath() + "WEB-INF/config/" + configFile;
        LogUtil.info("load file : " + configPath);
        xmltool = new JsonConfigHelper(new File(configPath));
        propertyCache.clear();
    }

    @Override
    public void setProperty(String key, String value) {
        xmltool.setProperty(key, value);
        // 设置缓存
        propertyCache.put(key, value);
    }

    /**
     * 获取配置文件名
     * @return String
     */
    public String getConfigFile() {
        return configFile;
    }

    /**
     * 设置配置文件名
     * @param configFile String
     */
    public void setConfigFile(String configFile) {
        this.configFile = configFile;
        reset();
    }

}

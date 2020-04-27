package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.config.impl.DefaultPropertyCryptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Description: 属性配置器，配置参数加载的工具
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		lamontYu
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018-3-16 下午1:37:35		lamontYu			1.0				1.0 Version
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

	protected PropertyCryptor propertyCryptor;

	/**
	 * 获得PropertyCryptor
	 * @return 属性加密器
	 */
	public PropertyCryptor getPropertyCryptor() {
		return propertyCryptor;
	}

	/**
	 * 设置PropertyCryptor
	 * @param propertyCryptor 属性加密器
	 */
	public void setPropertyCryptor(PropertyCryptor propertyCryptor) {
		this.propertyCryptor = propertyCryptor;
	}

	/**
	 * 属性配置缓存,以便系统中调用
	 */
	private static Properties props;
	/**
	 * 默认的加密后缀
	 */
	public static final String ENCRYPTION_SUFFIX = ".encryption";

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties properties)
	        throws BeansException {
		// 获取系统配置加密器,为空时使用默认配置加密器
		propertyCryptor = propertyCryptor == null ? new DefaultPropertyCryptor() : propertyCryptor;
		int suffixLength = ENCRYPTION_SUFFIX.length();
		// 加密的属性
		Map<String, String> encryptProps = new HashMap<String, String>();
		// Entry<Object, Object> entry : properties.entrySet()
		for (Iterator<Entry<Object, Object>> iter = properties.entrySet().iterator(); iter.hasNext();) {
			Entry<Object, Object> entry = iter.next();
			String key = String.valueOf(entry.getKey());
			if (key.endsWith(ENCRYPTION_SUFFIX)) { // 若配置名称后缀带有密文标识,需要进行解密
				String value = String.valueOf(entry.getValue());
				try {
	                value = propertyCryptor.decrypt(value); // 解密密文
                } catch (Exception e) {
	                LogUtil.error("配置解密失败[" + key + "]", e);
                }
				// 解密完成后,将配置名称的密文标识去除,设置加进去
				encryptProps.put(key.substring(0, key.length() - suffixLength), value);
				// 将原加密的名称去除,已经没有作用
				iter.remove();
			}
		}
		// 将加密的的属性值设置
		for (Entry<String, String> entry : encryptProps.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
		super.processProperties(beanFactoryToProcess, properties);
		props = properties;
	}

	/**
	 * 获取属性配置值(字符)
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return String 属性值(字符)
	 */
	public static String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

	/**
	 * 获取属性配置值(数值)
	 * @param key 属性名
	 * @param defaultValue 默认值
	 * @return Integer 属性值(数值)
	 */
	public static Integer getPropertyAsInteger(String key, Integer defaultValue) {
		String value = getProperty(key, defaultValue == null ? null : defaultValue.toString());
		if (Utils.notEmpty(value)) {
			return Integer.valueOf(value);
		}
		return null;
	}
	
}
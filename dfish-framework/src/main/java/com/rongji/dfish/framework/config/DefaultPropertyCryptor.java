package com.rongji.dfish.framework.config;

import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;

/**
 * Description: 默认的属性加密器,用于系统参数属性配置解密
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		YuLM
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018-3-23 下午5:46:04		YuLM			1.0				1.0 Version
 */
public class DefaultPropertyCryptor implements PropertyCryptor {

	/**
	 * 密钥
	 */
	private static final String SECRET_KEY = "DFISH";
	/**
	 * 加密器
	 */
	private static final StringCryptor SC = CryptFactory.getStringCryptor(CryptFactory.BLOWFISH, CryptFactory.UTF8,
			CryptFactory.URL_SAFE_BASE64, SECRET_KEY);
	
	/**
	 * 获取加密器
	 * @return StringCryptor
	 * @author YuLM
	 */
	protected StringCryptor getCryptor() {
		return SC;
	}
	
	@Override
    public String decrypt(String str) {
		StringCryptor sc = getCryptor();
		return sc.decrypt(str);
    }

	@Override
    public String encrypt(String str) {
		StringCryptor sc = getCryptor();
	    return sc.encrypt(str);
    }

}

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
	private String secretKey = "DFISH";
	/**
	 * 加密器
	 */
	private StringCryptor cryptor;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * 获取加密器
	 * @return StringCryptor
	 * @author YuLM
	 */
	protected StringCryptor getCryptor() {
		if (cryptor == null) {
			cryptor = CryptFactory.getStringCryptor(CryptFactory.BLOWFISH, CryptFactory.UTF8,
					CryptFactory.BASE32, secretKey);
		}
		return cryptor;
	}
	
	@Override
    public String decrypt(String str) {
		StringCryptor cryptor = getCryptor();
		return cryptor.decrypt(str);
    }

	@Override
    public String encrypt(String str) {
		StringCryptor cryptor = getCryptor();
	    return cryptor.encrypt(str);
    }

}

package com.rongji.dfish.framework.config.impl;

import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.CryptFactory;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.config.PropertyCryptor;

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

	private CryptProvider cryptProvider;

	public CryptProvider getCryptProvider() {
		return cryptProvider;
	}

	public void setCryptProvider(CryptProvider cryptProvider) {
		this.cryptProvider = cryptProvider;
	}

	/**
	 * 获取加密器
	 * @return StringCryptor
	 * @author lamontYu
	 */
	protected StringCryptor getCryptor() {
		if (cryptProvider == null) {
			cryptProvider = new CryptProvider();
		}
		return cryptProvider.getCryptor();
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

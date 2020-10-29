package com.rongji.dfish.framework.config.impl;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.crypto.CryptorBuilder;
import com.rongji.dfish.framework.config.PropertyCryptor;

/**
 * Description: 默认的属性加密器,用于系统参数属性配置解密
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		lamontYu
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018-3-23 下午5:46:04		lamontYu			1.0				1.0 Version
 */
public class DefaultPropertyCryptor implements PropertyCryptor {

	private CryptorBuilder cryptorBuilder;

	/**
	 * getter
	 * @return CryptorBuilder
	 */
	public CryptorBuilder getCryptorBuilder() {
		return cryptorBuilder;
	}

	/**
	 * setter
	 * @param cryptorBuilder CryptorBuilder
	 */
	public void setCryptorBuilder(CryptorBuilder cryptorBuilder) {
		this.cryptorBuilder = cryptorBuilder;
	}

	@Override
    public String decrypt(String str) {
//		StringCryptor cryptor = getCryptor();
		return getCryptorBuilder().build().decrypt(str);
    }

	@Override
    public String encrypt(String str) {
//		StringCryptor cryptor = getCryptor();
	    return getCryptorBuilder().build().encrypt(str);
    }

}

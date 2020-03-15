package com.rongji.dfish.framework.config.impl;

import com.rongji.dfish.base.crypto.Cryptor;
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

	private Cryptor cryptor;

	/**
	 * 获得Cryptor
	 * @return
	 */
	public Cryptor getCryptor() {
		return cryptor;
	}

	/**
	 * Cryptor
	 * @param cryptProvider
	 */
	public void setCryptor(Cryptor cryptProvider) {
		this.cryptor = cryptor;
	}


	
	@Override
    public String decrypt(String str) {
//		StringCryptor cryptor = getCryptor();
		return cryptor.decrypt(str);
    }

	@Override
    public String encrypt(String str) {
//		StringCryptor cryptor = getCryptor();
	    return cryptor.encrypt(str);
    }

}

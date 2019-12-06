package com.rongji.dfish.framework.config;

/**
 * Description: 参数属性加密器
 * Copyright:   Copyright © 2018
 * Company:     rongji
 * @author		lamontYu
 * @version		1.0
 *
 * Modification History:
 * Date						Author			Version			Description
 * ------------------------------------------------------------------
 * 2018-3-23 下午3:21:35		lamontYu			1.0				1.0 Version
 */
public interface PropertyCryptor {

	/**
	 * 解密密文
	 * @param str String 需要被解密的字符
	 * @return String
	 */
	String decrypt(String str);
	
	/**
	 * 加密字符
	 * @param str String 需要加密的字符
	 * @return String
	 */
	String encrypt(String str);
	
	
}

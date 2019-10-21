package com.rongji.dfish.base.crypt;
/**
 *
 * NoneCryptor 没有加密,直接就是用base64或十六进制数字显示(文本和内码间的转化)
 *
 * @author LinLW
 * @version 5.0
 */
public final class NoneCryptor extends StringCryptor {
	 NoneCryptor(String encoding, int presentStyle){
		super.encoding = encoding;
		super.presentStyle = presentStyle;
	}

    /**
     * 原文和密文相同
     * @param src byte[]
     * @return byte[]
     * @throws Exception
     */
    @Override
    protected byte[] encrypt(byte[] src) throws Exception {
		return src;
	}
    /**
     * 原文和密文相同
     * @param src byte[]
     * @return byte[]
     * @throws Exception
     */
    @Override
    protected byte[] decrypt(byte[] src) throws Exception {
		return src;
	}

}

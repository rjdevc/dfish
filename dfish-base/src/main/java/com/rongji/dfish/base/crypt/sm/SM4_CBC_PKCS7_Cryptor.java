package com.rongji.dfish.base.crypt.sm;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.rongji.dfish.base.crypt.StringCryptor;

public class SM4_CBC_PKCS7_Cryptor extends StringCryptor {
	private static final byte[] DEFAULT_SECRET_KEY=":sm4.rongji.com:".getBytes();
	private static final byte[] DEFAULT_IV=":sm4.rongji.com:".getBytes();
	public SM4_CBC_PKCS7_Cryptor(String encoding, int presentStyle,Object arg){
		this.encoding=encoding;
		this.presentStyle=presentStyle;
		if(arg==null){
			secretKey=DEFAULT_SECRET_KEY;
			iv=DEFAULT_IV;
		}else	if(arg instanceof String){
			secretKey=getKeyFromString((String)arg);
			iv=DEFAULT_IV;
		}else	if(arg instanceof Object[]||arg instanceof String[]){
			Object[] cast=(Object[])arg;
			secretKey=getKeyFromString((String)cast[0]);
			iv=getKeyFromString((String)cast[1]);
		}
	}

	private byte[] secretKey;
	private byte[] iv;
	@Override
	protected byte[] encrypt(byte[] src) throws Exception {
		SM4_Context ctx = new SM4_Context();
		ctx.isPadding = true;
		ctx.mode = SM4.SM4_ENCRYPT;
		
		SM4 sm4 = new SM4();
		sm4.sm4_setkey_enc(ctx, secretKey);
		return sm4.sm4_crypt_cbc(ctx, iv, src);
	}
	

	@Override
	protected byte[] decrypt(byte[] src) throws Exception {
		SM4_Context ctx = new SM4_Context();
		ctx.isPadding = true;
		ctx.mode = SM4.SM4_DECRYPT;
		
		SM4 sm4 = new SM4();
		sm4.sm4_setkey_dec(ctx, secretKey);
		return sm4.sm4_crypt_cbc(ctx, iv, src);
	}

}

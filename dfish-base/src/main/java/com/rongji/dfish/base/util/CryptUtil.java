package com.rongji.dfish.base.util;

import com.rongji.dfish.base.crypt.*;

import java.io.*;
import java.util.ArrayList;

/**
 * 加解密常用的方法
 * <p><strong>usage</strong><br/>
 * Cryptor c= CryptUtil.prepareCryptor(CryptUtil.ALGORITHM_BLOWFISH,"password".getBytes())
 * .present(CryptUtil.PRESENT_BASE32).build();
 * String s=c.decrypt(src);
 * </p>
 *
 * @author DFish team
 *
 */
public class CryptUtil {
	public static final String ALGORITHM_NONE=null;
	/**
	 * BLOWFISH加解密方法,由于相同源可以得到不同结果,而且破解就像当于得到加密密钥的过程所以有相当的安全性,效率还可以.
	 * BLOWFISH 使用的秘钥 可以是1-16byte
	 */
	public static final String ALGORITHM_BLOWFISH = Cryptor.CryptorBuilder.ALGORITHM_BLOWFISH;
	/**
	 * 一个比较早期的加解密方法。一个字节改变可以改变8个字节的密文。安全性不是是很高。 后面一般用多次加密的方法。多重DES。如Triple DES
	 * DES 使用的秘钥 是8byte
	 * @see #ALGORITHM_TRIPLE_DES
	 */
	public static final String ALGORITHM_DES =  Cryptor.CryptorBuilder.ALGORITHM_DES;
	/**
	 * AES算法，改算法由DES和RSA融合而成，曾在美国军方使用，现已退役，但对于民用安全性还行
	 * DES 使用的秘钥 是 8byte
	 */
	public static final String ALGORITHM_AES =  Cryptor.CryptorBuilder.ALGORITHM_AES;
	/**
	 * 三重DES算法。
	 * TRIPLE_DES 使用的秘钥 是8byte
	 * @see #ALGORITHM_DES
	 */
	public static final String ALGORITHM_TRIPLE_DES =  Cryptor.CryptorBuilder.ALGORITHM_TRIPLE_DES;


	/**
	 * MD5数字摘要方法
	 * 注意不可解密
	 */
	public static final String ALGORITHM_MD5 = Digester.DigesterBuilder.ALGORITHM_MD5;
	/**
	 * SHA-1数字摘要方法，精度为160位。安全性比MD5高
	 * 注意不可解密
	 */
	public static final String ALGORITHM_SHA1 = Digester.DigesterBuilder.ALGORITHM_SHA1;
	/**
	 * SHA数字摘要方法，精度为256位。
	 * 注意不可解密
	 */
	public static final String ALGORITHM_SHA256 = Digester.DigesterBuilder.ALGORITHM_SHA256;

	/**
	 * SHA数字摘要方法，精度为512位。
	 * 注意不可解密
	 */
	public static final String ALGORITHM_SHA512 = Digester.DigesterBuilder.ALGORITHM_SHA512;

	public static final int PRESENT_RAW = AbstractCryptor.AbstractCryptBuilder.PRESENT_RAW;
	public static final int PRESENT_HEX = AbstractCryptor.AbstractCryptBuilder.PRESENT_HEX;
	public static final int PRESENT_BASE64 = AbstractCryptor.AbstractCryptBuilder.PRESENT_BASE64;
	public static final int PRESENT_BASE32 = AbstractCryptor.AbstractCryptBuilder.PRESENT_BASE32;
	/**
	 * 注意和 RTF4648 的并不完全一致，该表示方式没有最后的PAD(=)
	 * 加密的话和旧版本有所不同，后面不再带点号(.) 所以新版本加密的在旧版本中将无法解密
	 * 但旧版本加密的新版本中可以解密。
	 */
	public static final int PRESENT_BASE64_URLSAFE = AbstractCryptor.AbstractCryptBuilder.PRESENT_BASE64_URLSAFE;

	/**
	 * 字符集用GBK
	 */
	public static final String ENCODING_GBK = AbstractCryptor.AbstractCryptBuilder.ENCODING_GBK;
	/**
	 * 字符集用UTF-8
	 */
	public static final String ENCODING_UTF8 = AbstractCryptor.AbstractCryptBuilder.ENCODING_UTF8;

	public static Cryptor.CryptorBuilder prepareCryptor(String algorithm, Object key){
		return Cryptor.CryptorBuilder.create(algorithm,key);
	}
	public static Digester.DigesterBuilder prepareDigester(String algorithm){
		return Digester.DigesterBuilder.create(algorithm);
	}

}

package com.rongji.dfish.base.util;

import com.rongji.dfish.base.crypt.AbstractCryptor;
import com.rongji.dfish.base.crypt.Cryptor;
import com.rongji.dfish.base.crypt.Digester;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加解密常用的方法
 * @author DFish team
 *
 */
public class CryptUtil {
	/**
	 * BLOWFISH加解密方法,由于相同源可以得到不同结果,而且破解就像当于得到加密密钥的过程所以有相当的安全性,效率还可以.
	 * BLOWFISH 使用的秘钥 可以是1-16byte
	 */
	public static final String BLOWFISH = "Blowfish";
	/**
	 * 一个比较早期的加解密方法。一个字节改变可以改变8个字节的密文。安全性不是是很高。 后面一般用多次加密的方法。多重DES。如Triple DES
	 * DES 使用的秘钥 是8byte
	 * @see #TRIPLE_DES
	 */
	public static final String DES = "DES";
	/**
	 * AES算法，改算法由DES和RSA融合而成，曾在美国军方使用，现已退役，但对于民用安全性还行
	 * DES 使用的秘钥 是 8byte
	 */
	public static final String AES = "AES";
	/**
	 * 三重DES算法。
	 * TRIPLE_DES 使用的秘钥 是8byte
	 * @see #DES
	 */
	public static final String TRIPLE_DES = "DESede";

	public static Cryptor.CryptorBuilder prepareCryptor(String algorithm, Object key){
		return Cryptor.CryptorBuilder.create(algorithm,key);
	}
	public static Digester.DigesterBuilder prepareDigester(String algorithm){
		return Digester.DigesterBuilder.create(algorithm);
	}
	public static void main (String[] args) throws IOException {
		Cryptor c=CryptUtil.prepareCryptor(DES,"THIS_IS_".getBytes())
				.gzip(true).encoding("UTF-8").present(1).build();
		String src="君不见，黄河之水天上来⑵，奔流到海不复回。\n" +
				"君不见，高堂明镜悲白发，朝如青丝暮成雪⑶。\n" +
				"人生得意须尽欢⑷，莫使金樽空对月。\n" +
				"天生我材必有用，千金散尽还复来。\n" +
				"烹羊宰牛且为乐，会须一饮三百杯⑸。\n" +
				"岑夫子，丹丘生⑹，将进酒，杯莫停⑺。\n" +
				"与君歌一曲⑻，请君为我倾耳听⑼。\n" +
				"钟鼓馔玉不足贵⑽，但愿长醉不复醒⑾。\n" +
				"古来圣贤皆寂寞，惟有饮者留其名。\n" +
				"陈王昔时宴平乐，斗酒十千恣欢谑⑿。\n" +
				"主人何为言少钱⒀，径须沽取对君酌⒁。\n" +
				"五花马⒂，千金裘，呼儿将出换美酒，与尔同销万古愁⒃。 [1] ";
		src="dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
		String en=c.encode(src);
		System.out.println(en);
		System.out.println("长度由"+src.getBytes("UTF8").length+"变为"+en.length());
		String de=c.decode(en);
		System.out.println(de);

		Digester d=CryptUtil.prepareDigester("SHA-1")
				.encoding("UTF-8").present(1).build();
		String di=d.digest(src);
		System.out.println(di);


//		ByteArrayOutputStream baos1=new ByteArrayOutputStream();
//		AbstractCryptor.HexOutputStream hos =new AbstractCryptor.HexOutputStream(baos1);
//		hos.write("haha".getBytes());
//		String hex=new String(baos1.toByteArray());
//		System.out.println(hex);
//		baos1.reset();
//
//		ByteArrayInputStream bais=new ByteArrayInputStream(hex.getBytes());
//		AbstractCryptor.HexInputStream his =new AbstractCryptor.HexInputStream(bais);
//		byte[] buff=new byte[8192];
//		int r=his.read(buff);
//		String res=new String(buff,0,r,"UTF-8");
//		System.out.println(res);

	}
}

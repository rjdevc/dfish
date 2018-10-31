package com.rongji.dfish.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
	/**
	 * JCECryptTool 用来加解密内容，JCECryptTool 它用于加解密较大的数据量，或文件。所以他的接口是按流设计的。
	 * <pre>
	 * CryptUtil.JCECryptTool tool=new CryptUtil.JCECryptTool(CryptUtil.BLOWFISH,"THIS.IS.PASSWORD".getBytes("UTF-8"));
	 * tool.setIn(fis1);
	 * tool.setOut(fos1);
	 * tool.encrypt();
	 * fis1.close();
	 * fos1.close();
	 * </pre>
	 * 因为设计用途，所以他实际上更多时刻只适用于使用分组秘钥算法。所以一般只用以下几种算法。
	 * {@link CryptUtil#AES} {@link CryptUtil#DES} {@link CryptUtil#TRIPLE_DES} {@link CryptUtil#BLOWFISH}
	 * 注意各种算法秘钥长度不尽相同。
	 * 现在的版本暂时不支持SM4。
	 * @author DFishTeam
	 *
	 */
	public static class JCECryptTool {
		protected Cipher cipher;
		protected SecretKeySpec sks;
		protected OutputStream out;
		protected InputStream in;
		/**
		 * 构造函数
		 * @param algorithm 算法
		 * @param key 秘钥 byte[]
		 */
		public JCECryptTool(String algorithm, byte[] key) {
			try {
				sks = new SecretKeySpec(key, algorithm);
				cipher = Cipher.getInstance(algorithm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public OutputStream getOut() {
			return out;
		}

		public void setOut(OutputStream out) {
			this.out = out;
		}

		public InputStream getIn() {
			return in;
		}

		public void setIn(InputStream in) {
			this.in = in;
		}
		/**
		 * 开始执行加密操作
		 */
		public void encrypt() {
			try {
				cipher.init(Cipher.ENCRYPT_MODE, sks);
				doCrypt();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		protected void doCrypt() throws IOException, IllegalBlockSizeException, BadPaddingException {
			if (in == null || out == null) {
				throw new NullPointerException("in or out must be setl");
			}
			byte[] buff = new byte[8192];
			int readed = 0;
			while ((readed = in.read(buff)) > 0) {
				byte[] b = cipher.update(buff, 0, readed);
				if (b != null && b.length > 0) {
					out.write(b);
				}
			}
			byte[] b = cipher.doFinal();
			if (b != null && b.length > 0) {
				out.write(b);
			}
		}
		/**
		 * 开始执行解密操作
		 */
		public void decrypt() {
			try {
				cipher.init(Cipher.DECRYPT_MODE, sks);
				doCrypt();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 因为 一般文本信息熵较低，直接压塑会有较好的效果。如果先加密在zip的话，压缩率就会很小。
	 * 所以一般都是先GZIP再加密。形成一个固定用法，所以，封装一个固定用法。
	 * <pre>
	 * CryptUtil.GzipAndCryptTool tool=new CryptUtil.GzipAndCryptTool(CryptUtil.BLOWFISH,"THIS.IS.PASSWORD".getBytes("UTF-8"));
	 * tool.setIn(fis1);
	 * tool.setOut(fos1);
	 * tool.encrypt();
	 * fis1.close();
	 * fos1.close();
	 * </pre>
	 */
	public static class GzipAndCryptTool extends JCECryptTool{

		public GzipAndCryptTool(String algorithm, byte[] key) {
			super(algorithm,key);
		}

		public void encrypt() {
			try {
				if (in == null || out == null) {
					throw new NullPointerException("in or out must be setl");
				}
				cipher.init(Cipher.ENCRYPT_MODE, sks);
				GZIPOutputStream gos=new GZIPOutputStream(new OutputStream(){
					@Override
					public void write(int b) throws IOException {
						throw new java.lang.UnsupportedOperationException();
					}

					@Override
					public void write(byte[] buff) throws IOException {
						byte[] b = cipher.update(buff, 0, buff.length);
						if (b != null && b.length > 0) {
							out.write(b);
						}
					}

					@Override
					public void write(byte[] buff, int off, int len) throws IOException {
						byte[] b = cipher.update(buff, off, len);
						if (b != null && b.length > 0) {
							out.write(b);
						}
					}
				});
				byte[] buff = new byte[8192];
				int readed =0;
				while ((readed = in.read(buff)) > 0) {
					gos.write(buff, 0, readed);
				}
				gos.close();
				byte[] b = cipher.doFinal();
				if (b != null && b.length > 0) {
					out.write(b);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		public void decrypt() {
			try {
				if (in == null || out == null) {
					throw new NullPointerException("in or out must be setl");
				}
				cipher.init(Cipher.DECRYPT_MODE, sks);
				GZIPInputStream gis=new GZIPInputStream(new InputStream(){
					private byte[] lastBytes;
					private int off;
					private int len;
					
					@Override
					public int read() throws IOException {
						byte[] buff=new byte[1];
						if(read(buff,0,1)<1){
							return -1;
						}
						return buff[0]&0xFF;
					}
					@Override
					public int read(byte[] buff,int off, int len) throws IOException {
						//1.从遗留的buff中读取内容，如果超过buff长度。则直接返回
						if(this.len>=len){
							System.arraycopy(lastBytes, this.off, buff, off, len);
							this.off+=len;
							this.len-=len;
							return len;
						}
						//2.如果不够长度，则尝试从in 中读取一次内容。并把遗留的内容，外加新内容的前面一部分返回
						int readed=0;
						if(this.len>0){
							System.arraycopy(lastBytes, this.off, buff, off, this.len);
							readed+=this.len;
							this.off=0;
							this.len=0;
						}
						
						byte[] inBuff=new byte[8192];
						int inReaded=in.read(inBuff);
						byte[] b=null;
						if(inReaded>0){
							b = cipher.update(inBuff, 0, inReaded);
						}else{
							//3.如果已经读不到内容，则doFinal
							try {
								b = cipher.doFinal();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(b!=null&&b.length>0){
							int copyBytes=Math.min(len-readed,b.length);
							System.arraycopy(b, 0, buff, off+readed, copyBytes);
							readed+=copyBytes;
							lastBytes=b;
							this.off=copyBytes;
							this.len=b.length-copyBytes;
						}
						return readed;
					}
				});
				byte[] buff = new byte[8192];
				int readed = 0;
				while ((readed = gis.read(buff)) > 0) {
					out.write(buff,0,readed);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
}

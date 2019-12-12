package com.rongji.dfish.base.crypt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

/**
 *
 * StringCryptor 定义一个可加密,解密的工具,原文和密文都是String型
 *
 * @author LInLW
 * @version 1.0
 * @deprecated 现在主推流模式
 * @see Cryptor
 */
@Deprecated
public abstract class StringCryptor {
  /**
   * 内码转化成字符串所用的方法
   */
  protected int presentStyle;
  /**
   * 内码转化成字符串所用的方法
   * 这里用十六进制字符表达
   */
  public static final int PRESENT_STYLE_HEX_STRING = 1;

  /**
   * 内码转化成字符串所用的方法
   * 这里用BASE64算法转化
   */
  public static final int PRESENT_STYLE_BASE64 = 2;

  /**
   * 内码转化成字符串所用的方法
   * 这里用BASE64算法转化然後把BASE64的+/=符號替換成網絡傳輸中不失真的字符-_.
   */
  public static final int PRESENT_STYLE_URL_SAFE_BASE64 = 3;
  /**
   * 内码转化成字符串所用的方法
   * 这里用BASE32算法转化
   */
  public static final int PRESENT_STYLE_BASE32 = 4;
  
  /**
   * 先调用压缩算法，再转成BASE_64 
   * 一般只适合大片的文本原文，密文的话，压缩算法的效率很低。
   * @deprecated 一般是UTF8_GZIP 配合BASE64来使用。很少使用GZIP_WITH_BASE64 按一般情况估计，原文GZIP会有较好的压缩率，而密文GZIP的压缩率一般较低
   */
  @Deprecated
  public static final int PRESENT_STYLE_GZIP_WITH_BASE64 = 5;
  
  /**
   * 字符集只支持GBK和UTF-8两种
   */
  protected String encoding;
  /**
   * 字符集用GBK
   */
  public static final String ENCODING_GBK = "GBK";
  /**
   * 字符集用UTF-8
   */
  public static final String ENCODING_UTF8 = "UTF-8";
  /**
   * 字符集用UTF-8,并对产生的byte数组进行一次GZIP压缩
   */
  public static final String ENCODING_UTF8_GZIP = "UTF8_GZIP";
  /**
   * 字符集用UNICODE
   */
  public static final String ENCODING_UNICODE = "UNICODE";

//  private static final BASE64Decoder B64DE = new BASE64Decoder();
//
//  private static final BASE64Encoder B64EN = new BASE64Encoder();

  private static final int[] HEX_DE_INT = { // 用于加速解密的cache
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
      0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
      0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

  private static final char[] HEX_EN_CHAR = { // 用于加速加密的cache
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
      'F'};

  /**
   * 解密密字符串成原文
   *
   * @param str 密文
   * @return 原文
   * @throws IllegalArgumentException
   * @throws RuntimeException
   */
  public final String decrypt(String str) {
    if (str == null || "".equals(str)) {
      return str;
    }
    byte[] b = null;
    switch (presentStyle) {
      case PRESENT_STYLE_HEX_STRING: {
        b = hex2byte(str);
        break;
      }
      case PRESENT_STYLE_BASE64: {
        b = Base64.decode(str.replace("\n", "").replace("\r", "").getBytes());
        break;
      }
      case PRESENT_STYLE_BASE32: {
          b = Base32.decode(str.getBytes());
          break;
      }
      case PRESENT_STYLE_GZIP_WITH_BASE64: {
    	  b = Base64.decode(str.replace("\n", "").replace("\r", "").getBytes());
    	  int i=0;
    	  byte[] buff=new byte[8192];
    	  ByteArrayOutputStream baos=new ByteArrayOutputStream();
			try {
				GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(b));
				while((i=zis.read(buff))>=0){
					baos.write(buff, 0, i);
				}
				zis.close();
				b=baos.toByteArray();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          break;
      }
      case PRESENT_STYLE_URL_SAFE_BASE64: {
        b = UrlSafeBase64.decode(str);
        break;
      }
    }

    byte[] de;
    try {
      de = decrypt(b);
    }
    catch (RuntimeException e1) {
      throw e1;
    }
    catch (Exception ex2) {
      throw new RuntimeException("Can not decrypt the code!\r\nsrc=" + str);
    }

    try {
    	if(ENCODING_UTF8_GZIP.equals(encoding)){
    		ByteArrayOutputStream baos=new ByteArrayOutputStream();
    		  int i=0;
        	  byte[] buff=new byte[8192];
			try {
				GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(de));
				while((i=zis.read(buff))>=0){
					baos.write(buff, 0, i);
				}
				zis.close();
				de=baos.toByteArray();
				return new String(de, ENCODING_UTF8);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
    	}else{
    		return new String(de, encoding);
    	}
    }
    catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(
          "The encoding setting ("
          + encoding + ") is unavilable!");
    }
  }

  /**
   * 加密字符串成密文
   *
   * @param str 原文
   * @return 密文
   * @throws IllegalArgumentException
   * @throws RuntimeException
   */
  public final String encrypt(String str) {
    if (str == null || "".equals(str)) {
      return str;
    }
    byte[] b = null;
    try {
    	if(ENCODING_UTF8_GZIP.equals(encoding)){
    		b = str.getBytes(ENCODING_UTF8);
    		ByteArrayOutputStream baos=new ByteArrayOutputStream();
    		try {
	    		GZIPOutputStream zos=new GZIPOutputStream(baos);
				zos.write(b);
				zos.close();
				b=baos.toByteArray();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}else{
    		b = str.getBytes(encoding);
    	}
    }
    catch (UnsupportedEncodingException e) {
      throw new IllegalArgumentException(
          "The encoding setting ("
          + encoding + ") is unavilable!");
    }
    byte[] en;
    try {
      en = encrypt(b);
    }
    catch (RuntimeException e1) {
      throw e1;
    }
    catch (Exception ex2) {
    	ex2.printStackTrace();
      throw new RuntimeException("Can not encrypt the code!\r\nsrc=" + str);
    }
    switch (presentStyle) {
      case PRESENT_STYLE_HEX_STRING:
        return byte2hex(en);
      case PRESENT_STYLE_BASE64:
        return new String(Base64.encode(en)).replace("\n", "").replace("\r", "");
      case PRESENT_STYLE_GZIP_WITH_BASE64:
//    	  en=LZ77.encode(en);
    	ByteArrayOutputStream baos=new ByteArrayOutputStream();
  		try {
			GZIPOutputStream zos=new GZIPOutputStream(baos);
			zos.write(en);
			zos.close();
			return new String(Base64.encode(baos.toByteArray())).replace("\n", "").replace("\r", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
  		return null;
      case PRESENT_STYLE_BASE32:
          return  new String(Base32.encode(en));
      case PRESENT_STYLE_URL_SAFE_BASE64:
       return UrlSafeBase64.encode(en);
    }
    return null;
  }

  /**
   * 加密原文成密文,数据格式为内码(二进制 byte数组)
   * @param src byte[]
   * @return byte[]
   * @throws Exception
   */
  protected abstract byte[] encrypt(byte[] src) throws Exception;

  /**
   * 解密密文成原文,数据格式为内码(二进制 byte数组)
   * @param src byte[]
   * @return byte[]
   * @throws Exception
   */
  protected abstract byte[] decrypt(byte[] src) throws Exception;

  /**
   * 字节数组转化成字符串
   *
   * @param b byte[]
   * @return String
   */
  public final static String byte2hex(byte[] b) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < b.length; i++) {
      int inte = b[i] & 0xff;
      result.append(HEX_EN_CHAR[inte >> 4]);
      result.append(HEX_EN_CHAR[inte & 0x0f]);
    }
    return result.toString();
  }

  /**
   * 字符串转化成字节数组
   *
   * @param hex String
   * @return byte[]
   * @throws IllegalArgumentException
   */
  public final static byte[] hex2byte(String hex) {
    if ( (hex == null) || "".equals(hex)) {
      return null;
    }
    if ( (hex.length() & 1) == 1) {
      throw new IllegalArgumentException("hex length should not be odd.");
    }
    int len = hex.length();
    byte[] ba = new byte[len>>1];
    for (int i = 0; i < len; i += 2) {
      ba[i >>
          1] = (byte) (HEX_DE_INT[hex.charAt(i)] << 4 | HEX_DE_INT[hex
                       .charAt(i + 1)]);
    }
    return ba;
  }

  private static final char[] C = {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
      'F'};
  public static final String toString(byte[] b) {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    for (int i = 0; i < b.length; ) {
      sb.append(C[b[i] >> 4 & 0x0F]);
      sb.append(C[b[i] & 0x0F]);
      i++;
      if (i < b.length) {
        sb.append(',');
      }
    }
    sb.append(']');
    return sb.toString();
  }
	protected byte[] getKeyFromString(String arg) {
		String encoding=this.encoding;
		if(ENCODING_UTF8_GZIP.equals(encoding)){
			encoding=ENCODING_UTF8;
		}
		if(arg.length()==32){
			try {
				return hex2byte(arg);
			} catch (Exception e) {
				throw new RuntimeException("user 32 HEX string or 16 bytes string ["+encoding+"]");
			}
		}else{
			try {
				byte[] b=arg.getBytes(encoding);
				byte[] ret=new byte[16];
				System.arraycopy(b, 0, ret, 0, b.length>16?16:b.length);
				if(b.length<16){
					Arrays.fill(ret, b.length, 16, (byte)0);
				}
				return ret;
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("user 32 HEX string or 16 bytes string ["+encoding+"]");
			}
		}
	}
}

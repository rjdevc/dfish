package com.rongji.dfish.base.crypt;

import java.security.MessageDigest;

/**
 * MessageDigestCryptor 数字摘要 加密
 *
 * @author LinLW 参照itask原有加密做法
 * @version 5.0
 * @deprecated 现在推荐流模式
 */
@Deprecated
public class MessageDigestCryptor2007
    extends StringCryptor {
  private String algorithms;
  /**
   * 创建一个数字摘要的的加密类
   * @param algorithms String
   * @param encoding String
   * @param presentStyle int
   */
  protected MessageDigestCryptor2007(String algorithms, String encoding,
                                     int presentStyle) {
    super.encoding = encoding;
    super.presentStyle = presentStyle;
    this.algorithms = algorithms;
  }

  /**
   * 把数据摘要的byte数组加密
   * @param src byte[]
   * @return byte[]
   * @throws Exception
   */
  @Override
  protected synchronized byte[] encrypt(byte[] src) throws Exception {
    MessageDigest d = null;
    d = MessageDigest.getInstance(algorithms); // MD5 SHA-1
    d.reset();
    d.update(src);
    return d.digest();
  }

  /**
   * 数据摘要不可以解密，肯定会出异常。
   * @param src byte[]
   * @return byte[]
   * @throws Exception UnsupportedOperationException
   */
  @Override
  protected byte[] decrypt(byte[] src) throws Exception {
    throw new UnsupportedOperationException("This cryptor (" + algorithms
                                            + ") do not support decrypt !");
  }

}

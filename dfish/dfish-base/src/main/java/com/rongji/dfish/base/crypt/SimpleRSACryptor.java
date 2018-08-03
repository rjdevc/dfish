package com.rongji.dfish.base.crypt;

import java.math.BigInteger;

/**
 * SimpleRSACryptor 是简单RSA加解密,没有分块.所以支持的内容少,如果用默认密钥支持19个字节,
 *
 * @author LinLW
 * @version 1.0
 */
public final class SimpleRSACryptor
    extends StringCryptor {
  private BigInteger modulus;

  private BigInteger decryptKey;

  private BigInteger encryptKey;
  /**
   * 构造函数
   * @param encoding String
   * @param presentStyle int
   */
  public SimpleRSACryptor(String encoding, int presentStyle) {
    this(encoding, presentStyle, null);
  }
  /**
   * 带参数的构造函数
   * @param encoding String
   * @param presentStyle int
   * @param arg Object
   */
  SimpleRSACryptor(String encoding, int presentStyle, Object arg) {
    super.encoding = encoding;
    super.presentStyle = presentStyle;
    if (arg != null && arg instanceof BigInteger[]) {
      BigInteger[] cast = (BigInteger[]) arg;
      this.decryptKey = cast[0];
      this.encryptKey = cast[1];
      this.modulus = cast[2];
    }
    else {
      decryptKey = new BigInteger(
          "165082373040883377361978614966392409743541012468569");
      encryptKey = new BigInteger("3918894713");
      modulus = new BigInteger(
          "165725181879270469994038250733646804851649626344179");
    }
  }
  /**
   * 用JDK提供的算法进行带模的幂预算，达到加密的效果
   * @param src byte[]
   * @return byte[]
   * @throws Exception
   */
  protected byte[] encrypt(byte[] src) throws Exception {
    BigInteger bi = new BigInteger(src);
    if (bi.signum() == -1) {
      return bi.negate().modPow(encryptKey, modulus).negate()
          .toByteArray();
    }
    else {
      return bi.modPow(encryptKey, modulus).toByteArray();
    }
  }
  /**
   * 用JDK提供的算法进行带模的幂预算，达到解密的效果
   * @param src byte[]
   * @return byte[]
   * @throws Exception
   */
  protected byte[] decrypt(byte[] src) throws Exception {
    BigInteger bi = new BigInteger(src);
    if (bi.signum() == -1) {
      return bi.negate().modPow(decryptKey, modulus).negate()
          .toByteArray();
    }
    else {
      return bi.modPow(decryptKey, modulus).toByteArray();
    }
  }

}

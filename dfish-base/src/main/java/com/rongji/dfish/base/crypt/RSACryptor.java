package com.rongji.dfish.base.crypt;

import java.math.BigInteger;
import java.io.ByteArrayOutputStream;

/**
 * RSA 加解密的方法。
 * RSA由于模数有限，所以不能支持一大块文档直接加密生成文档。必须分块
 * 分块的时候，为了标识，某块已经结束了。必须用定位符来表示某块结束了。
 * 这里用来定位符的方法就是首位加0x01
 * 于是如果密钥的大于2<<<8*bytes小于模数，则支持bytes的加密
 *
 * 加密的时候，按bytes的数量把源数据分块，并在每块前加1，加密的数据大小为模数的byteLength长
 * 不足的前面补0x00
 * 串成byte数组
 *
 * 解密的时候，按模数的byteLength长把数据源划分成N个块。每块解出来后，切除第一个0x01以前的数据。
 * 重新串成原byte数组。
 *
 *
 * @deprecated 有BUG 已经废弃
 * @author LinLW
 * @version 1.0
 */
public class RSACryptor
    extends StringCryptor {
  private BigInteger modulus;

  private BigInteger decryptKey;

  private BigInteger encryptKey;
  private int supportByteLen;
  private int modulusByteLength;
  RSACryptor(String encoding, int presentStyle, Object arg) {
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
    modulusByteLength = getByteLength(modulus);

    supportByteLen = modulusByteLength - 2;
    BigInteger bi = BigInteger.ONE;
    bi = bi.shiftLeft(8 * supportByteLen + 1);
    while (bi.compareTo(modulus) > 0) {
      supportByteLen--;
      bi = bi.shiftRight(8);
    }
  }

  protected byte[] encrypt(byte[] src) throws Exception {
    int blocks = (src.length - 1) / supportByteLen + 1;
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    for (int i = 0; i < blocks; i++) {
      //按bytes的数量把源数据分块，并在每块前加1
      byte[] s = new byte[supportByteLen + 1];
      s[0] = (byte) 0x01;
      if (src.length > i * supportByteLen + supportByteLen) {
        System.arraycopy(src, supportByteLen * i, s, 1, supportByteLen);
      }
      else {
        System.arraycopy(src, supportByteLen * i, s, 1, src.length - supportByteLen * i);
      }
      BigInteger en_ = new BigInteger(s).modPow(encryptKey, modulus);
      byte[] en = en_.toByteArray();

      int enLength = en.length;
      if (enLength > 0 && en[0] == 0) {
        enLength--;
      }
      if (enLength > modulusByteLength) {
        throw new ArrayIndexOutOfBoundsException(enLength);
      }
      int trop = modulusByteLength - enLength;
      if (trop > 0) {
        ba.write(new byte[trop]);
      }
      if (enLength == en.length) {
        ba.write(en);
      }
      else { //被截取过
        ba.write(en, 0, enLength);
      }
    }
    return ba.toByteArray();
  }

  protected byte[] decrypt(byte[] src) throws Exception {
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    int blocks = src.length / modulusByteLength;
    for (int i = 0; i < blocks; i++) {
      BigInteger bi = null;
      if ( (src[i * modulusByteLength] & 0x80) > 0) {
        byte[] b = new byte[modulusByteLength + 1];
        b[0] = 0x00;
        System.arraycopy(src, i * modulusByteLength, b, 1, modulusByteLength);
        bi = new BigInteger(b);
      }
      else {
        byte[] b = new byte[modulusByteLength];
        System.arraycopy(src, i * modulusByteLength, b, 0, modulusByteLength);
        bi = new BigInteger(b);
      }
      BigInteger de_ = bi.modPow(decryptKey, modulus);
      byte[] de = de_.toByteArray();
      ba.write(de, 1, de.length - 1);
    }
    return ba.toByteArray();
  }

  private static int getByteLength(BigInteger b) {
    byte[] bs = b.toByteArray();
    if (bs[0] == 0) {
      return bs.length - 1;
    }
    return bs.length;
  }
}

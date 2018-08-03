package com.rongji.dfish.base.crypt;

/**
 * <p>Title: 榕基I-TASK执行先锋</p>
 *
 * <p>Description: 专门为提高企业执行力而设计的产品</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 榕基软件开发有限公司</p>
 *
 * @author LinLW
 * @version 1.0
 */
public final class UrlSafeBase64 {
  static private byte[] decode = new byte[256];
  static private byte[] encode = new byte[64];
  private static final byte SIGN = -128;
  private static final byte PAD = '.';
  static {
    for (int i = 0; i < 256; i++) {
      decode[i] = -1;
    }
    for (int i = 'Z'; i >= 'A'; i--) {
      decode[i] = (byte) (i - 'A');
    }
    for (int i = 'z'; i >= 'a'; i--) {
      decode[i] = (byte) (i - 'a' + 26);
    }
    for (int i = '9'; i >= '0'; i--) {
      decode[i] = (byte) (i - '0' + 52);
    }
    decode['+'] = 62;
    decode['/'] = 63;
    decode['-'] = 62;
    decode['_'] = 63;

    for (int i = 0; i <= 25; i++) {
      encode[i] = (byte) ('A' + i);
    }

    for (int i = 26, j = 0; i <= 51; i++, j++) {
      encode[i] = (byte) ('a' + j);
    }

    for (int i = 52, j = 0; i <= 61; i++, j++) {
      encode[i] = (byte) ('0' + j);
    }

    encode[62] = '-';
    encode[63] = '_';
  }

  public static String encode(byte[] ba) {
    int lengthDataBits = ba.length * 8;
    int fewerThan24bits = lengthDataBits % 24;
    int numberTriplets = lengthDataBits / 24;
    byte encodedData[] = null;

    if (fewerThan24bits != 0) {
      //data not divisible by 24 bit
      encodedData = new byte[ (numberTriplets + 1) * 4];
    }
    else {
      // 16 or 8 bit
      encodedData = new byte[numberTriplets * 4];
    }

    byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

    int encodedIndex = 0;
    int dataIndex = 0;
    int i = 0;
    for (i = 0; i < numberTriplets; i++) {
      dataIndex = i * 3;
      b1 = ba[dataIndex];
      b2 = ba[dataIndex + 1];
      b3 = ba[dataIndex + 2];

      l = (byte) (b2 & 0x0f);
      k = (byte) (b1 & 0x03);

      encodedIndex = i * 4;
      byte val1 = ( (b1 & SIGN) == 0) ? (byte) (b1 >> 2) :
          (byte) ( (b1) >> 2 ^ 0xc0);
      byte val2 = ( (b2 & SIGN) == 0) ? (byte) (b2 >> 4) :
          (byte) ( (b2) >> 4 ^ 0xf0);
      byte val3 = ( (b3 & SIGN) == 0) ? (byte) (b3 >> 6) :
          (byte) ( (b3) >> 6 ^ 0xfc);

      encodedData[encodedIndex] = encode[val1];
      encodedData[encodedIndex + 1] =
          encode[val2 | (k << 4)];
      encodedData[encodedIndex + 2] =
          encode[ (l << 2) | val3];
      encodedData[encodedIndex + 3] = encode[b3 & 0x3f];
    }

    // form integral number of 6-bit groups
    dataIndex = i * 3;
    encodedIndex = i * 4;
    if (fewerThan24bits == 8) {
      b1 = ba[dataIndex];
      k = (byte) (b1 & 0x03);
      //log.debug("b1=" + b1);
      //log.debug("b1<<2 = " + (b1>>2) );
      byte val1 = ( (b1 & SIGN) == 0) ? (byte) (b1 >> 2) :
          (byte) ( (b1) >> 2 ^ 0xc0);
      encodedData[encodedIndex] = encode[val1];
      encodedData[encodedIndex + 1] = encode[k << 4];
      encodedData[encodedIndex + 2] = PAD;
      encodedData[encodedIndex + 3] = PAD;
    }
    else if (fewerThan24bits == 16) {

      b1 = ba[dataIndex];
      b2 = ba[dataIndex + 1];
      l = (byte) (b2 & 0x0f);
      k = (byte) (b1 & 0x03);

      byte val1 = ( (b1 & SIGN) == 0) ? (byte) (b1 >> 2) :
          (byte) ( (b1) >> 2 ^ 0xc0);
      byte val2 = ( (b2 & SIGN) == 0) ? (byte) (b2 >> 4) :
          (byte) ( (b2) >> 4 ^ 0xf0);

      encodedData[encodedIndex] = encode[val1];
      encodedData[encodedIndex + 1] =
          encode[val2 | (k << 4)];
      encodedData[encodedIndex + 2] = encode[l << 2];
      encodedData[encodedIndex + 3] = PAD;
    }
    return new String(encodedData);
  }

  public static byte[] decode(String str) {
    char[] base64Data = str.toCharArray();
    if (base64Data.length == 0) {
      return new byte[0];
    }

    int numberQuadruple = base64Data.length / 4;
    byte decodedData[] = null;
    byte b1 = 0, b2 = 0, b3 = 0, b4 = 0, marker0 = 0, marker1 = 0;

    // Throw away anything not in base64Data

    int encodedIndex = 0;
    int dataIndex = 0;
    {
      // this sizes the output array properly - rlw
      int lastData = base64Data.length;
      // ignore the '=' padding
      while (base64Data[lastData - 1] == PAD) {
        if (--lastData == 0) {
          return new byte[0];
        }
      }
      decodedData = new byte[lastData - numberQuadruple];
    }

    for (int i = 0; i < numberQuadruple; i++) {
      dataIndex = i * 4;
      marker0 = (byte)base64Data[dataIndex + 2];
      marker1 = (byte)base64Data[dataIndex + 3];

      b1 = decode[base64Data[dataIndex]];
      b2 = decode[base64Data[dataIndex + 1]];

      if (marker0 != PAD && marker1 != PAD) {
        //No PAD e.g 3cQl
        b3 = decode[marker0];
        b4 = decode[marker1];

        decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
        decodedData[encodedIndex + 1] =
            (byte) ( ( (b2 & 0xf) << 4) | ( (b3 >> 2) & 0xf));
        decodedData[encodedIndex + 2] = (byte) (b3 << 6 | b4);
      }
      else if (marker0 == PAD) {
        //Two PAD e.g. 3c[Pad][Pad]
        decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
      }
      else if (marker1 == PAD) {
        //One PAD e.g. 3cQ[Pad]
        b3 = decode[marker0];

        decodedData[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
        decodedData[encodedIndex + 1] =
            (byte) ( ( (b2 & 0xf) << 4) | ( (b3 >> 2) & 0xf));
      }
      encodedIndex += 3;
    }
    return decodedData;
  }
}

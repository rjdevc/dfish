package com.rongji.dfish.base.crypto;

import java.io.*;

/**
 * 加解密工具
 * 与旧版比较。现在支持流模式。而并非全部都转化成String
 * 对于大的内容，或有性能要求的场景适用
 *
 */
public interface Cryptor {
    /**
     * 将 is 里面的内容加密后输出到 os 中去
     * @param is 输入流。输入流可以不用带buffered
     * @param os 输出流。输入流可以不用带buffered
     */
    void encrypt(InputStream is, OutputStream os);
    /**
     * 将 is 里面的内容解密后输出到 os 中去
     * 数字摘要算法是不可解密的，这类算法调用解密方法会出错。
     * 如果秘钥给错，也会解密失败
     * @param is 输入流。输入流可以不用带buffered
     * @param os 输出流。输入流可以不用带buffered
     */
    void decrypt(InputStream is, OutputStream os);

    /**
     * 将字符串内容加密
     * @param src 原文
     * @return 密文
     */
    String encrypt(String src);

    /**
     * 将字符串内容解密
     * 数字摘要算法是不可解密的，这类算法调用解密方法会出错。
     * 如果秘钥给错，也会解密失败
     * @param src 密文
     * @return 解密后的结果。
     */
    String decrypt(String src);
}

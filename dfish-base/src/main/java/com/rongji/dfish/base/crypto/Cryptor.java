package com.rongji.dfish.base.crypto;

import java.io.*;

public interface Cryptor {
    void encrypt(InputStream is, OutputStream os);
    void decrypt(InputStream is, OutputStream os);

    String encrypt(String src);
    String decrypt(String src);
}

package com.rongji.dfish.framework.service;

import com.rongji.dfish.base.crypt.CryptProvider;
import com.rongji.dfish.base.crypt.StringCryptor;
import com.rongji.dfish.framework.dao.BaseDao;

public class BaseService4Simple<P> extends BaseDao<P, String> {

    protected CryptProvider cryptProvider;

    public CryptProvider getCryptProvider() {
        return cryptProvider;
    }

    public void setCryptProvider(CryptProvider cryptProvider) {
        this.cryptProvider = cryptProvider;
    }

    protected StringCryptor getCryptor() {
        if (cryptProvider == null) {
            cryptProvider = new CryptProvider();
        }
        return cryptProvider.getCryptor();
    }

    /**
     * 加密字符
     *
     * @param str 加密前的字符
     * @return 加密后的密文
     */
    public String encrypt(String str) {
        return getCryptor().encrypt(str);
    }

    /**
     * 解密字符
     *
     * @param str 加密的密文
     * @return 解密后的字符
     */
    public String decrypt(String str) {
        return getCryptor().decrypt(str);
    }

    public String getNewId() {
        return IdGenerator.getSortedId32();
    }

}

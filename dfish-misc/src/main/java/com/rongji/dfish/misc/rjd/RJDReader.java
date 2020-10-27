package com.rongji.dfish.misc.rjd;

import com.rongji.dfish.base.crypto.stream.SM4ECBInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * RJD格式的读取器
 * <p></p>
 */
public class RJDReader {
    private ZipInputStream zis;
    private String password;
    private LinkedHashMap<String,RJDCallback> callbacks;
    private RJDCallback defaultCallback;

    /**
     * 新建一个读取器
     * @param is 输入流
     * @param password 密码
     * @throws IOException
     */
    public RJDReader(InputStream is, String password)throws IOException {
        zis=new ZipInputStream(is);
        this.password=password;
        callbacks=new LinkedHashMap<>();
    }

    /**
     * 读取内容，并当做String处理
     * @param callback RJDStringCallback
     * @throws IOException
     */
    public void readStringEntries(RJDStringCallback callback)throws IOException {
        readEntries(callback);
    }

    /**
     * 读取内容，
     * @param callback RJDCallback
     * @throws IOException
     */
    public void readEntries(RJDCallback callback)throws IOException {
        this.registerDefaultCallback(callback);
        doRead();
    }

    /**
     * 注册url符合这个pattern的用指定的callback来处理
     * 如果某个url可以满足多个pattern 以第一个注册的pattern为准
     * 如果pattern为* 则相当于registerDefaultCallback
     * defaultCallback 只有在没有任何匹配上的时候才被调用。
     * @param pattern 可以使用*为通配符
     * @param callback 回调
     */
    public void registerCallback(String pattern, RJDCallback callback) {
        if("*".equals(pattern)){
            registerDefaultCallback(callback);
        }else{
            callbacks.put(pattern,callback);
        }
    }

    /**
     * 注册默认的callback
     * defaultCallback 只有在没有任何匹配上的时候才被调用。
     * @param callback 回调
     */
    public void registerDefaultCallback(RJDCallback callback) {
        defaultCallback=callback;
    }

    /**
     * 开始读取内容
     * @throws IOException
     */
    public void doRead()throws IOException {
        ZipEntry ze;
        while((ze=zis.getNextEntry())!=null) {
            String url = ze.getName();
            RJDCallback callback=null;
            for(Map.Entry<String,RJDCallback> entry:callbacks.entrySet()){
                String pattern=entry.getKey();
                if(match(url,pattern)){
                    callback=entry.getValue();
                    break;
                }
            }
            if(callback==null){
                callback=defaultCallback;
            }
            if(callback!=null) {
                SM4ECBInputStream sm4is=new SM4ECBInputStream(zis,RJDWriter.getKey(password));
                callback.execute(url, sm4is);
            }
            zis.closeEntry();
        }
        zis.close();
    }

    /**
     * 判定字符串是否符合模式。 这里的模式 是可用*代替任何字符表示方式。
     * 比如 *.json  或 *data/*.json
     * 如果字符串符合这个格式，则返回true
     * @param str 字符串
     * @param pattern 模式
     * @return boolean
     */
    public static boolean match(String str, String pattern) {
        int len = str.length();
        int index = 0;
        char ch;
        for (int patIdx = 0, patLen = pattern.length(); patIdx < patLen; patIdx++) {
            ch = pattern.charAt(patIdx);
            if (ch == '*') {
                // 通配符星号*表示可以匹配任意多个字符
                while (index < len) {
                    if (match(str.substring(index),pattern.substring(patIdx + 1))) {
                        return true;
                    }
                    index++;
                }
            } else {
                if (index >= len || ch != str.charAt(index)) {
                    return false;
                }
                index++;
            }
        }
        return index == len;
    }

}


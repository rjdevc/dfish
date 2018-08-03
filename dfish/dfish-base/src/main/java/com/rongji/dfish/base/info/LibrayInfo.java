package com.rongji.dfish.base.info;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类信息
 *
 * @author LinLW
 * @version 1.0
 */
public final class LibrayInfo {
	private static final Log LOG=LogFactory.getLog(LibrayInfo.class);
//    /**
//     * 类路径
//     */
//    private static final String CLZ_NAME =
//            "com/rongji/dfish/base/info/LibrayInfo.class";
    /**
     * 单例类
     */
    private static LibrayInfo instance = new LibrayInfo();
    /**
     * 取得单例
     * @return LibaryInfo
     */
    public static LibrayInfo getLibaryInfo() {
        return instance;
    }

    /**
     * 是否是以jar的形式存在
     * @return boolean
     */
    public boolean isIsInJar() {
        return isInJar;
    }

    /**
     * jar的名称是什么，如果不是以jar形式存在，则为空
     * @return String
     */
    public String getJarName() {
        return jarName;
    }

    /**
     * 取得class根路径，或jar文件所在路径
     * @return File
     */
    public File getLibPath() {
        return libPath;
    }

    public boolean isSupportDom4j() {
        return supportDom4j;
    }

    public boolean isSupportJdk15() {
        return supportJdk15;
    }

    public boolean isSupportImageProcessing() {
        return supportImageProcessing;
    }

    /**
     * 是否是以jar的形式存在
     */
    private boolean isInJar;
    /**
     * jar的名称是什么，如果不是以jar形式存在，则为空
     */
    private String jarName;
    /**
     * 取得class根路径，或jar文件所在路径
     */
    private File libPath;
    /**
     * 构造函数
     */
    private LibrayInfo() {
    	String CLZ_NAME=getClass().getName().replace('.', '/')+".class";
        java.net.URL url = getClass().getClassLoader().getResource(CLZ_NAME);

        String basePath = url.getFile().substring(0,
                                                  url.getFile().length() -
                                                  CLZ_NAME.length());
        if(basePath.indexOf("file:")>=0){
            basePath=basePath.substring(basePath.indexOf("file:")+5);
        }

        if (isInJar = "jar".equals(url.getProtocol())) {
            int jarPos=basePath.indexOf(".jar");

            int lastFileSep = basePath.substring(0, jarPos).
                              lastIndexOf("/");
            if(lastFileSep<0){
                lastFileSep = basePath.substring(0, jarPos).
                              lastIndexOf("\\");
            }
            jarName = basePath.substring(lastFileSep + 1, basePath.length() - 2);
            String libPathStr = basePath.substring(0, lastFileSep + 1); //去除前面的file:
            if(!libPathStr.startsWith(File.separator))libPathStr=File.separator+libPathStr;
           	LOG.info("==============================================" +
           			"\r\nurl.getProtocol() = "+ url.getProtocol()+
           			"\r\nurl.getFile() = "+ url.getFile()+
           			"\r\nbasePath = "+ basePath+
           			"\r\njarName = "+ jarName+
           			"\r\nlibPathStr = "+ libPathStr+
           			"\r\n==============================================");
            try {
                libPath = new File(java.net.URLDecoder.decode(libPathStr,
                        "UTF-8"));
            } catch (UnsupportedEncodingException ex) {}
        } else {
            String libPathStr = basePath;
            int jarPos=basePath.indexOf(".jar");
            if(jarPos>0){
                int lastFileSep = basePath.substring(0, jarPos).
                                  lastIndexOf("/");
                if(lastFileSep<0){
                    lastFileSep = basePath.substring(0, jarPos).
                                  lastIndexOf("\\");
                }
                libPathStr=basePath.substring(0,lastFileSep);
            }
            LOG.info("=============================================="+
           			"\r\nurl.getProtocol() = "+ url.getProtocol()+
           			"\r\nurl.getFile() = "+ url.getFile()+
           			"\r\nbasePath = "+ basePath+
           			"\r\nlibPathStr = "+ libPathStr+
           			"\r\n==============================================");
            try {
                libPath = new File(java.net.URLDecoder.decode(libPathStr,
                        "UTF-8"));
            } catch (UnsupportedEncodingException ex1) {}
        }

        try {
            Class.forName("java.util.Quene");
            supportJdk15 = true;
        } catch (ClassNotFoundException ex) {}
        try {
            Class.forName("org.dom4j.Document");
            supportDom4j = true;
        } catch (ClassNotFoundException ex) {}
        try {
            BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawLine(0, 0, 10, 10);
            g.dispose();// free resource

            supportImageProcessing = true;
        } catch (Throwable ex) {}

    }

    private boolean supportJdk15;
    private boolean supportDom4j;
    private boolean supportImageProcessing;
}

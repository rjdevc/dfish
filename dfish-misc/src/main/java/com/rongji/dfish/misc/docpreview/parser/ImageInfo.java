package com.rongji.dfish.misc.docpreview.parser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

/**
 * 图片信息，便于快速读取word里面内嵌的图片的基础信息，以便如何保存。
 * 这个ImageInfo 只适用于已知byte[]的 情况。
 */
public class ImageInfo {

    /**
     * 图片格式 PNG
     */
    public static final String PNG="png";
    /**
     * 图片格式JPG
     */
    public static final String JPG="jpg";
    /**
     * 图片格式GIF
     */
    public static final String GIF="gif";
    /**
     * 图片格式BMP
     */
    public static final String BMP="bmp";
    private int width;
    private int height;
    private String type;

    /**
     * 图片宽度
     * @return int
     */
    public int getWidth() {
        return width;
    }

    /**
     * 图片宽度
     * @param width int
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 图片高度
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * 图片高度
     * @param height int
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 图片类型
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * 图片类型
     * @param type String
     */
    public void setType(String type) {
        this.type = type;
    }

    private static boolean startsWith(byte[] sample, byte[] feature) {
        int len = Math.min(sample.length, feature.length);
        for (int i = 0; i < len; i++) {
            if (sample[i] != feature[i]) {
                return false;
            }
        }
        return true;
    }

    private static final byte[] JPEG_FEATURE = new byte[]{(byte) 0xFF, (byte) 0xD8};
    private static final byte[] PNG_FEATURE = new byte[]{(byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G'};
    private static final byte[] GIF_FEATURE = new byte[]{(byte) 'G', (byte) 'I', (byte) 'F'};
    private static final byte[] BMP_FEATURE = new byte[]{(byte) 'B', (byte) 'M'};

    /**
     * 从byte[] 中读取基础信息
     * @param src byte[]
     * @return ImageInfo
     */
    public static ImageInfo of(byte[] src){
        ImageInfo ii=new ImageInfo();
        if(startsWith(src,JPEG_FEATURE)){
            ii.setType(ImageInfo.JPG);
            readJpegInfo(src,ii);
        }else if(startsWith(src,PNG_FEATURE)){
            ii.setType(ImageInfo.PNG);
            readPngInfo(src,ii);
        }else if(startsWith(src,GIF_FEATURE)){
            ii.setType(ImageInfo.GIF);
            readGifInfo(src,ii);
        }else if(startsWith(src,BMP_FEATURE)){
            ii.setType(ImageInfo.BMP);
            readBmpInfo(src,ii);
        }
        return ii;
    }

    private static void readBmpInfo(byte[] src,ImageInfo ii) {
        ii.setHeight(readIntLh(src,22));
        if(ii.getHeight()<0){
            ii.setHeight(-ii.getHeight());
        }
        ii.setWidth(readIntLh(src,18));
    }
    private static void readGifInfo(byte[] src,ImageInfo ii) {
        ii.setHeight(readIntLh(src,8));
        ii.setWidth(readIntLh(src,6));
    }

    private static void readPngInfo(byte[] src, ImageInfo ii) {
        ii.setHeight(readIntHl4(src,20));
        ii.setWidth(readIntHl4(src,16));
    }
    private static int readIntHl4(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<24)|((src[pos+1]&0xFF)<<16)|((src[pos+2]&0xFF)<<8)|(src[pos+3]&0xFF);
    }
    private static int readIntLh(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8);
    }
    private static int readIntLh4(byte[] src, int pos) {
        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8)|((src[pos+2]&0xFF)<<16)|((src[pos+3]&0xFF)<<24);
    }
    private static int readIntHl(byte[] src, int pos) {
        return ((src[pos]&0xFF)<<8)|(src[pos+1]&0xFF);
    }

    private static void readJpegInfo(byte[] src, ImageInfo ii) {
        JpegChunk jb= JpegChunk.HEAD;
        while(jb!=null&&!jb.isEnd()){
            jb= JpegChunk.readNext(src,jb);
            if(jb.type==(byte)0xC0){
                ii.setHeight(readIntHl(src,jb.start+5));
                ii.setWidth(readIntHl(src,jb.start+7));
                break;
            }
        }
    }

    /**
     * JPEG的一个数据块(block)
     */
    private static class JpegChunk {
        byte[]src;
        int start,end;
        byte type;
        static final JpegChunk HEAD=new JpegChunk(null,0,2);
        public JpegChunk(byte[] src, int start, int end){
            this.src=src;
            this.start=start;
            this.end=end;
        }
        public boolean isEnd(){
            if(this==HEAD){
                return false;
            }
            if(end-start==2&src!=null){
                return src[end-2]==-1&& src[end-1]==(byte) 0xd9;
            }
            return false;
        }
        static JpegChunk readNext(byte[] src, JpegChunk pre){
            JpegChunk jb=  new JpegChunk(src,pre.end,0);
           jb.type=src[jb.start+1];
            if(jb.type==(byte) 0xd9){
                jb.end=jb.start+2;//结束节点
            }else{
               int length= readIntHl(src,jb.start+2);
               jb.end=jb.start+2+length;
            }
            return jb;
        }
    }

}

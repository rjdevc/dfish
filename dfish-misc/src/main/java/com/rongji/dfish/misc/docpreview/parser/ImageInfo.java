package com.rongji.dfish.misc.docpreview.parser;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class ImageInfo {


    public static final String PNG="png";
    public static final String JPG="jpg";
    public static final String GIF="gif";
    public static final String BMP="bmp";
    private int width;
    private int height;
    private String type;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

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
    private static int readIntHl4(byte[] src, int high) {
        return ((src[high]&0xFF)<<24)|((src[high+1]&0xFF)<<16)|((src[high+2]&0xFF)<<8)|(src[high+3]&0xFF);
    }
    private static int readIntLh(byte[] src, int high) {
        return (src[high]&0xFF)|((src[high+1]&0xFF)<<8);
    }
    private static int readIntLh4(byte[] src, int high) {
        return (src[high]&0xFF)|((src[high+1]&0xFF)<<8)|((src[high+2]&0xFF)<<16)|((src[high+3]&0xFF)<<24);
    }

    private static void readJpegInfo(byte[] src, ImageInfo ii) {
        JpegChunk jb= JpegChunk.HEAD;
        while(jb!=null&&!jb.isEnd()){
            jb= JpegChunk.readNext(src,jb);
            if(jb.type==(byte)0xC0){
                ii.setHeight(JpegChunk.readIntHl(src,jb.start+5));
                ii.setWidth(JpegChunk.readIntHl(src,jb.start+7));
                break;
            }
        }
    }

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
               int length= readIntHl(src,jb.start+2); ;
               jb.end=jb.start+2+length;
            }
            return jb;
        }

        private static int readIntHl(byte[] src, int high) {
            return ((src[high]&0xFF)<<8)|(src[high+1]&0xFF);
        }
    }

}
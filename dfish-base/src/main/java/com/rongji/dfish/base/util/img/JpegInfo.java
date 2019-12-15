package com.rongji.dfish.base.util.img;

import com.rongji.dfish.base.util.ByteArrayUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class JpegInfo extends ImageInfo {
    private byte[] thumbData;
    private int thumbOff;
    private int thumbLen;
    public ByteArrayInputStream getThumbnail(){
        if(thumbData==null){
            return null;
        }
        return new ByteArrayInputStream(thumbData,thumbOff,thumbLen);
    }

    public static JpegInfo readJpegInfo(byte[] src, int read, InputStream source) throws IOException {
        JpegInfo ji=new JpegInfo();
        ji.setType(TYPE_JPEG);
        int start=0;
        ExtendableBytes eb=new ExtendableBytes(src,read,source);
        while(true){
            JpegChunk jb= JpegChunk.readNextJpegChunk(eb,start);
            if(jb.isEnd()){
                break;
            }
            start=jb.end;
//            System.out.println("get chunk "+JpegChunk.getTypeName(jb.type)+" form "+jb.start+" to "+jb.end);
            if(jb.type==JpegChunk.SOF0){// 图片基本信息
                ji.setHeight(ByteArrayUtil.readShortBigEndian(eb.getBytes(),jb.start+5));
                ji.setWidth(ByteArrayUtil.readShortBigEndian(eb.getBytes(),jb.start+7));
            }else if(jb.type==JpegChunk.EXIF){
                //EXIF 帧 中有很大几率有缩略图
                // 这种做法有较大的几率出问题。
                int MAX_OFF=8;
                int endPos=ByteArrayUtil.indexOf(jb.src,jb.end-MAX_OFF,MAX_OFF,JPEG_END);
                if(endPos<0){
                    //在最后8位里面找图片结束符号,如果没找到则忽略
                    //有的时候结束符号会以 FF D9 00 00 来结束的而不是总是最后一个。
                    continue;
                }
                int boi= ByteArrayUtil.indexOf(jb.src,jb.start,jb.end-jb.start,JPEG_BEGIN);
                if(boi> 0){
                    ji.thumbData=jb.src;
                    ji.thumbOff=jb.start+boi;
                    ji.thumbLen=jb.end-MAX_OFF+JPEG_END.length+endPos-ji.thumbOff;
                }
            }else if(jb.type==JpegChunk.SOS){
                break;
            }
        }
        source.close();
        return ji;
    }

    public static class JpegChunk {
        static final byte MAGIC =(byte)0xFF;
        /**
         * Start Of Image
         */
        public static final byte SOI =(byte)0xD8;//文件头
        /**
         * End Of Image
         */
        public static final byte EOI =(byte)0xD9;//文件尾
        /**
         * Start of Scan
         */
        public static final byte SOS =(byte)0xDA;// 扫描行开始
        /**
         * Define Quantization Table
         */
        public static final byte DQT =(byte)0xDB;// 定义量化表
        /**
         * Define Restart Interval
         */
        public static final byte DRI =(byte)0xDD;// 定义重新开始间隔//99
        /**
         * Start of Frame
         */
        public static final byte SOF0 =(byte)0xC0;//帧开始（标准 JPEG）
        public static final byte SOF1 =(byte)0xC1;//帧开始（标准 JPEG）
        /**
         * Difine Huffman Table
         */
        public static final byte DHT =(byte)0xC4;//定义 Huffman 表（霍夫曼表）

        public static final byte APP0 =(byte)0xE0;// 定义交换格式和图像识别信息
        // E1-EF 相当于 APP 1-15 各种扩展应用 APP1 通常为EXIF 信息
        public static final byte EXIF =(byte)0xE1;// EXIF 信息 可能包含有缩略图
        static final byte APP1 =(byte)0xE1;// EXIF 信息 可能包含有缩略图 同EXIF
        static final byte APP2 =(byte)0xE2;// 各种参数
        static final byte APP13 =(byte)0xED;// 各种参数 //也可能包含有缩略图
        static final byte APP14 =(byte)0xEE;// 版权
        static final byte APP15 =(byte)0xEF;// APP结束
        /**
         * Comment
         */
        public static final byte COM =(byte)0xFE;// 注释
        public static String getTypeName(byte type) {
            switch (type){
                case SOI: return "SOI";
                case EOI: return "EOI";
                case SOS: return "SOS";
                case DQT: return "DQT";
                case DRI: return "DRI";
                case SOF0: return "SOF0";
                case SOF1: return "SOF1";
                case DHT: return "DHT";
                case EXIF: return "EXIF";
                case COM: return "COM";
                default:
                    if(type>=APP0&type<=APP15){
                        return "APP"+(type-APP0);
                    }
                    return Integer.toHexString(type&0xFF).toUpperCase();
            }
        }
        // 原文链接：https://blog.csdn.net/yun_hen/article/details/78135122
        public byte[]src;
        public int start,end;
        public byte type;
        public JpegChunk(byte[] src, int start, int end){
            this.src=src;
            this.start=start;
            this.end=end;
        }
        public boolean isEnd(){
            if(end-start==2&src!=null){
                return src[end-2]==-1&& src[end-1]== SOF0;
            }
            return false;
        }

        public static JpegChunk readNextJpegChunk(ExtendableBytes eb, int start)throws IOException {
            eb.ensureLen(start+2);//FF type
            JpegChunk jb=  new JpegChunk(eb.getBytes(),start,0);
            //首位固定是FF
            jb.type=eb.getBytes()[jb.start+1];
            while(jb.type== JpegChunk.MAGIC){ //跳过连续的0xFF
                eb.ensureLen(jb.start+2);
                jb.type=eb.getBytes()[++jb.start+1];
            }
            switch (jb.type){
                case JpegChunk.EOI:
                case JpegChunk.SOI:
                    jb.end=jb.start+2;//结束节点
                    return jb;
                default:
                    int length= ByteArrayUtil.readShortBigEndian(eb.getBytes(),jb.start+2);
                    jb.end=jb.start+2+length;
                    eb.ensureLen(jb.end);
                    jb.src=eb.getBytes();
                    return jb;
            }
        }
    }
    protected static class ExtendableBytes {
        private byte[] bytes;
        private int len;
        private InputStream source;
        public byte[] getBytes(){
            return bytes;
        }
        ExtendableBytes(byte[] bytes, int len, InputStream source){
            this.bytes = bytes;
            this.len =len;
            this.source=source;
        }
        public void ensureLen(int len)throws IOException{
            if(this.len >=len){
                return;
            }
            byte[]buff=new byte[8192];
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int working= this.len;
            while(working<len){
                int r=source.read(buff);
                if(r==-1){
                    source.close();
                    throw new RuntimeException("can not len enough bytes");
                }
                baos.write(buff,0,r);
                working+=r;
            }
            byte[] newBytes= baos.toByteArray();
            byte[] newSrc=new byte[this.len +newBytes.length];
            if(this.len >0) {
                System.arraycopy(bytes, 0, newSrc, 0, this.len);
            }
            System.arraycopy(newBytes,0,newSrc, this.len,newBytes.length);
            this.bytes =newSrc;
            this.len =newSrc.length;
        }
    }

    private static final byte[] JPEG_BEGIN=new byte[]{-1,JpegChunk.SOI,-1};
    private static final byte[] JPEG_END=new byte[]{-1,JpegChunk.EOI};
}

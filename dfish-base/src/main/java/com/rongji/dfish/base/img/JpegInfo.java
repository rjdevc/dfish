package com.rongji.dfish.base.img;

import com.rongji.dfish.base.util.ByteArrayUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jpeg图片的基本信息处理
 */
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

    /**
     * JPEG 数据块
     * 根据JPEG规范 JPEG文件是可以分为几个有规范的数据块。
     * 每块至少两个字节，都是由0xFF开头，第二个字节是它的类型。
     * 除了开头和结尾数据块，仅由两个字节构成外，其他块后续都有内容。具体格式根据类型有所不同
     *
     */
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

        /**
         * 把JPEG 数据块(chunk)类型转化成人便于阅读的符号。
         * @param type byte
         * @return String
         */
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
        /**
         * 内容的引用。注意这里的src并不是全部属于该chunk
         */
        public byte[]src;
        /**
         * 标识 src 数据哪些方位属于该数据块
         */
        public int start,end;
        /**
         * 数据块类型。这个值一定等于src[start+1]
         */
        public byte type;

        /**
         * 构造函数
         * @param src
         * @param start
         * @param end
         */
        public JpegChunk(byte[] src, int start, int end){
            this.src=src;
            this.start=start;
            this.end=end;
        }

        /**
         * 是否是结束节点
         * @return boolean
         */
        public boolean isEnd(){
            if(end-start==2&src!=null){
                return src[end-2]==-1&& src[end-1]== SOF0;
            }
            return false;
        }

        /**
         * 从JPEG数据流中读取下一个内容块
         * @param eb ExtendableBytes
         * @param start 开始
         * @return JpegChunk
         * @throws IOException
         */
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

    /**
     * 可扩展的Byte数组
     * 由于使用流模式读取JPEG内容。一旦读取到足够信息，就会放弃读取剩余内容。
     * 所以，数据块的大小，会随着需要不断的扩展。直到足够。
     * 通常 数码相机的照片，它的大小，分辨率(DPI)等参数并不在第一个8K内。毕竟缩略图，拍照参数等。会占用一定空间。
     * 所以，在流模式处理过程中，会将ExtendableBytes 的句柄给高一层的程序。而具体bytes[]将会变化。
     * 从高一层的应用程序看，就是这个byte数组扩展了。
     */
    protected static class ExtendableBytes {
        private byte[] bytes;
        private int len;
        private InputStream source;

        /**
         * 取得这个byte的原始值
         * @return byte[]
         */
        public byte[] getBytes(){
            return bytes;
        }

        /**
         * 构造函数
         * @param bytes 已知的byte数组 和流模式一样，它有很大可能是buff的倍数长度。但其有效长度只有在len中。后续空间是没有初始化过的无效内容。
         * @param len 已知的有效长度
         * @param source 输入流的句柄。
         */
        ExtendableBytes(byte[] bytes, int len, InputStream source){
            this.bytes = bytes;
            this.len =len;
            this.source=source;
        }

        /**
         * 保证从流中读取不少于 len长度的内容。很多情况下，流并不是按当个字节读的。
         * 所以ensureLen的时候，有很大可能，不仅仅读取到len 而是buff的倍数。
         * @param len int
         * @throws IOException
         */
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

//package com.rongji.dfish.misc.util.img;
//
//import com.rongji.dfish.base.util.LogUtil;
//
//import java.io.*;
//
///**
// * 读取文件的信息。
// */
//public class ImgInfo {
//    public static final String PNG="png";
//    public static final String JPG="jpg";
//    public static final String GIF="gif";
//    public static final String BMP="bmp";
//    private int width;
//    private int height;
//    private String type;
//    private int colorbits;
//
//    public int getWidth() {
//        return width;
//    }
//
//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public int getColorbits() {
//        return colorbits;
//    }
//
//    public void setColorbits(int colorbits) {
//        this.colorbits = colorbits;
//    }
//
//
//
//    private static boolean startsWith(byte[] sample, byte[] feature) {
//        int len = Math.min(sample.length, feature.length);
//        for (int i = 0; i < len; i++) {
//            if (sample[i] != feature[i]) {
//                return false;
//            }
//        }
//        return true;
//    }
//    protected static boolean startsWith(byte[] sample,int offset, byte[] feature) {
//        int len = Math.min(sample.length-offset, feature.length);
//        for (int i = 0; i < len; i++) {
//            if (sample[i+offset] != feature[i]) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private static final byte[] JPEG_FEATURE = new byte[]{(byte) 0xFF, (byte) 0xD8};
//    private static final byte[] PNG_FEATURE = new byte[]{(byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G'};
//    private static final byte[] GIF_FEATURE = new byte[]{(byte) 'G', (byte) 'I', (byte) 'F'};
//    private static final byte[] BMP_FEATURE = new byte[]{(byte) 'B', (byte) 'M'};
//
//    public static ImgInfo of(byte[] src){
//        try {
//            return of(new ByteArrayInputStream(src));
//        } catch (IOException e) {
//            LogUtil.error("unknown file",e);
//        }
//        return null;
//    }
//    public static final ImgInfo UNKNOWN=new ImgInfo();
//    static{
//        UNKNOWN.setType("UNKNOWN");
//        UNKNOWN.setWidth(0);
//        UNKNOWN.setHeight(0);
//        UNKNOWN.setColorbits(0);
//    }
//    public static ImgInfo of(InputStream is)throws IOException{
//        return of(is,false);
//    }
//    public static ImgInfo of(InputStream is,boolean quick) throws IOException {
//        ImgInfo ii=new ImgInfo();
//        //尝试读取8K字节。 判断是什么数据类型，
//        //如果是PNG/BMP/GIF 则直接读取信息。
//        //如果是JPG
//        // 如果是quick 模式 只返回 ImgInfo 则尝试从8K中读取需要的信息，如果读取不到，则往后读取到信息为止。
//        // 如果不是quick模式，需要返回 JpegInfo 里面应该包含ISO等扩展信息。和缩略图的信息，做图片压缩的时候可能用到上。
//        // 这时，应该先读取完整的图片字节。并取得除了图片主体信息外所有EXIF信息和缩略图的EXIF信息。
//        byte[] buff=new byte[8192];
//        try {
//            int read = is.read(buff);
//            if (startsWith(buff, JPEG_FEATURE)) {
//                if (quick) {
//                    ii=new JpegInfo();
//                    JpegInfo.readJpegInfoQuick(buff, is, (JpegInfo)ii);
//                } else {
//                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                    baos.write(buff,0,read);
//                    while((read=is.read(buff))>0){
//                        baos.write(buff,0,read);
//                    }
//                    ii=new JpegInfo();
//                    JpegInfo.readJpegInfoFull(baos.toByteArray(), (JpegInfo)ii);
//                }
//            } else if (startsWith(buff, PNG_FEATURE)) {
//                if (read < 24) {
//                    return UNKNOWN;
//                }
//                readPngInfo(buff, ii);
//            } else if (startsWith(buff, GIF_FEATURE)) {
//                if (read < 10) {
//                    return UNKNOWN;
//                }
//                readGifInfo(buff, ii);
//            } else if (startsWith(buff, BMP_FEATURE)) {
//                if (read < 30) {
//                    return UNKNOWN;
//                }
//                readBmpInfo(buff, ii);
//            }
//        }catch(IOException ex){
//            ex.printStackTrace();
//        }finally {
//            is.close();
//        }
//        return ii;
//    }
//
//
//    private static void readBmpInfo(byte[] src,ImgInfo ii) {
//        ii.setType(BMP);
//        ii.setColorbits(readShortLittleEndian(src,28));
//        ii.setWidth(readShortLittleEndian(src,18));
//        int height= readShortLittleEndian(src,22);
//        if(height<0){
//            height = - height;
//        }
//        ii.setHeight(height);
//    }
//    private static void readGifInfo(byte[] src,ImgInfo ii) {
//        ii.setType(GIF);
//        //TODO unknown colorbits
//        // 只找到 位深为8 的样本。而且不知道如何读取。
//        ii.setColorbits(8);
//        ii.setHeight(readShortLittleEndian(src,8));
//        ii.setWidth(readShortLittleEndian(src,6));
//    }
//
//    private static void readPngInfo(byte[] src, ImgInfo ii) {
//        ii.setType(PNG);
//        //TODO unknown colorbits
//        // 如果要获取PNG的位深，需要获取PNG图片的chunk 暂时不做支持。
//        //PNG 通常有PNG8 PNG24 PNG32 其中 PNG24并不支持透明。
//        //PNG8 更接近于GIF
//        ii.setWidth(readIntBigEndian(src,16));
//        ii.setHeight(readIntBigEndian(src,20));
//    }
//
//
//
//    protected static int readShortBigEndian(byte[] src, int pos) {
//        return ((src[pos]&0xFF)<<8)|(src[pos+1]&0xFF);
//    }
//    protected static int readIntBigEndian(byte[] src, int pos) {
//        return ((src[pos]&0xFF)<<24)|((src[pos+1]&0xFF)<<16)|((src[pos+2]&0xFF)<<8)|(src[pos+3]&0xFF);
//    }
//    protected static int readShortLittleEndian(byte[] src, int pos) {
//        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8);
//    }
//
//    protected static int readIntLittleEndian(byte[] src, int pos) {
//        return (src[pos]&0xFF)|((src[pos+1]&0xFF)<<8)|((src[pos+2]&0xFF)<<16)|((src[pos+3]&0xFF)<<24);
//    }
//    protected static int readByte(byte[] src, int pos) {
//        return src[pos]&0xFF;
//    }
//
//
//
//    public static void main(String[] args){
//        try {
//            ImgInfo.of(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000044.jpg"),false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
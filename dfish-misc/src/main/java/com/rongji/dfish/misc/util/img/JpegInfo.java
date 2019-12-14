//package com.rongji.dfish.misc.util.img;
//
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//
//public class JpegInfo extends ImgInfo {
//
//    protected static void readJpegInfoFull(byte[] src, JpegInfo ii) {
//        ii.setType(JPG);
////        JpegChunk jb= JpegChunk.HEAD;
//        int start=0;
//        while(true){
//            JpegChunk jb= readNext(src,start);
//            if(jb.isEnd()){
//                break;
//            }
//            start=jb.end;
//            if(jb.type==JpegChunk.SOF0){// 图片基本信息
//                ii.setHeight(readShortBigEndian(src,jb.start+5));
//                ii.setWidth(readShortBigEndian(src,jb.start+7));
//            }else  if(jb.type==JpegChunk.EXIF){
//            }else  if(jb.type==JpegChunk.SOS){
//                break;//后面的内容不可识别，跳出防止报错。
//            }
//            System.out.println(jb);
//        }
//    }
//    protected static void readJpegInfoQuick(byte[] src, InputStream is, JpegInfo ii) {
//        ii.setType(JPG);
//        int start=0;
//        while(true){
//            JpegChunk jb= readNext(src,start);
//            if(jb.isEnd()){
//                break;
//            }
//            start=jb.end;
//            if(jb.type==JpegChunk.SOF0){// 图片基本信息
//                ii.setHeight(readShortBigEndian(src,jb.start+5));
//                ii.setWidth(readShortBigEndian(src,jb.start+7));
//                break;//简单的时候就够了。
//            }
//        }
//    }
//    static class JpegChunks{
//        byte[] src;
//        InputStream is;
//        int width;
//        int height;
//
//    }
//    static JpegChunk readNext(byte[] src, int start){
//        JpegChunk jb=  new JpegChunk(src,start,0);
//        //首位固定是FF
//        jb.type=src[jb.start+1];
//        while(jb.type== JpegChunk.MAGIC){ //跳过连续的0xFF
//            jb.type=src[++jb.start+1];
//        }
//        int length;
//        switch (jb.type){
//            case JpegChunk.EOI:
//            case JpegChunk.SOI:
//                jb.end=jb.start+2;//结束节点
//                return jb;
//            case JpegChunk.APP0:
//                length= readShortBigEndian(src,jb.start+2); ;
//                jb.end=jb.start+2+length;
//                return App0Chunk.of(jb);
//            case JpegChunk.EXIF:
//                length= readShortBigEndian(src,jb.start+2); ;
//                jb.end=jb.start+2+length;
//                if(startsWith(src,jb.start+4,"http".getBytes())){
//                    return StringChunk.of(jb);
//                }else if(startsWith(src,jb.start+4,"Exif".getBytes())){
//                    return ExifChunk.of(jb);
//                }
//                return jb;
//            default:
//                length= readShortBigEndian(src,jb.start+2); ;
//                jb.end=jb.start+2+length;
//                return jb;
//        }
////        if(jb.type== JpegChunk.EOI||jb.type== JpegChunk.SOI){
////            jb.end=jb.start+2;//结束节点
////        }else if(jb.type== JpegChunk.SOS){
////            int length= readShortBigEndian(src,jb.start+2); ;
////            jb.end=jb.start+2+length;//FIXME 增加数据块长度
////        }else{//||jb.type== JpegChunk.DQT||jb.type== JpegChunk.DRI
////            int length= readShortBigEndian(src,jb.start+2); ;
////            jb.end=jb.start+2+length;
////        }
////        return jb;
//    }
//    /**
//     * JPEG的一个数据块(block)
//     */
//    public static class JpegChunk {
//        static final byte MAGIC =(byte)0xFF;
//        public static final byte SOI =(byte)0xD8;//文件头
//        public static final byte EOI =(byte)0xD9;//文件尾
//        public static final byte SOS =(byte)0xDA;// 扫描行开始
//        public static final byte DQT =(byte)0xDB;// 定义量化表
//        public static final byte DRI =(byte)0xDD;// 定义重新开始间隔//99
//
//        public static final byte SOF0 =(byte)0xC0;//帧开始（标准 JPEG）
//        public static final byte SOF1 =(byte)0xC1;//帧开始（标准 JPEG）
//        public static final byte DHT =(byte)0xC4;//定义 Huffman 表（霍夫曼表）
//
//        public static final byte APP0 =(byte)0xE0;// 定义交换格式和图像识别信息
//        // E1-EF 相当于 APP 1-15 各种扩展应用 APP1 通常为EXIF 信息
//        public static final byte EXIF =(byte)0xE1;// EXIF 信息 可能包含有缩略图
//        static final byte APP1 =(byte)0xE1;// EXIF 信息 可能包含有缩略图 同EXIF
//        static final byte APP2 =(byte)0xE2;// 各种参数
//        static final byte APP13 =(byte)0xED;// 各种参数 //也可能包含有缩略图
//        static final byte APP14 =(byte)0xEE;// 版权
//        static final byte APP15 =(byte)0xEE;// APP结束
//        public static final byte COM =(byte)0xFE;// 注释
//        private String getName(byte type) {
//            switch (type){
//                case SOI: return "SOI";
//                case EOI: return "EOI";
//                case SOS: return "SOS";
//                case DQT: return "DQT";
//                case DRI: return "DRI";
//                case SOF0: return "SOF0";
//                case SOF1: return "SOF1";
//                case DHT: return "DHT";
//                case EXIF: return "EXIF";
//                case COM: return "COM";
//                default:
//                    if(type>=APP0&type<=APP15){
//                        return "APP"+(type-APP0);
//                    }
//                    return Integer.toHexString(type&0xFF).toUpperCase();
//            }
//        }
//        // 原文链接：https://blog.csdn.net/yun_hen/article/details/78135122
//        byte[]src;
//        int start,end;
//        byte type;
//        private JpegChunk(byte[] src, int start, int end){
//            this.src=src;
//            this.start=start;
//            this.end=end;
//        }
//        private boolean isEnd(){
//            if(this==HEAD){
//                return false;
//            }
//            if(end-start==2&src!=null){
//                return src[end-2]==-1&& src[end-1]== SOF0;
//            }
//            return false;
//        }
//        static final JpegChunk HEAD=new JpegChunk(null,0,0);
//
//        @Override
//        public String toString(){
//            StringBuilder sb=new StringBuilder();
//            headToString(sb);
//            if(src[start]==MAGIC) {
//                for (int i = start; i < end; i += 16) {
//                    sb.append("\r\n");
//                    int lineLen = Math.min(16, end - i);
//                    for (int k = 0; k < lineLen; k++) {
//                        append(sb, src[i + k]);
//                        sb.append(' ');
//                    }
//                    for (int k = lineLen; k < 16; k++) {
//                        sb.append("   ");
//                    }
//                    sb.append("   ");
//                    for (int k = 0; k < lineLen; k++) {
//                        byte b = src[i + k];
//                        if (b >= 32 && b < 128) {
//                            sb.append((char) b);
//                        } else {
//                            sb.append('.');
//                        }
//                    }
//                }
//            }
//            return sb.toString();
//        }
//        protected void headToString(StringBuilder sb){
//            if(src[start]==MAGIC){
//                sb.append("CHUNK ");
//                sb.append(getName(type));
//                sb.append('(');
//                append(sb,type);
//                sb.append(") ");
//            }else{
//                sb.append("ERROR_CHUNK(");
//                append(sb,type);
//                sb.append(") ");
//            }
//
//            sb.append(" bytes ");
//            sb.append(end-start);
//            sb.append('(');
//            sb.append(Integer.toHexString(start).toUpperCase());
//            sb.append(" - ");
//            sb.append(Integer.toHexString(end).toUpperCase());
//            sb.append(")");
//        }
//
//        static void append(StringBuilder sb,byte b){
//            int i=b&0xFF;
//            sb.append(HEX_CHARS[i>>4]);
//            sb.append(HEX_CHARS[i&0xF]);
//        }
//        static char[] HEX_CHARS={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
//    }
//    public static class App0Chunk extends JpegChunk{
//        private App0Chunk(byte[] src, int start, int end) {
//            super(src, start, end);
//        }
//
//        public int getVersionHigh() {
//            return versionHigh;
//        }
//
//        public int getVersionLow() {
//            return versionLow;
//        }
//
//        public int getUnit() {
//            return unit;
//        }
//
//        public int getXdpi() {
//            return xdpi;
//        }
//
//        public int getYdpi() {
//            return ydpi;
//        }
//
//        //        ① 数据长度  2字节  ①~⑨9个字段的总长度
////  ②标识符  5字节   固定值0x4A46494600，即字符串“JFIF0”
////                  ③ 版本号 2字节  一般是0x0102，表示JFIF的版本号1.2
////                  ④ X和Y的密度单位 1字节 只有三个值可选 ,0：无单位；1：点数/英寸；2：点数/厘米
////  ⑤ X方向像素密度     2字节 取值范围未知
////  ⑥ Y方向像素密度       2字节 取值范围未知  
////                  ⑦ 缩略图水平像素数目  1字节  取值范围未知
////  ⑧ 缩略图垂直像素数目  1字节  取值范围未知
////  ⑨ 缩略图RGB位图       长度可能是3的倍数 缩略图RGB位图数据
////————————————————
////        原文链接：https://blog.csdn.net/liushuang95/article/details/72799730
//        private int versionHigh;//byte
//        private int versionLow;//byte
//        private int unit;//0：无单位；1：点数/英寸；2：点数/厘米 //byte
//        private int xdpi;
//        private int ydpi;
//        private int thumbnailWidth;
//        private int thumbnailHeight;
//        private byte[] thumbnailData;
//        protected static App0Chunk of(JpegChunk chunk){
//            App0Chunk app0=new App0Chunk(chunk.src,chunk.start,chunk.end);
//            app0.type=chunk.type;
//            app0.versionHigh=readByte(chunk.src,chunk.start+9);
//            app0.versionLow=readByte(chunk.src,chunk.start+10);
//            app0.unit=readByte(chunk.src,chunk.start+11);
//            app0.xdpi=readShortBigEndian(chunk.src,chunk.start+12);
//            app0.ydpi=readShortBigEndian(chunk.src,chunk.start+14);
//            // 如果长度够的话，则读取缩略图的宽度，长度，以及数据//但一般这些信息都在exif里面这里通常都是0
//            return app0;
//        }
//        @Override
//        public String toString(){
//            StringBuilder sb=new StringBuilder();
//            headToString(sb);
//            sb.append("\r\nversion=");
//            sb.append(versionHigh);
//            sb.append('.');
//            sb.append(versionLow);
//            sb.append("\r\nunit=");
//            switch (unit){
//                case 1:sb.append("inch(1)");break;
//                case 2:sb.append("cm(2)");break;
//                default:sb.append("pixel(0)");break;
//            }
//            sb.append("\r\nxdpi=");sb.append(xdpi);
//            sb.append("\r\nydpi=");sb.append(ydpi);
//            return sb.toString();
//        }
//    }
//    public static class StringChunk extends JpegChunk{
//        private StringChunk(byte[] src, int start, int end) {
//            super(src, start, end);
//        }
//        String content;
//        public String getContent(){
//            return content;
//        }
//        protected static StringChunk of(JpegChunk chunk) {
//            StringChunk strCh = new StringChunk(chunk.src, chunk.start, chunk.end);
//            strCh.type=chunk.type;
//            try {
//                strCh.content=new String(chunk.src,chunk.start+4, chunk.end-chunk.start-4,"UTF8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            return strCh;
//        }
//        @Override
//        public String toString(){
//            StringBuilder sb=new StringBuilder();
//            headToString(sb);
//            sb.append("\r\n");
//            sb.append(content);
//            return sb.toString();
//        }
//    }
//    public static class ExifChunk extends JpegChunk{
//        private ExifChunk(byte[] src, int start, int end) {
//            super(src, start, end);
//        }
//
//        protected static ExifChunk of(JpegChunk chunk) {
//            ExifChunk strCh = new ExifChunk(chunk.src, chunk.start, chunk.end);
//            strCh.type=chunk.type;
//            return strCh;
//        }
//        @Override
//        public String toString(){
//            return super.toString();
//        }
//    }
//
//}

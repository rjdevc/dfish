package com.rongji.dfish.misc.util;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ImageUtilTest2 {

    public static void main(String[]args) throws IOException {
       print(ImageIO.getReaderFileSuffixes());
       print(ImageIO.getWriterFileSuffixes());

       String folder="C:\\Users\\LinLW\\Pictures\\Saved Pictures\\";
        testFile(folder+"6a20c6f2b2119313215e928762380cd791238d4f.jpg");
        testFile(folder+"500个BUG.gif");
        testFile(folder+"37_170405181132_1.png");
        testFile(folder+"000000000432.BMP");
        testFile(folder+"640.webp");

    }
    static void testFile(String fileName)throws IOException{
        ImageTypeDeligate itd=new ImageTypeDeligate(new FileInputStream(fileName));
        ImageIO.read(itd);
        System.out.println(itd.getImageType());
        //这里爱做什么做什么。
        itd.close();
    }

    static void print(String[] arr){
        System.out.println(Arrays.asList(arr));
    }
    static void print(byte[] arr){
        StringBuilder sb=new StringBuilder();
        sb.append('[');
        for(byte b:arr){
            sb.append(b);
            sb.append(',');
            sb.append(' ');
        }
        sb.setCharAt(sb.length()-2,']');
        System.out.println(sb);
    }


    static class ImageTypeDeligate extends SampleDeligate{
        public static final int TYPE_UNKOWN=0;
        public static final int TYPE_BMP=1;
        public static final int TYPE_GIF=2;
        public static final int TYPE_JPEG=3;
        public static final int TYPE_PNG=4;
        public ImageTypeDeligate(InputStream raw){
            super(raw,4);
        }
        public int getImageType(){
            byte[] sample=getSample();
            if(match(sample,JPEG_FEATURE)){
                return TYPE_JPEG;
            }
            if(match(sample,PNG_FEATURE)){
                return TYPE_PNG;
            }
            if(match(sample,GIF_FEATURE)){
                return TYPE_GIF;
            }
            if(match(sample,BMP_FEATURE)){
                return TYPE_BMP;
            }
            return TYPE_UNKOWN;
        }
        private boolean match(byte[] sample,byte[]feature){
            int len=Math.min(sample.length,feature.length);
            for(int i=0;i<len;i++){
                if(sample[i]!=feature[i]){
                    return false;
                }
            }
            return true;
        }
        private static final byte[] JPEG_FEATURE=new byte[]{(byte)0xFF,(byte)0xD8,(byte)0xFF};
        private static final byte[] PNG_FEATURE=new byte[]{(byte)0x89,(byte)'P',(byte)'N',(byte)'G'};
        private static final byte[] GIF_FEATURE=new byte[]{(byte)'G',(byte)'I',(byte)'F'};
        private static final byte[] BMP_FEATURE=new byte[]{(byte)'B',(byte)'M'};

    }

    /**
     * 代理一个inputStream当他顺序读取的时候，将会留下最前方的sampleLength个byte作为标本。
     */
    static class SampleDeligate extends InputStream{
        private  InputStream raw;
        private byte[]sample;
        private int index;
        public SampleDeligate(InputStream raw, int sampleLength){
            this.raw=raw;
            sample=new byte[sampleLength];
            index=0;
        }
        public SampleDeligate(InputStream raw){
            this(raw,64);
        }
        @Override
        public int read() throws IOException {
            int readed= raw.read();
            if(index<sample.length){
                if(readed>=0&&readed<256){
                    sample[index++]=(byte)readed;
                }
            }
            return readed;
        }

        @Override
        public int read(byte[] b) throws IOException {
            return read(b,0,b.length);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int readed = raw.read(b, off, len);
            if(index<sample.length){
               int toSample=Math.min(sample.length-index,len);
               System.arraycopy(b,off,sample,index,toSample);
               index+=toSample;
            }
            return readed;
        }

        @Override
        public long skip(long n) throws IOException {
            if(index<sample.length){
                int toSample=(int)Math.min(sample.length-index,n);
                byte[]temp=new byte[toSample];
                read(temp);
                System.arraycopy(temp,0,sample,index,toSample);
                index+=toSample;
                return toSample+raw.skip(n-toSample);
            }
            return raw.skip(n);
        }

        @Override
        public int available() throws IOException {
            return raw.available();
        }

        @Override
        public void close() throws IOException {
            raw.close();
        }

        @Override
        public void mark(int readlimit) {
            raw.mark(readlimit);
        }

        @Override
        public void reset() throws IOException {
            raw.reset();
        }

        @Override
        public boolean markSupported() {
            return raw.markSupported();
        }
        public byte[] getSample(){
            return sample;
        }
    }
}

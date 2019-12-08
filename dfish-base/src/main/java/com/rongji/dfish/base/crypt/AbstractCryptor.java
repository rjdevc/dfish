package com.rongji.dfish.base.crypt;



import java.io.*;

public abstract class AbstractCryptor {
    public static class AbstractCryptBuilder<T extends AbstractCryptBuilder> {
        public static final int PRESENT_RAW = 0;
        public static final int PRESENT_HEX = 1;
        public static final int PRESENT_BASE64 = 2;
        public static final int PRESENT_BASE32 = 3;
        public static final int PRESENT_BASE64_RFC4648_URLSAFE = 4;
        protected String algorithm;
        protected String encoding;
        protected int present;
        protected boolean gzip=false;
        public String getAlgorithm() {
            return algorithm;
        }
        public String getEncoding() {
            return encoding;
        }
        public int getPresent() {
            return present;
        }
        public T encoding(String encoding){
            this.encoding=encoding;
            return (T)this;
        }
        public T present(int present){
            this.present=present;
            return (T)this;
        }
    }

    public static class Base32InputStream extends PackageInputStream{
        protected Base32InputStream(InputStream in) {
            super(in);
        }//FIXME
    }
    public static class Base32OutputStream extends PackageOutputStream{
        protected Base32OutputStream(OutputStream out) {
            super(out);
            chunk=new byte[5];
        }
        private static final byte[] ALPHABET = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
                'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'
        };
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] res=new byte[(chunkSize+len)/5*8];

            out.write(res);
        }
    }
    static abstract class PackageOutputStream extends FilterOutputStream{
        protected byte[] chunk;
        protected int chunkSize;
        private final byte[] singleByte = new byte[1];
        public PackageOutputStream(OutputStream out) {
            super(out);
            chunkSize=0;
        }
        @Override
        public void write(int b) throws IOException {
            singleByte[0]=(byte)b;
            this.write(singleByte);
        }
    }
    static abstract class PackageInputStream extends FilterInputStream{
        protected byte[] chunk;
        protected int chunkSize;
        public PackageInputStream(InputStream in) {
            super(in);
            chunkSize=0;
        }
        @Override
        public int read()throws IOException{
            byte[] b=new byte[1];
            int read=read(b,0,1);
            if(read<=0){//等0时是否要阻断
                return -1;
            }
            return 0xff&b[0];
        }
    }

    public static class HexInputStream extends PackageInputStream{
        private static final byte[] HEX_DE = { // 用于加速解密的cache
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 0
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 16
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 32
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, // 48
                0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, //64
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 80
                0, 10, 11, 12, 13, 14, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 96

        public HexInputStream(InputStream in) {
            super(in);
            chunk=new byte[2];
        }

        @Override
        public int read(byte[] b,int off,int len)throws IOException{
            //可尝试双倍读取in 的内容，做解压缩
            byte[] buff=new byte[2*len];
            int rread=in.read(buff);
            if(rread<0){
                return rread;
            }
            // 剩余的字符应该加入到洗一次read中去
            int read=0;
            for(int i=0;i<rread;i++){
                byte by=buff[i];
                if(by<'0'){ //空格 \r \n \t
                    continue;
                }
                if(chunkSize++==0){
                    chunk[0]= HEX_DE[by];
                }else{
                    byte v=(byte) (chunk[0] << 4 | HEX_DE[by]);
                    b[off+read++]=v;
                    chunkSize=0;
                }
            }
            return read;
        }
    }
    public static class HexOutputStream extends PackageOutputStream{
        public HexOutputStream(OutputStream out){
            super(out);
        }

        private static final byte[] HEX_EN_BYTE = { // 用于加速加密的cache
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
                'F'};

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] hex=new byte[len*2];
            for(int i=0;i<len;i++) {
                int inte = b[i+off] & 0xff;
                hex[i*2]= HEX_EN_BYTE[inte >> 4];
                hex[i*2+1]= HEX_EN_BYTE[inte & 0x0f];
            }
            out.write(hex);
        }
    }

    /**
     * 该方法不计入encoding.
     * 1 先过 zip 的流
     * 2 转接 加密流
     * 3 转接 present的流
     * @param is
     * @param os
     */

    protected void encode(InputStream is, OutputStream os){
        OutputStream pos;
        switch (builder.present){
            case AbstractCryptBuilder.PRESENT_HEX:
                pos=new HexOutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pos= os;//new Base32OutputStream(os);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
                pos=os;//new Base64OutputStream(os,false);
                break;
            case AbstractCryptBuilder.PRESENT_BASE64_RFC4648_URLSAFE:
                pos=os;//new Base64OutputStream(os,true);
                break;
            default:
                pos=os;
        }
//        InputStream zis;
//        if(builder.gzip){
//            zos=new ZipOutputStream(is);
//        }else{
//            zos=os;
//        }
        doEncrypt(is,pos);
//        ZipOutputStream(new CipherOutputStream(presentOS ,cipher));

    }

    protected abstract void doEncrypt(InputStream is, OutputStream os);
    protected abstract void doDecrypt(InputStream is, OutputStream os);

    protected void decode(InputStream is, OutputStream os){
        InputStream pis;
        switch (builder.present){
            case AbstractCryptBuilder.PRESENT_HEX:
                pis=new HexInputStream(is);
                break;
            case AbstractCryptBuilder.PRESENT_BASE32:
                pis=is;
                break;
            case AbstractCryptBuilder.PRESENT_BASE64:
                pis=is;
                break;
            case AbstractCryptBuilder.PRESENT_BASE64_RFC4648_URLSAFE:
                pis=is;
                break;
            default:
                pis=is;
        }
        doDecrypt(pis,os);
    }

    protected String encode(String src){
        ByteArrayInputStream bais= null;
        try {
            bais = new ByteArrayInputStream(src.getBytes(builder.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.encode(bais,baos);
        return new String(baos.toByteArray());
    }
    protected String decode(String src){
        ByteArrayInputStream bais=  new ByteArrayInputStream(src.getBytes());
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        this.decode(bais,baos);
        try {
                return new String(baos.toByteArray(), builder.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractCryptor(AbstractCryptBuilder builder){
        this.builder=builder;
    }
    protected AbstractCryptBuilder builder;


}

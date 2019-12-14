package com.rongji.dfish.base.util.img;

import com.rongji.dfish.base.util.ByteArrayUtil;

import java.io.IOException;
import java.io.InputStream;

public class ImageInfo{
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_BMP = 1;
    public static final int TYPE_GIF = 2;
    public static final int TYPE_JPEG = 3;
    public static final int TYPE_PNG = 4;
    public static final String[] TYPE_NAMES = {"unknown", "bmp", "gif", "jpg", "png"};
    public static final byte[] HEAD_JPEG = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    public static final byte[] HEAD_PNG = new byte[]{(byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G'};
    public static final byte[] HEAD_GIF = new byte[]{(byte) 'G', (byte) 'I', (byte) 'F'};
    public static final byte[] HEAD_BMP = new byte[]{(byte) 'B', (byte) 'M'};
    public static final ImageInfo UNKNOWN=new ImageInfo();

    private int width;
    private int height;
    private int type;

    public static ImageInfo of(InputStream source) throws IOException {

        //尝试读取8K字节。 判断是什么数据类型，
        //如果是PNG/BMP/GIF 则直接读取信息。
        //如果是JPG
        // 如果是quick 模式 只返回 ImgInfo 则尝试从8K中读取需要的信息，如果读取不到，则往后读取到信息为止。
        // 如果不是quick模式，需要返回 JpegInfo 里面应该包含ISO等扩展信息。和缩略图的信息，做图片压缩的时候可能用到上。
        // 这时，应该先读取完整的图片字节。并取得除了图片主体信息外所有EXIF信息和缩略图的EXIF信息。
        byte[] buff=new byte[8192];
        try {
            int read = source.read(buff);
            if (ByteArrayUtil.startsWith(buff, HEAD_JPEG)) {
                return JpegInfo.readJpegInfo(buff,read,source);
            } else if (ByteArrayUtil.startsWith(buff, HEAD_PNG)) {
                return readPngInfo(buff,read);
            } else if (ByteArrayUtil.startsWith(buff, HEAD_GIF)) {
                return readGifInfo(buff ,read);
            } else if (ByteArrayUtil.startsWith(buff, HEAD_BMP)) {
                return readBmpInfo(buff,read);
            }
        }catch(IOException ex){
            throw ex;
        }finally {
            try {
                source.close();
            }catch (IOException ex){ }
        }
        return ImageInfo.UNKNOWN;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getType() {
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }
    public String getTypeName() {
        if(getType()<0||getType()>=TYPE_NAMES.length){
            setType(0);
        }
        return TYPE_NAMES[type];
    }



    private static ImageInfo readBmpInfo(byte[] src,int read) {
        if (read < 30) {
            return  ImageInfo.UNKNOWN;
        }else {
            ImageInfo ii=new ImageInfo();
            ii.setType(TYPE_BMP);
            ii.setWidth(ByteArrayUtil.readShortLittleEndian(src, 18));
            int height = ByteArrayUtil.readShortLittleEndian(src, 22);
            if (height < 0) {
                height = -height;
            }
            ii.setHeight(height);
            return ii;
        }
    }
    private static ImageInfo readGifInfo(byte[] src,int read) {
        if (read < 10) {
            return ImageInfo.UNKNOWN;
        }else {
            ImageInfo ii=new ImageInfo();
            ii.setType(TYPE_GIF);
            ii.setHeight(ByteArrayUtil.readShortLittleEndian(src, 8));
            ii.setWidth(ByteArrayUtil.readShortLittleEndian(src, 6));
            return ii;
        }
    }

    private static ImageInfo readPngInfo(byte[] src,int read) {
        if (read < 24) {
            return ImageInfo.UNKNOWN;
        }else {
            ImageInfo ii=new ImageInfo();
            ii.setType(TYPE_PNG);
            ii.setWidth(ByteArrayUtil.readIntBigEndian(src, 16));
            ii.setHeight(ByteArrayUtil.readIntBigEndian(src, 20));
            return null;
        }
    }
}

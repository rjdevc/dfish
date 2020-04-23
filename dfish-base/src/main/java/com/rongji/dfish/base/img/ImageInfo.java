package com.rongji.dfish.base.img;

import com.rongji.dfish.base.util.ByteArrayUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * 图片的基本信息，主要是图片的真实类型，高度和宽度
 */
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

    /**
     * 从 InputString 中读取图片基本信息
     *
     * 快速获取图形信息。这个操作一般会消耗掉InputStream
     *一般来说，小于2MB 的图片这个性能提升的不明显。
     * 但如果是大于2MB的JPEG，提升就相当明显，并且有很大几率找到硬件(相机/手机)已经预设的缩略图。
     * 在一些场景可能大大提高性能。注意，这个缩略图通常只有160*120像素上下。
     * 如果需要更大的缩略图。也不适合使用这个方法。
     * @return ImageInfo
     *
     * @param input InputStream
     * @return ImageInfo
     * @throws IOException 图片读取的IO异常
     */
    public static ImageInfo of(InputStream input) throws IOException {

        //尝试读取8K字节。 判断是什么数据类型，
        //如果是PNG/BMP/GIF 则直接读取信息。
        //如果是JPG
        // 如果是quick 模式 只返回 ImgInfo 则尝试从8K中读取需要的信息，如果读取不到，则往后读取到信息为止。
        // 如果不是quick模式，需要返回 JpegInfo 里面应该包含ISO等扩展信息。和缩略图的信息，做图片压缩的时候可能用到上。
        // 这时，应该先读取完整的图片字节。并取得除了图片主体信息外所有EXIF信息和缩略图的EXIF信息。
        byte[] buff=new byte[8192];
        try {
            int read = input.read(buff);
            if (ByteArrayUtil.startsWith(buff, HEAD_JPEG)) {
                return JpegInfo.readJpegInfo(buff,read,input);
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
                input.close();
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

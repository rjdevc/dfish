package com.rongji.dfish.base.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片处理助手
 */
public class ImageUtil {

    /**
     * 是否图片扩展名
     *
     * @param fileExtName
     * @return boolean
     */
    public static boolean isImageExtName(String fileExtName) {
        if (Utils.isEmpty(fileExtName)) {
            return false;
        }
        // 非支持格式的文件，不进行处理
        List<String> supportTypes = Arrays.asList(ImageIO.getReaderFormatNames());
        return supportTypes.contains(fileExtName.toLowerCase());
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算得到最合理大小的图片
     *
     * @param rawImage 原始图片
     * @param width    缩放后的宽度
     * @param height   缩放后的高度
     * @return BufferedImage
     */
    public static BufferedImage getZoomedImage(BufferedImage rawImage, int width, int height) {
        return getZoomedImage(rawImage, width, height, true);
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算得到最合理大小的图片
     *
     * @param rawImage   原始图片
     * @param width      缩放后的宽度,<=0时采用原始图片宽度
     * @param height     缩放后的高度,<=0时采用原始图片高度
     * @param destInside 原图是否在指定宽高范围内
     * @return BufferedImage
     */
    public static BufferedImage getZoomedImage(BufferedImage rawImage, int width, int height, boolean destInside) {
        if (rawImage == null) {
            return null;
        }
        if (width <= 0 && height <= 0) {
            return rawImage;
        } else if (width <= 0) {
            width = rawImage.getWidth();
        } else if (height <= 0) {
            height = rawImage.getHeight();
        }
        // 宽度缩放比例
        double widthScale = new Double(width) / rawImage.getWidth();
        // 高度缩放比例
        double heightScale = new Double(height) / rawImage.getHeight();
        // 如果原图都是在指定宽高内采用比例低的;若生成图片在原图范围内采取比例高的
        boolean basedWidth = destInside ? widthScale < heightScale : widthScale > heightScale;
        int destWidth;
        int destHeight;
        if (basedWidth) {
            destWidth = width;

            destHeight = (int) (widthScale * rawImage.getHeight());
            destHeight = destInside && destHeight > height ? height : destHeight;
        } else {
            destWidth = (int) (heightScale * rawImage.getWidth());
            destWidth = destInside && destWidth > width ? width : destWidth;

            destHeight = height;
        }
        int imgType = rawImage.getType();
        // 获取目标图片
        BufferedImage destImage = new BufferedImage(destWidth, destHeight, imgType);

        Graphics g = destImage.getGraphics();
        g.drawImage(rawImage, 0, 0, destWidth, destHeight, null);
        // 画笔释放
        g.dispose();
        return destImage;
    }

    /**
     * 根据图片按比例缩放,图片的大小比例保持不变,系统计算最合理的大小进行输出
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @param fileExtName 输出图片类型
     * @param width       输出图片的宽,<=0时采用原始图片宽度
     * @param height      输出图片的高,<=0时采用原始图片高度
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output, String fileExtName, int width, int height) throws Exception {
        checkArguments(input, output, fileExtName);

        try {
            // 读取原始图片
            ImageTypeDeligate itd = new ImageTypeDeligate(input);
            BufferedImage rawImage = ImageIO.read(itd);
            // 获取缩放的图片
            BufferedImage destImage = getZoomedImage(rawImage, width, height);
            writeImage(destImage, itd.getImageTypeName(), output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 根据图片按照原始尺寸缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @param fileExtName 输出图片类型
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void zoom(InputStream input, OutputStream output, String fileExtName) throws Exception {
        zoom(input, output, fileExtName, -1, -1);
    }

    /**
     * 判断参数
     *
     * @param input
     * @param output
     * @param fileExtName
     */
    private static void checkArguments(InputStream input, OutputStream output, String fileExtName) {
        if (input == null || output == null) {
            throw new IllegalArgumentException("输入流或输出流为空");
        }
        if (!isImageExtName(fileExtName)) {
            throw new IllegalArgumentException("输出的非图片文件类型");
        }
    }

    /**
     * 输入图片
     *
     * @param destImage
     * @param fileExtName
     * @param output
     * @throws Exception
     */
    private static void writeImage(BufferedImage destImage, String fileExtName, OutputStream output) throws Exception {
        // 输出图片
        ImageIO.write(destImage, fileExtName, output);
    }

    /**
     * 根据图片按指定宽高强制缩放
     * 图片按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input       图片文件输入流
     * @param output      图片文件输出流
     * @param fileExtName 输出图片类型
     * @param width       输出图片的宽
     * @param height      输出图片的高
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void resize(InputStream input, OutputStream output, String fileExtName, int width, int height) throws Exception {
        checkArguments(input, output, fileExtName);
        if (width <= 0 && height <= 0) { // 按照原始尺寸输出
            zoom(input, output, fileExtName);
            return;
        }
        Graphics g = null;
        try {
            // 读取原始图片
            ImageTypeDeligate itd = new ImageTypeDeligate(input);
            BufferedImage rawImage = ImageIO.read(itd);
            int imageType = rawImage.getType();
            // 目标图片创建
            BufferedImage destImage = new BufferedImage(width, height, imageType);
            // 获取画笔工具
            g = destImage.getGraphics();
//            if (bgColor == null) { // 无填充背景色
            // 按指定大小强制缩放
            g.drawImage(rawImage, 0, 0, width, height, null);
//            } else {
//                // 输出图片填充背景色(为空时,不填充,按照指定宽高缩放;不为空时,按比例缩放到最合理的大小后填充颜色)
//                // 设置背景色
//                g.setColor(bgColor);
//                // 必须调用这个方法将背景色填充到图片去
//                g.fillRect(0, 0, width, height);
//                // 获取按比例缩放的图片
//                BufferedImage zoomedImage = getZoomedImage(rawImage, width, height);
//                // 横纵坐标偏移量
//                int x = (width - zoomedImage.getWidth()) / 2;
//                int y = (height - zoomedImage.getHeight()) / 2;
//                g.drawImage(zoomedImage, x, y, bgColor,null);
//            }
            writeImage(destImage, itd.getImageTypeName(), output);
        } finally {
            if (g != null) {
                g.dispose();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 根据图片按指定宽高切割(图片最中间部分),并按照输出流进行输出,输出完成后将自动关闭输入流和输出流
     *
     * @param input  图片文件输入流
     * @param output 图片文件输出流
     * @param width  缩放图片的宽
     * @param height 缩放图片的高
     * @throws Exception 当输入流非图片或者图片绘制过程中可能有异常
     */
    public static void cut(InputStream input, OutputStream output, String fileExtName, int width, int height) throws Exception {
        checkArguments(input, output, fileExtName);

        if (width <= 0 && height <= 0) { // 按照原始尺寸输出
            zoom(input, output, fileExtName);
            return;
        }

        try {
            // 读取原始图片
            ImageTypeDeligate itd = new ImageTypeDeligate(input);
            BufferedImage rawImage = ImageIO.read(itd);
            // 获取缩放的图片
            BufferedImage destImage = getZoomedImage(rawImage, width, height, false);

            int x = (destImage.getWidth() - width) / 2;
            int y = (destImage.getHeight() - height) / 2;
//            // 输出图片
//            ImageIO.write(destImage.getSubimage(x, y, width, height), fileExtName, output);
            writeImage(destImage.getSubimage(x, y, width, height), itd.getImageTypeName(), output);
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }


    static class ImageTypeDeligate extends SampleDeligate {
        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_BMP = 1;
        public static final int TYPE_GIF = 2;
        public static final int TYPE_JPEG = 3;
        public static final int TYPE_PNG = 4;
        private static final String[] TYPE_NAMES = {"unknown", "bmp", "gif", "jpg", "png"};

        public ImageTypeDeligate(InputStream raw) {
            super(raw, 4);
        }

        public int getImageType() {
            byte[] sample = getSample();
            if (match(sample, JPEG_FEATURE)) {
                return TYPE_JPEG;
            }
            if (match(sample, PNG_FEATURE)) {
                return TYPE_PNG;
            }
            if (match(sample, GIF_FEATURE)) {
                return TYPE_GIF;
            }
            if (match(sample, BMP_FEATURE)) {
                return TYPE_BMP;
            }
            return TYPE_UNKNOWN;
        }

        public String getImageTypeName() {
            return TYPE_NAMES[getImageType()];
        }

        private boolean match(byte[] sample, byte[] feature) {
            int len = Math.min(sample.length, feature.length);
            for (int i = 0; i < len; i++) {
                if (sample[i] != feature[i]) {
                    return false;
                }
            }
            return true;
        }

        private static final byte[] JPEG_FEATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
        private static final byte[] PNG_FEATURE = new byte[]{(byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G'};
        private static final byte[] GIF_FEATURE = new byte[]{(byte) 'G', (byte) 'I', (byte) 'F'};
        private static final byte[] BMP_FEATURE = new byte[]{(byte) 'B', (byte) 'M'};

    }

    /**
     * 代理一个inputStream当他顺序读取的时候，将会留下最前方的sampleLength个byte作为标本。
     */
    static class SampleDeligate extends FilterInputStream {
        private byte[] sample;
        private int index;

        public SampleDeligate(InputStream in, int sampleLength) {
            super(in);
            sample = new byte[sampleLength];
            index = 0;
        }

        public SampleDeligate(InputStream in) {
            this(in, 64);
        }

        @Override
        public int read() throws IOException {
            int read = in.read();
            if (index < sample.length) {
                if (read >= 0 && read < 256) {
                    sample[index++] = (byte) read;
                }
            }
            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = in.read(b, off, len);
            if (index < sample.length) {
                int toSample = Math.min(sample.length - index, len);
                System.arraycopy(b, off, sample, index, toSample);
                index += toSample;
            }
            return read;
        }

        @Override
        public long skip(long n) throws IOException {
            if (index < sample.length) {
                int toSample = (int) Math.min(sample.length - index, n);
                byte[] temp = new byte[toSample];
                read(temp);
                return toSample + in.skip(n - toSample);
            }
            return in.skip(n);
        }

        public byte[] getSample() {
            return sample;
        }
    }

}

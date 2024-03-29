package com.rongji.dfish.base.img;

import com.rongji.dfish.base.util.ByteArrayUtil;
import com.rongji.dfish.base.util.LogUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 图像处理器。
 * 典型用法
 * ImageProcessor.of(new FileInputStream("test.jpg"))
 *   .zoom(160,160,true)
 *   .waterMark("榕基软件",16, new Color(51,102,153), -80,-32)
 *   .output(new FileOutputStream("test_thumb.jpg"));
 * 它会尽可能的减少图像格式转化过程中的临时文件保存，从而提高性能。
 */
public class ImageProcessor implements Cloneable {
    private InputStream input;
    private BufferedImage image;
    private List<ImageCallback> callbacks;
    private String realType;


    /**
     * 原始图片输入流
     *
     * @return InputStream
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * 待处理的图片，在没有动作之前，可能还未能从Input中读取，有可能是空的。
     *
     * @return BufferedImage
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * 待处理的动作。
     *
     * @return List
     */
    public List<ImageCallback> getCallbacks() {
        return callbacks;
    }

    /**
     * 尝试充输入流中发现真正的类型。而不是根据文件名后缀判断。
     * 有点时候后缀是错误的。
     *
     * @return String
     */
    public String getRealType() {
        return realType;
    }

    /**
     * 构造函数
     *
     * @param input InputStream
     */
    public ImageProcessor(InputStream input) {
        this.input = input;
        callbacks = new ArrayList<>();
    }

    /**
     * 构造函数
     *
     * @param image BufferedImage
     */
    public ImageProcessor(BufferedImage image) {
        this.image = image;
        callbacks = new ArrayList<>();
    }

    /**
     * 快速调用方式。
     *
     * @param input InputStream
     * @return ImageProcessor
     */
    public static ImageProcessor of(InputStream input) {
        return new ImageProcessor(input);
    }

    /**
     * 快速调用方式。
     *
     * @param image BufferedImage
     * @return ImageProcessor
     */
    public static ImageProcessor of(BufferedImage image) {
        return new ImageProcessor(image);
    }

    /**
     * 安排一个Callback动作，动作将在准备好前后文后执行。
     *
     * @param callback 动作
     * @return 本身
     */
    public ImageProcessor schedule(ImageCallback callback) {
        callbacks.add(callback);
        return this;
    }

    /**
     * 执行所有安排好的callback动作。
     *
     * @throws Exception exception
     */
    protected void execute() throws Exception {
        BufferedImage working = image;
        for (ImageCallback callback : callbacks) {
            working = callback.execute(working, this);
        }
        reset();
    }


    /**
     * mark 将会记住当前image状态，后续使用reset将回到这个状态。
     *
     * @return this
     * @throws Exception exception
     */
    public ImageProcessor mark() throws Exception {
        readImageFromIn();
        BufferedImage working = image;
        for (ImageCallback callback : callbacks) {
            working = callback.execute(working, this);
        }
        image = working;
        callbacks.clear();
        return this;
    }

    @Override
    public ImageProcessor clone() {
        try {
            readImageFromIn();
            ImageProcessor io = new ImageProcessor(clone(image));
            io.realType = this.realType;
            return io;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 文字水印。
     *
     * @param text     内容
     * @param fontSize 像素
     * @param color    颜色 需要半透明请颜色设置alpha值.注意这里取值范围是0-255
     * @param x        像素。如果x为负数，表示从右边算起。需要自行估计文本宽度
     * @param y        像素。如果y为负数，表示从下面算起。如果是正数需要自行估计文本高度
     * @return this
     */
    public ImageProcessor watermark(String text, int fontSize, Color color, int x, int y) {
        return watermark(text, new Font(Font.DIALOG, Font.PLAIN, fontSize), color, x, y);
    }

    /**
     * 文字水印
     *
     * @param text  内容
     * @param font  字体
     * @param color 颜色 需要半透明请颜色设置alpha值.注意这里取值范围是0-255
     * @param x     像素。如果x为负数，表示从右边算起。需要自行估计文本宽度
     * @param y     像素。如果y为负数，表示从下面算起。如果是正数需要自行估计文本高度
     * @return this
     */
    public ImageProcessor watermark(String text, Font font, Color color, int x, int y) {
        schedule((image, processor) -> {
            int width = image.getWidth(); // 得到源图宽
            int height = image.getHeight(); // 得到源图长
            int realx = x;
            int realy = y;
            if (realx < 0) {
                realx = width + realx;
            }
            if (realy < 0) {
                realy = height + realy;
            }
            /*
            复制一个图片制作水印，不破坏原有的image
             */

            image = clone(image);

            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor(color);
            g.setFont(font);
            if (color != null) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) (color.getAlpha() / 255.0)));
            }
            // 在指定坐标绘制水印文字
            g.drawString(text, realx, realy);
            g.dispose();
            return image;
        });
        return this;
    }

    /**
     * 图片水印。
     *
     * @param img   内容
     * @param alpha 半透明请颜色设置alpha值 范围0.0 - 1.0
     * @param x     像素。如果x为负数，表示从右边算起。需要自行估计图片宽度
     * @param y     像素。如果y为负数，表示从下面算起。需要自行估计图片高度
     * @return this
     */
    public ImageProcessor watermark(BufferedImage img, float alpha, int x, int y) {
        if (img == null) {
            throw new IllegalArgumentException("img == null!");
        }
        schedule((image, processor) -> {
            int width = image.getWidth(); // 得到源图宽
            int height = image.getHeight(); // 得到源图长
            int realx = x;
            int realy = y;
            if (realx < 0) {
                realx = width + realx;
            }
            if (realy < 0) {
                realy = height + realy;
            }
             /*
            复制一个图片制作水印，不破坏原有的image
             */
            image = clone(image);

            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawImage(img, realx, realy, img.getWidth(), img.getHeight(), null);
            g.dispose();
            return image;
        });
        return this;
    }

    /**
     * 图片水印
     *
     * @param another 内容
     * @param alpha   半透明请颜色设置alpha值 范围0.0 - 1.0
     * @param x       像素。如果x为负数，表示从右边算起。需要自行估计图片宽度
     * @param y       像素。如果y为负数，表示从下面算起。需要自行估计图片高度
     * @return this
     * @throws IOException 读取another里面的内容可能会发生 IOException
     */
    public ImageProcessor watermark(ImageProcessor another, float alpha, int x, int y) throws IOException {
        if (another.image == null) {
            another.readImageFromIn();
        }
        return watermark(another.image, alpha, x, y);
    }

    private void readImageFromIn() throws IOException {
        if (image == null) {
            try {
                ImageTypeDecorator itd = new ImageTypeDecorator(input);
                image = ImageIO.read(itd);
                if (image == null) {
                    throw new IllegalArgumentException("unsupported image type!");
                }
                this.realType = itd.getTypeName();
            } catch (IOException iex) {
                throw iex;
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
    }

    private static BufferedImage clone(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        return new BufferedImage(cm, image.copyData(null), cm.isAlphaPremultiplied(), null);
    }

    /**
     * 缩放到
     *
     * @param width  int
     * @param height int
     * @return ImageProcessor
     */
    public ImageProcessor resize(int width, int height) {
        return schedule((image, processor) -> {
            BufferedImage destImage = new BufferedImage(width, height, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            Image smooth =
                    image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            g.drawImage(smooth, 0, 0, width, height, null);
            g.dispose();
            return destImage;
        });
    }

    /**
     * 剪切，一般x或y可能是负数。
     *
     * @param x      图片绘制位置，如果是负数，左边-x个像素会在画布外，相当于左边会被切出-x个像素
     * @param y      图片绘制位置，如果是负数，上方-y个像素会在画布外，相当于上方会被切出-y个像素
     * @param width  剪切完图片宽度
     * @param height 剪切完图片高度
     * @return this
     */
    public ImageProcessor cut(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || width < 0 || height < 0) {
            throw new IllegalArgumentException("X, Y, width, height should not lses than 0");
        }
        return schedule((image, processor) -> {
            BufferedImage destImage = new BufferedImage(width, height, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            g.drawImage(image, x, y, width, height, null);
            g.dispose();
            return destImage;
        });
    }

    /**
     * 剪切到大小，取图片中心内容。
     *
     * @param width  剪切完图片宽度
     * @param height 剪切完图片高度
     * @return this
     */
    public ImageProcessor cut(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("width, height should not lses than 0");
        }
        return schedule((image, processor) -> {
            int realWidth = width;
            int realHeight = height;
            if (image.getWidth() < realWidth) {
                realWidth = image.getWidth();
            }
            if (image.getHeight() < realHeight) {
                realHeight = image.getHeight();
            }
            BufferedImage destImage = new BufferedImage(realWidth, realHeight, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            g.drawImage(image, (realWidth - image.getWidth()) / 2, (realHeight - image.getHeight()) / 2, image.getWidth(), image.getHeight(), null);
            g.dispose();
            return destImage;
        });
    }

    /**
     * 等比例缩放到不大于指定高宽
     *
     * @param width  int
     * @param height int
     * @return this
     */
    public ImageProcessor zoom(int width, int height) {
        return zoom(width, height, true);
    }

    /**
     * 等比例缩放到不大于指定高宽
     *
     * @param width    int
     * @param height   int
     * @param contains 最终输出图是否包含在这个范围内。如果为true则在内部。否则，可以有部分超出。
     *                 如果true，后续动作可能会通过补背景色让输出的图片吻合实际大小。
     *                 如果false，后续动作可能会通过cut 让输出的图片吻合实际大小。
     * @return this
     */
    public ImageProcessor zoom(int width, int height, boolean contains) {
        return schedule((image, processor) -> {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            int destWidth = width < 0 ? imageWidth : width;
            int destHeight = height < 0 ? imageHeight : height;
            double scaleWidth = 1.0 * destWidth / imageWidth;
            double scaleHeight = 1.0 * destHeight / imageHeight;
            double scale = contains ? Math.min(scaleWidth, scaleHeight) : Math.max(scaleWidth, scaleHeight);
            int canvasWidth = (int) (imageWidth * scale + 0.5);
            int canvasHeight = (int) (imageHeight * scale + 0.5);
            BufferedImage destImage = new BufferedImage(canvasWidth, canvasHeight, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            Image smooth =
                    image.getScaledInstance(canvasWidth, canvasHeight, java.awt.Image.SCALE_SMOOTH);
            g.drawImage(smooth, 0, 0, canvasWidth, canvasHeight, null);
            g.dispose();
            return destImage;
        });
    }

    /**
     * 缩放并且增加补白，以填满指定大小
     * @param width 宽度
     * @param height 高度
     * @param color 颜色。注意如果使用透明的颜色的话，要确认图片保存格式是否支持透明。
     * @return 自己
     */
    public ImageProcessor zoomAndPad(int width, int height, Color color) {
        return schedule((image, processor) -> {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            double scaleWidth = 1.0 * width / imageWidth;
            double scaleHeight = 1.0 * height / imageHeight;
            double scale = Math.min(scaleWidth, scaleHeight);
            int canvasWidth = (int) (imageWidth * scale + 0.5);
            int canvasHeight = (int) (imageHeight * scale + 0.5);
            BufferedImage destImage = new BufferedImage(width, height, image.getType());

            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            g.setColor(color);
            g.fillRect(0, 0, width, height);
            Image smooth =
                    image.getScaledInstance(canvasWidth, canvasHeight, java.awt.Image.SCALE_SMOOTH);
            g.drawImage(smooth, (width - canvasWidth) / 2, (height - canvasHeight) / 2,
                    canvasWidth, canvasHeight, null);
            g.dispose();
            return destImage;
        });
    }

    /**
     * 缩放并把不符合长宽比的内容剪切走，保留中心的内容
     * @param width 宽度
     * @param height 高度
     * @return 自己
     */
    public ImageProcessor zoomAndCut(int width, int height) {
        return schedule((image, processor) -> {
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            double scaleWidth = 1.0 * width / imageWidth;
            double scaleHeight = 1.0 * height / imageHeight;
            double scale = Math.max(scaleWidth, scaleHeight);
            int canvasWidth = (int) (imageWidth * scale + 0.5);
            int canvasHeight = (int) (imageHeight * scale + 0.5);
            BufferedImage destImage = new BufferedImage(width, height, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            Image smooth =
                    image.getScaledInstance(canvasWidth, canvasHeight, java.awt.Image.SCALE_SMOOTH);
            g.drawImage(smooth, (width - canvasWidth) / 2, (height - canvasHeight) / 2, width, height, null);
            g.dispose();
            return destImage;
        });
    }


    /**
     * 等比例缩放
     *
     * @param scale double 比例 小于1是缩小，大于1是放大
     * @return this
     */
    public ImageProcessor zoom(double scale) {
        return schedule((image, processor) -> {
            int width = (int) (image.getWidth() * scale + 0.5);
            int height = (int) (image.getHeight() * scale + 0.5);
            BufferedImage destImage = new BufferedImage(width, height, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            Image smooth =
                    image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            g.drawImage(smooth, 0, 0, width, height, null);
            g.dispose();
            return destImage;
        });
    }


    /**
     * 图片完成并输出
     *
     * @param output 输出地址
     * @throws Exception 图片输出异常
     */
    public void output(OutputStream output) throws Exception {
        if (callbacks.size() == 0 && image == null) {
            //什么都没做，而且也没有指定文件名变化的。
            // 直接文件流拷贝
            byte[] buff = new byte[8192];
            int read = 0;
            try {
                while ((read = input.read(buff)) >= 0) {
                    output.write(buff, 0, read);
                }
            } catch (IOException iex) {
                LogUtil.error(null, iex);
            } finally {
                try {
                    input.close();
                } catch (IOException iex2) {
                }
                try {
                    output.close();
                } catch (IOException iex2) {
                }
            }
        } else {
            output(output, null);
        }
    }

    /**
     * 图片完成并输出,可同时改变图片类型
     * 对于照片等，全图有各种信息的用JPEG效果更好。
     * 对于图标等，图片中只有8-16种颜色的。用GIF或PNG会有较好的效果。
     * GIF和PNG都支持透明。一般只有在做动画的时候才需要用GIF
     *
     * @param output    输出地址
     * @param imageType 可改变图片类型。jpg(jpeg) png gif
     * @throws Exception 图片输出异常
     */
    public void output(OutputStream output, String imageType) throws Exception {
        callbacks.add((image, processor) -> {
            String realType = imageType == null ? processor.realType : imageType;
            try {
                ImageIO.write(image, realType, output);
                return image;
            } catch (IOException iex) {
                throw iex;
            } finally {
                if (output != null) {
                    output.close();
                }
            }
        });
        execute();
    }

    /**
     * 将状态重置到 上次mark 标记后的状态。
     * 如果都没有reset过则是返回初始状态(读取的原始图片)
     *
     * @return this
     */
    public ImageProcessor reset() {
        this.callbacks.clear();
        return this;
    }

    /**
     * 代理类，这个代理类类读取图片的时候，将留下4个字节做为样本。
     * 并根据这个前面的4个字节得到图片的类型。
     */
    static class ImageTypeDecorator extends SampleDecorator {

        public ImageTypeDecorator(InputStream in) {
            super(in, 4);
        }

        public int getImageType() {
            byte[] sample = getSample();
            if (ByteArrayUtil.startsWith(sample, ImageInfo.HEAD_JPEG)) {
                return ImageInfo.TYPE_JPEG;
            }
            if (ByteArrayUtil.startsWith(sample, ImageInfo.HEAD_PNG)) {
                return ImageInfo.TYPE_PNG;
            }
            if (ByteArrayUtil.startsWith(sample, ImageInfo.HEAD_GIF)) {
                return ImageInfo.TYPE_GIF;
            }
            if (ByteArrayUtil.startsWith(sample, ImageInfo.HEAD_BMP)) {
                return ImageInfo.TYPE_BMP;
            }
            return ImageInfo.TYPE_UNKNOWN;
        }

        public String getTypeName() {
            return ImageInfo.TYPE_NAMES[getImageType()];
        }
    }

    /**
     * 代理一个inputStream当他顺序读取的时候，将会留下最前方的sampleLength个byte作为标本。
     */
    static class SampleDecorator extends FilterInputStream {
        private byte[] sample;
        private int index;

        public SampleDecorator(InputStream in, int sampleLength) {
            super(in);
            sample = new byte[sampleLength];
            index = 0;
        }

        public SampleDecorator(InputStream in) {
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


//    /**
//     * 等比例缩放
//     */
//    public static class AdvancedZoomCallback implements ImageCallback {
//        Integer maxWidth;
//        Integer maxHeight;
//        Integer minWidth;
//        Integer minHeight;
//        boolean suitForScope = true;
//        Double maxAspectRatio;
//        Double fixAspectRatio;
//        Color paddingColor;
//
//        /**
//         * 最大宽度
//         *
//         * @return Integer
//         */
//        public Integer getMaxWidth() {
//            return maxWidth;
//        }
//
//        /**
//         * 最大宽度
//         *
//         * @param maxWidth Integer
//         */
//        public void setMaxWidth(Integer maxWidth) {
//            this.maxWidth = maxWidth;
//        }
//
//        /**
//         * 最大高度
//         *
//         * @return Integer
//         */
//        public Integer getMaxHeight() {
//            return maxHeight;
//        }
//
//        /**
//         * 最大高度
//         *
//         * @param maxHeight Integer
//         */
//        public void setMaxHeight(Integer maxHeight) {
//            this.maxHeight = maxHeight;
//        }
//
//        /**
//         * 最小宽度
//         *
//         * @return Integer
//         */
//        public Integer getMinWidth() {
//            return minWidth;
//        }
//
//        /**
//         * 最小宽度
//         *
//         * @param minWidth Integer
//         */
//        public void setMinWidth(Integer minWidth) {
//            this.minWidth = minWidth;
//        }
//
//        /**
//         * 最小高度
//         *
//         * @return Integer
//         */
//        public Integer getMinHeight() {
//            return minHeight;
//        }
//
//        /**
//         * 最小高度
//         *
//         * @param minHeight Integer
//         */
//        public void setMinHeight(Integer minHeight) {
//            this.minHeight = minHeight;
//        }
//
//        /**
//         * 缩放的时候，如果太小是否进行放大到其中一边到达maxWidth或maxHeight，
//         * 如果太大就就缩小到minWidth或者minHeight
//         *
//         * @return boolean
//         */
//        public boolean isSuitForScope() {
//            return suitForScope;
//        }
//
//        /**
//         * 缩放的时候，如果太小是否进行放大到其中一边到达maxWidth或maxHeight，
//         * 如果太大就就缩小到minWidth或者minHeight
//         *
//         * @param suitForScope boolean
//         */
//        public void setSuitForScope(boolean suitForScope) {
//            this.suitForScope = suitForScope;
//        }
//
//        /**
//         * 最大比例
//         * 如果长宽比超出最大比例，将会切掉超宽或超大的部分。保留中间的部分
//         * 这个比例设置为2 和设置为0.5是等价的。
//         *
//         * @return Double
//         */
//        public Double getMaxAspectRatio() {
//            return maxAspectRatio;
//        }
//
//        /**
//         * 最大比例
//         * 如果长宽比超出最大比例，将会切掉超宽或超大的部分。保留中间的部分
//         * 这个比例设置为2 和设置为0.5是等价的。
//         *
//         * @param maxAspectRatio Double
//         */
//        public void setMaxAspectRatio(Double maxAspectRatio) {
//            this.maxAspectRatio = maxAspectRatio;
//        }
//
//        /**
//         * 如果超出最大比例的时候，并设置了fixScale 则不再用最大比例计算长宽比。而用该比例计算。
//         * 比如说，可能maxScale设置了2.0.反正这时候都要切图了。有可能干脆多切一点，保留4.0/3.0
//         * 可见范围也更多一些。
//         *
//         * @return Double
//         */
//        public Double getFixAspectRatio() {
//            return fixAspectRatio;
//        }
//
//        /**
//         * 如果超出最大比例的时候，并设置了fixScale 则不再用最大比例计算长宽比。而用该比例计算。
//         * 比如说，可能maxScale设置了2.0.反正这时候都要切图了。有可能干脆多切一点，保留1.2
//         * 可见范围也更多一些。
//         *
//         * @param fixAspectRatio Double
//         */
//        public void setFixAspectRatio(Double fixAspectRatio) {
//            this.fixAspectRatio = fixAspectRatio;
//        }
//
//        /**
//         * padColor为填充颜色，如果要透明填充，可以设置alpha值为最大。
//         * 填充仅发生在设置了maxWidth和maxHeight，并且等比例缩放后,原图的比例和画布比例不一致。
//         * 出现图片和边框中间有空白的，这些空白将会填充这些颜色。
//         *
//         * @return Color
//         */
//        public Color getPaddingColor() {
//            return paddingColor;
//        }
//
//        /**
//         * padColor为填充颜色，如果要透明填充，可以设置alpha值为最大。
//         * 填充仅发生在设置了maxWidth和maxHeight，并且等比例缩放后,原图的比例和画布比例不一致。
//         * 出现图片和边框中间有空白的，这些空白将会填充这些颜色。
//         *
//         * @param paddingColor Color
//         */
//        public void setPaddingColor(Color paddingColor) {
//            this.paddingColor = paddingColor;
//        }
//
//        @Override
//        public BufferedImage execute(BufferedImage image, ImageProcessor processor) throws Exception {
//            int fixedWidth = 0, canvasWidth = 0;
//            int fixedHeight = 0, canvasHeight = 0;
//            boolean needCut = false;
//            int cutX = 0, cutY = 0, cutW = 0, cutH = 0;
//            int imageWidth = image.getWidth();
//            int imageHeight = image.getHeight();
//            if (maxAspectRatio != null) {
//                double maxRateTmp = maxAspectRatio < 1.0 ? 1.0 / maxAspectRatio : maxAspectRatio;
//                double nowRate = 1.0 * imageWidth / imageHeight;
//                nowRate = nowRate < 1.0 ? 1.0 / nowRate : nowRate;
//                if (nowRate > maxRateTmp) {
//                    //需要剪切
//                    needCut = true;
//                    double realCutScale = maxRateTmp;
//                    if (fixAspectRatio != null) {
//                        double fixRateTemp = fixAspectRatio < 1.0 ? 1.0 / fixAspectRatio : fixAspectRatio;
//                        if (fixAspectRatio < realCutScale) {
//                            realCutScale = fixRateTemp;
//                        }
//                    }
//                    if (imageWidth > imageHeight) {
//                        cutW = (int) (imageHeight * realCutScale + 0.5);
//                        cutH = imageHeight;
//                        cutX = (imageWidth - cutW) / 2;
//                        cutY = 0;
//                        imageWidth = cutW;
//                    } else {
//                        cutH = (int) (imageWidth * realCutScale + 0.5);
//                        cutW = imageWidth;
//                        cutY = (imageHeight - cutH) / 2;
//                        cutX = 0;
//                        imageHeight = cutH;
//                    }
//                }
//            }
//            if (minHeight != null && minWidth != null) {
//
//                double widthScale = new Double(minWidth) / imageWidth;
//                double heightScale = new Double(minHeight) / imageHeight;
//                if (widthScale <= 1.0 && heightScale <= 1.0) {
//                    if (!needCut&&!suitForScope) {
//                        return image; //无需缩放
//                    }
//                }
//                double scale = Math.max(widthScale, heightScale);
//                canvasWidth = fixedWidth = (int) (imageWidth * scale + 0.5);
//                canvasHeight = fixedHeight = (int) (imageHeight * scale + 0.5);
//            } else if (maxHeight != null && maxWidth != null) {
//                if (maxHeight < 0) {
//                    maxHeight = image.getHeight();
//                }
//                if (maxWidth < 0) {
//                    maxWidth = image.getWidth();
//                }
//
//                double widthScale = new Double(maxWidth) / imageWidth;
//                double heightScale = new Double(maxHeight) / imageHeight;
//
//                double scale = 1.0;
//                if (widthScale >= 1.0 && heightScale >= 1.0) {
//                    if (!suitForScope) {
//                        if (paddingColor == null && !needCut) {
//                            //无需缩放
//                            return image;
//                        }
//                        //scale=1.0;
//                    } else {
//                        scale = Math.min(widthScale, heightScale);
//                    }
//                } else {
//                    scale = Math.min(widthScale, heightScale);
//                }
//                fixedWidth = (int) (imageWidth * scale + 0.5);
//                fixedHeight = (int) (imageHeight * scale + 0.5);
//                if (paddingColor == null) {
//                    canvasWidth = fixedWidth;
//                    canvasHeight = fixedHeight;
//                } else {
//                    canvasWidth = maxWidth;
//                    canvasHeight = maxHeight;
//                }
//            }
////            System.out.println(
////                    "调试信息，预计画布 width= "+canvasWidth+" height="+canvasWidth+
////                            "\r\n预计需要切图 need=未完成 切图x=未完成 切图y=未完成 切图width=未完成 切图height=未完成" +
////                            "\r\n预计图像大小为 width="+fixedWidth+" height="+minHeight);
//
//            BufferedImage destImage = new BufferedImage(canvasWidth, canvasHeight, image.getType());
//            // 获取画笔工具
//            Graphics g = destImage.getGraphics();
//            if (paddingColor != null) {
//                g.setColor(paddingColor);
//                g.fillRect(0, 0, canvasWidth, canvasHeight);
//            }
//            if (needCut) {
//                image = image.getSubimage(cutX, cutY, cutW, cutH);
//            }
//            Image smooth =
//                    image.getScaledInstance(fixedWidth, fixedHeight, java.awt.Image.SCALE_SMOOTH);
//            g.drawImage(smooth, (canvasWidth - fixedWidth) / 2, (canvasHeight - fixedHeight) / 2,
//                    fixedWidth, fixedHeight, null);
//            g.dispose();
//            return destImage;
//        }
//
//    }

    /**
     * 具体的操作动作，操作动作可以通过回调来减少具体操作的损耗。
     */
    public interface ImageCallback {
        /**
         * 执行动作
         *
         * @param image     上一步处理完以后的image对象。
         * @param processor 操作本身句柄，可获得上下文
         * @return 这步处理完以后的image对象。如果这步对image有任何改变，建议不要在原image上改变，
         * 而应该新建一个image或复制image。防止mark和reset出现异常。
         * @throws Exception exception
         */
        BufferedImage execute(BufferedImage image, ImageProcessor processor) throws Exception;
    }


}

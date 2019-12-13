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

    public static class ImageOperation {
        //FIXME 文字水印 图片水印
        //FIXME 剪切
        //FIXME 快速获得图片的大小（不启动BufferedImage）
        //FIXME 尝试获得JPG原始缩略图（不启动BufferedImage）
        private InputStream source;
        private BufferedImage image;
        private List<ImageCallBack> works;
        private String realType;

        public ImageOperation(InputStream source){
            this.source=source;
            works=new ArrayList<>();
        }
        public ImageOperation(BufferedImage image){
            this.image=image;
            works=new ArrayList<>();
        }
        public static ImageOperation of(InputStream source){
            return new ImageOperation(source);
        }
        public static ImageOperation of(BufferedImage image){
            return new ImageOperation(image);
        }

        /**
         * 缩放到
         * @param width
         * @param height
         * @return
         */
        public ImageOperation resize(int width, int height)  {
           return schedule((image,oper)->{
               BufferedImage destImage = new BufferedImage(width, height, image.getType());
               // 获取画笔工具
               Graphics g = destImage.getGraphics();
               g.drawImage(image, 0, 0, width, height, null);
               g.dispose();
               return destImage;
           });
        }

        /**
         * 等比例缩放到不大于指定高宽
         * @param maxWidth
         * @param maxHeight
         * @return
         */
        public ImageOperation zoomNotBiggerThan(int maxWidth, int maxHeight)  {
            ZoomCallBack zcb=new ZoomCallBack();
            zcb.setMaxHeight(maxHeight);
            zcb.setMaxWidth(maxWidth);
            return schedule(zcb);
        }

        /**
         * 等比例缩放到不小于高宽
         * @param minWidth
         * @param minHeight
         * @return
         */
        public ImageOperation zoomNotSmallThan(int minWidth, int minHeight)  {
            ZoomCallBack zcb=new ZoomCallBack();
            zcb.setMinHeight(minHeight);
            zcb.setMinWidth(minWidth);
            return schedule(zcb);

        }

        public ImageOperation schedule(ImageCallBack callBack)  {
            works.add(callBack);
            return this;
        }
        public void saveAs(OutputStream target) throws Exception {
            saveAs(target,null);
        }
        public void saveAs(OutputStream target,String imageType) throws Exception {
            works.add((image, oper)->{
                String realType=imageType==null?this.realType:imageType;
                ImageIO.write(image, realType, target);
                target.close();//FIXME 安全关闭。
                return image;
            });
            executeWorks();
        }
        public ImageOperation mark()throws Exception{
            readImageFromIn();
            BufferedImage working=image;
            for(ImageCallBack callBack:works){
                working =callBack.execute(working,this);
            }
            return ImageOperation.of(working);
        }
        private void readImageFromIn()throws IOException{
            if(image==null) {
                //read imag
                ImageTypeDeligate itd = new ImageTypeDeligate(source);
                image = ImageIO.read(itd);
                itd.close();//FIXME 安全关闭。
                this.realType = itd.getImageTypeName();
            }
        }
        protected void executeWorks()throws Exception{
            readImageFromIn();
            BufferedImage working=image;
            for(ImageCallBack callBack:works){
                working =callBack.execute(working,this);
            }
        }
    }

    /**
     * 等比例缩放
     */
    public static class ZoomCallBack implements ImageCallBack{
        Integer maxWidth;
        Integer maxHeight;
        Integer minWidth;
        Integer minHeight;
        boolean zoomOutIfTooSmall=false;
        Double maxRate;
        Double fixRate;
        Color padColor;

        public Integer getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(Integer maxWidth) {
            this.maxWidth = maxWidth;
        }

        public Integer getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(Integer maxHeight) {
            this.maxHeight = maxHeight;
        }

        public Integer getMinWidth() {
            return minWidth;
        }

        public void setMinWidth(Integer minWidth) {
            this.minWidth = minWidth;
        }

        public Integer getMinHeight() {
            return minHeight;
        }

        public void setMinHeight(Integer minHeight) {
            this.minHeight = minHeight;
        }
        public boolean isZoomOutIfTooSmall() {
            return zoomOutIfTooSmall;
        }

        public void setZoomOutIfTooSmall(boolean zoomOutIfTooSmall) {
            this.zoomOutIfTooSmall = zoomOutIfTooSmall;
        }

        /**
         * 最大比例
         * 如果长宽比超出最大比例，将会切掉超宽或超大的部分。保留中间的部分
         * 这个比例设置为2 和设置为0.5是等价的。
         * @return
         */
        public Double getMaxRate() {
            return maxRate;
        }

        /**
         * 最大比例
         * 如果长宽比超出最大比例，将会切掉超宽或超大的部分。保留中间的部分
         * 这个比例设置为2 和设置为0.5是等价的。
         * @param maxRate
         */
        public void setMaxRate(Double maxRate) {
            this.maxRate = maxRate;
        }

        /**
         * 如果超出最大比例的时候，并设置了fixScale 则不再用最大比例计算长宽比。而用该比例计算。
         * 比如说，可能maxScale设置了2.0.反正这时候都要切图了。有可能干脆多切一点，保留4.0/3.0
         * 可见范围也更多一些。
         * @return
         */
        public Double getFixRate() {
            return fixRate;
        }

        /**
         * 如果超出最大比例的时候，并设置了fixScale 则不再用最大比例计算长宽比。而用该比例计算。
         * 比如说，可能maxScale设置了2.0.反正这时候都要切图了。有可能干脆多切一点，保留1.2
         * 可见范围也更多一些。
         * @param fixRate
         */
        public void setFixRate(Double fixRate) {
            this.fixRate = fixRate;
        }

        /**
         * padColor为填充颜色，如果要透明填充，可以设置alpha值为最大。
         * 填充仅发生在设置了maxWidth和maxHeight，并且等比例缩放后,原图的比例和画布比例不一致。
         * 出现图片和边框中间有空白的，这些空白将会填充这些颜色。
         * @return
         */
        public Color getPadColor() {
            return padColor;
        }

        /**
         * padColor为填充颜色，如果要透明填充，可以设置alpha值为最大。
         * 填充仅发生在设置了maxWidth和maxHeight，并且等比例缩放后,原图的比例和画布比例不一致。
         * 出现图片和边框中间有空白的，这些空白将会填充这些颜色。
         * @param padColor
         */
        public void setPadColor(Color padColor) {
            this.padColor = padColor;
        }
        @Override
        public BufferedImage execute(BufferedImage image, ImageOperation operation) throws Exception {
            int fixedWidth=0,canvasWidth=0;
            int fixedHeight=0,canvasHeight=0;
            boolean needCut=false;
            int cutX=0,cutY=0,cutW=0,cutH=0;
            int imageWidth=image.getWidth();
            int imageHeight=image.getHeight();
            if(maxRate !=null){
                double maxRateTmp= maxRate <1.0 ? 1.0/ maxRate : maxRate;
                double nowRate =imageWidth/imageHeight;
                nowRate = nowRate<1.0 ? 1.0/nowRate: nowRate;
                if(nowRate>maxRateTmp){
                    //需要剪切
                    needCut=true;
                    double realCutScale=maxRateTmp;
                    if(fixRate !=null){
                        double fixRateTemp= fixRate <1.0 ? 1.0/ fixRate : fixRate;
                        if(fixRate <realCutScale){
                            realCutScale=fixRateTemp;
                        }
                    }
                    if(imageWidth>imageHeight){
                        cutW=(int)(imageHeight*realCutScale+0.5);
                        cutH=imageHeight;
                        cutX=(imageWidth-cutW)/2;
                        cutY=0;
                        imageWidth=cutW;
                    }else{
                        cutH=(int)(imageWidth*realCutScale+0.5);
                        cutW=imageWidth;
                        cutY=(imageHeight-cutH)/2;
                        cutX=0;
                        imageHeight=cutH;
                    }
                }
            }
            if(minHeight!=null&&minWidth!=null) {

                double widthScale = new Double(minWidth) / imageWidth;
                double heightScale = new Double(minHeight) / imageHeight;
                if (widthScale <= 1.0 && heightScale <= 1.0) {
                    if(!needCut) {
                        return image; //无需缩放
                    }
                }
                double scale = Math.max(widthScale, heightScale);
                canvasWidth=fixedWidth = (int) (imageWidth * scale + 0.5);
                canvasHeight = fixedHeight=(int) (imageHeight * scale + 0.5);
            }else if(maxHeight!=null&&maxWidth!=null){
                double widthScale = new Double(maxWidth) / imageWidth;
                double heightScale = new Double(maxHeight) / imageHeight;

                double scale=1.0;
                if (widthScale >= 1.0 && heightScale >= 1.0) {
                    if(!zoomOutIfTooSmall) {
                        if(padColor==null&&!needCut) {
                            //无需缩放
                            return image;
                        }
                        //scale=1.0;
                    }else {
                        scale=Math.max(widthScale, heightScale);
                    }
                }else {
                    scale = Math.min(widthScale, heightScale);
                }
                fixedWidth = (int) (imageWidth * scale + 0.5);
                fixedHeight = (int) (imageHeight * scale + 0.5);
                if(padColor==null){
                    canvasWidth=fixedWidth;
                    canvasHeight=fixedHeight;
                }else{
                    canvasWidth=minWidth;
                    canvasHeight=minHeight;
                }
            }
//            System.out.println(
//                    "调试信息，预计画布 width= "+canvasWidth+" height="+canvasWidth+
//                            "\r\n预计需要切图 need=未完成 切图x=未完成 切图y=未完成 切图width=未完成 切图height=未完成" +
//                            "\r\n预计图像大小为 width="+fixedWidth+" height="+minHeight);

            BufferedImage destImage = new BufferedImage(canvasWidth, canvasHeight, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            if(padColor!=null){
                g.setColor(padColor);
                g.fillRect(0,0,canvasWidth,canvasHeight);
            }
            if(needCut){
                image=image.getSubimage(cutX,cutY,cutW,cutH);
            }
            g.drawImage(image, (canvasWidth-fixedWidth)/2, (canvasHeight-fixedHeight)/2,
                    fixedWidth, fixedHeight, null);
            g.dispose();
            return destImage;
        }

    }

    public interface ImageCallBack{
        BufferedImage execute(BufferedImage image,ImageOperation operation) throws Exception;
    }
    public static void main(String[] args) throws Exception {
//        long begin=System.currentTimeMillis();
//        BufferedImage rawImage = ImageIO.read(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg"));
//        showTime("after readImage",begin);
//        Graphics g=rawImage.getGraphics();
//        showTime("after getGraphics",begin);
        ImageUtil.ImageOperation oper=ImageUtil.ImageOperation.of(new FileInputStream("D:\\3_项目\\公司ITASK\\新闻附件\\000000000271.jpg"));
//        ZoomCallBack zoomCallBack=new ZoomCallBack();
//        zoomCallBack.setMaxWidth(200);
//        zoomCallBack.setMaxHeight(200);
//        oper.schedule(zoomCallBack);
        oper.zoomNotBiggerThan(200,200);
        oper.saveAs(new FileOutputStream("C:\\Users\\LinLW\\Desktop\\test.jpg"));

    }

    private static void showTime(String msg,long begin) {
        long cost=System.currentTimeMillis()-begin;
        System.out.println(cost+"ms: "+msg);
    }

}

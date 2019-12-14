package com.rongji.dfish.base;

import com.rongji.dfish.base.util.ByteArrayUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.img.ImageInfo;
import com.rongji.dfish.base.util.img.JpegInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {
    //FIXME 尝试获得JPG原始缩略图（不启动BufferedImage）
    private InputStream source;
    private boolean sourceRead=false;
    private BufferedImage image;
    private List<ImageCallback> works;
    private String realType;

    private static final String READ_MESSAGE=
            "同一个ImputStream只能执行一次操作。当你快速获得文件 高宽，或尝试获得JPG里面原生缩略图，将会消耗掉这个InputStream。" +
            "如果需要执行其他动作，需要InputStream 可以被重置(markSupported=true / ByteArrayInputStream 就是一种)。" +
            "或者可以在动作前用reuse方法，将OutputStream 转成ByteArrayInputStream。" +
            "注意这个操作将会根据图片文件大小占用一定量内存。" ;

    public ImageProcessor(InputStream source){
        this.source=source;
        works=new ArrayList<>();
    }
    public ImageProcessor(BufferedImage image){
        this.image=image;
        works=new ArrayList<>();
    }
    public static ImageProcessor of(InputStream source){
        return new ImageProcessor(source);
    }
    public static ImageProcessor of(BufferedImage image){
        return new ImageProcessor(image);
    }
    /**
     * 同一个ImputStream只能执行一次操作。当你快速获得文件 高宽，或尝试获得JPG里面原生缩略图，将会消耗掉这个InputStream。
     * 如果需要执行其他动作，需要InputStream 可以被重置(markSupported=true / ByteArrayInputStream 就是一种)。
     * 或者可以在动作前用reuse方法，将OutputStream 转成ByteArrayInputStream。
     * 注意这个操作将会根据图片文件大小占用一定量内存。
     * @throws IOException
     */
    public ImageProcessor reuse() throws IOException{
        if(image!=null||source==null||source.markSupported()){
            return this;
        }
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte[] buff=new byte[8192];
        int read=0;
        while ((read=source.read(buff))>=0){
            baos.write(buff,0,read);
        }
        source.close();
        source=new ByteArrayInputStream(baos.toByteArray());
        return this;
    }
    public ImageProcessor schedule(ImageCallback callback)  {
        works.add(callback);
        return this;
    }
    protected void executeWorks()throws Exception{
        readImageFromIn();
        BufferedImage working=image;
        for(ImageCallback callback:works){
            working =callback.execute(working,this);
        }
    }

    /**
     * 快速获取图形信息。这个操作一般会消耗掉InputStream
     * 一般来说，小于2MB 的图片这个性能提升的不明显。
     * 但如果是大于2MB的JPEG，提升就相当明显，并且有很大几率找到硬件(相机/手机)已经预设的缩略图。
     * 在一些场景可能大大提高性能。注意，这个缩略图通常只有160*120像素上下。
     * 如果需要更大的缩略图。也不适合使用这个方法。
     * @return ImageInfo
     */
    public ImageInfo getImageInfo() throws IOException {
        tryReset();
        ImageInfo ii=ImageInfo.of(source);
        this.realType=ii.getTypeName();
        this.sourceRead=true;
        return ii;
    }

    /**
     * 尝试从ImageInfo中获取缩略图的
     * 如果没找到，可能会放回空。
     * @param ii
     * @return
     */
    public static ImageProcessor getThumbnail(ImageInfo ii){
        if(ii instanceof JpegInfo){
            JpegInfo cast=( JpegInfo)ii;
            ByteArrayInputStream bais=cast.getThumbnail();
            if(bais!=null){
                return ImageProcessor.of(bais);
            }
        }
        return null;
    }

    /**
     * mark 将会记住当前image状态，并产生一个新的实例。
     * @return
     * @throws Exception
     */
    public ImageProcessor mark()throws Exception{
        readImageFromIn();
        BufferedImage working=image;
        for(ImageCallback callback:works){
            working =callback.execute(working,this);
        }
        return ImageProcessor.of(working);
    }

    /**
     * 文字水印。
     * @param text 内容
     * @param fontSize 像素
     * @param color 颜色 需要半透明请颜色设置alpha值.注意这里取值范围是0-255
     * @param x 像素。如果x为负数，表示从右边算起。需要自行估计文本宽度
     * @param y 像素。如果y为负数，表示从下面算起。需要自行估计文本高度
     * @return
     */
    public ImageProcessor textWatermark(String text,int fontSize,Color color, int x,int y){
        schedule((image,processor)->{
            int width = image.getWidth(); // 得到源图宽
            int height = image.getHeight(); // 得到源图长
            int realx=x;
            int realy=y;
            if(realx<0){
                realx=width-realx;
            }
            if(realy<0){
                realy=height-realy;
            }
            Graphics2D g = (Graphics2D)image.getGraphics();
            g.setColor(color);
            Font font=new Font(Font.DIALOG,Font.PLAIN,fontSize);
            g.setFont(font);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float)(color.getAlpha()/255.0)));
            // 在指定坐标绘制水印文字
            g.drawString(text, realx, realy);
            g.dispose();
            return image;
        });
        return this;
    }
    /**
     * 文字水印。
     * @param img 内容
     * @param alpha 半透明请颜色设置alpha值 范围0.0 - 1.0
     * @param x 像素。如果x为负数，表示从右边算起。需要自行估计文本宽度
     * @param y 像素。如果y为负数，表示从下面算起。需要自行估计文本高度
     * @return
     */
    public ImageProcessor imgWatermark(BufferedImage img,float alpha, int x,int y){
        schedule((image,processor)->{
            int width = image.getWidth(); // 得到源图宽
            int height = image.getHeight(); // 得到源图长
            int realx=x;
            int realy=y;
            if(realx<0){
                realx=width-realx;
            }
            if(realy<0){
                realy=height-realy;
            }
            Graphics2D g = (Graphics2D)image.getGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            // 在指定坐标绘制水印文字
            g.drawImage(img, realx, realy, img.getWidth(), img.getHeight(), null);

            g.dispose();
            return image;
        });
        return this;
    }
    public ImageProcessor imgWatermark(ImageProcessor another,float alpha, int x,int y) throws IOException {
        if(another.image==null){
            another.readImageFromIn();
        }
        return imgWatermark(another.image,alpha,x,y);
    }
    private void readImageFromIn()throws IOException{
        if(image==null) {
            tryReset();
            //read imag
            ImageTypeDeligate itd = new ImageTypeDeligate(source);
            image = ImageIO.read(itd);
            itd.close();//FIXME 安全关闭。
            sourceRead=true;
            this.realType = itd.getImageTypeName();
        }
    }
    private void tryReset() throws IOException {
        if(sourceRead){
            if(!source.markSupported()) {
                throw new UnsupportedOperationException(READ_MESSAGE );
            }else{
                source.reset();
            }
        }
    }
    /**
     * 缩放到
     * @param width
     * @param height
     * @return
     */
    public ImageProcessor resize(int width, int height)  {
        return schedule((image,oper)->{
            BufferedImage destImage = new BufferedImage(width, height, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.dispose();
            return destImage;
        });
    }

    public ImageProcessor cut(int top, int right,int bottom ,int left)  {
        if(top<0||right<0||bottom<0||left<0){
            throw  new IllegalArgumentException("top, right, bottom, left should not lses than 0");
        }
        return schedule((image,oper)->{
            int destW=image.getWidth()-left-right;
            int destH=image.getHeight()-top-bottom;
            BufferedImage destImage = new BufferedImage(destW, destH, image.getType());
            // 获取画笔工具
            Graphics g = destImage.getGraphics();
            g.drawImage(image, left, top, destW, destH, null);
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
    public ImageProcessor zoom(int maxWidth, int maxHeight)  {
        ZoomCallback zcb=new ZoomCallback();
        zcb.setMaxHeight(maxHeight);
        zcb.setMaxWidth(maxWidth);
        zcb.setZoomOutIfTooSmall(true);
        return schedule(zcb);
    }


    public void saveAs(OutputStream target) throws Exception {
        if(works.size()==0){
            //什么都没做，而且也没有指定文件名变化的。
            // 直接文件流拷贝
            tryReset();
            byte[] buff=new byte[8192];
            int read=0;
            try {
                while ((read = source.read(buff)) >= 0) {
                    target.write(buff, 0, read);
                }
            }catch (IOException iex){
                LogUtil.error(null,iex);
            }finally {
                try{
                    source.close();
                }catch (IOException iex2){}
                try{
                    target.close();
                }catch (IOException iex2){}
            }
            sourceRead=true;
        }else {
            saveAs(target, null);
        }
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





    static class ImageTypeDeligate extends SampleDeligate {

        public ImageTypeDeligate(InputStream raw) {
            super(raw, 4);
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

        public String getImageTypeName() {
            return ImageInfo.TYPE_NAMES[getImageType()];
        }
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




    /**
     * 等比例缩放
     */
    public static class ZoomCallback implements ImageCallback {
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
        public BufferedImage execute(BufferedImage image, ImageProcessor operation) throws Exception {
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

    /**
     * 具体的操作动作，操作动作可以通过回调来减少具体操作的损耗。
     */
    public interface ImageCallback {
        BufferedImage execute(BufferedImage image,ImageProcessor processor) throws Exception;
    }





}

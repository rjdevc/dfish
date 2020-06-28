package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.batch.BatchFunction;
import com.rongji.dfish.base.cache.impl.BaseCache;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgItem;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResult;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResultError;
import com.rongji.dfish.framework.service.IdGenerator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class PreparedJigsawGenerator extends AbstractJigsawGenerator<PreparedJigsawGenerator> {
    Semaphore SEMAPHORE;
    BlockingQueue<ImgResult> IMAGE_QUEUE;
    BlockingQueue<JsonResult> SAVE_FILE_QUEUE;
    LinkedList<JsonResult> histories =new LinkedList<>();
    private String initError;
    long lastCleanTime;
    long cleanInterval=900000;
    private static final Object LOCK=new Object();
    int RESERVE_IMAGES=15;
    int WAIT_THREAD=3;
    int PREPARED=5;
    List<File> imageFiles;
    BaseCache<File,byte[]> FILE_CACHE=new BaseCache<>();
    TemplateData templateData;


    @Override
    public JigsawImgResult generatorJigsaw(HttpServletRequest request) throws Exception {
        ensureEnv();
        //防止过于频繁刷新
        if(initError!=null){
            JigsawImgResult jigsaw = new JigsawImgResult();
            JigsawImgResultError error = new JigsawImgResultError(initError, 0);
            jigsaw.setError(error);
            return jigsaw;
        }
        HttpSession session = request.getSession();
        // 目前反暴力刷图策略暂时以session来判断,以后完善可以增加ip判断
        Integer generatorCount = (Integer) session.getAttribute(KEY_CAPTCHA_COUNT);
        if (generatorCount == null) {
            generatorCount = 0;
        } else if (generatorCount >= maxErrorCount) {
            // FIXME 暂不做控制,需要和前端配合
            Long lastLockTime = (Long) session.getAttribute(KEY_CAPTCHA_LOCK);

            long leftTimeout = 0;
            if (lastLockTime == null) {
                session.setAttribute(KEY_CAPTCHA_LOCK, System.currentTimeMillis());
                leftTimeout = timeout;
            } else {
                // 剩余时间
                leftTimeout = timeout - (System.currentTimeMillis() - lastLockTime);
                if (leftTimeout <= 0) {
                    generatorCount = 0;
                    session.removeAttribute(KEY_CAPTCHA_COUNT);
                    session.removeAttribute(KEY_CAPTCHA_LOCK);
                }
            }
            if (leftTimeout > 0) {
                JigsawImgResult jigsaw = new JigsawImgResult();
                JigsawImgResultError error = new JigsawImgResultError(errorMsg, leftTimeout);
                jigsaw.setError(error);
                return jigsaw;
            }
        }
        session.setAttribute(KEY_CAPTCHA_COUNT, ++generatorCount);

        if(SEMAPHORE.tryAcquire()){
            JsonResult jr= SAVE_FILE_QUEUE.take();
            SEMAPHORE.release();
            cleanIfNecessary();
            session.setAttribute(KEY_CAPTCHA,jr.x);
            return jr.ret;
        }else{
            //从histories 中获取信息
            if(histories.isEmpty()){
                JigsawImgResult jigsaw = new JigsawImgResult();
                JigsawImgResultError error = new JigsawImgResultError("验证码服务过于繁忙，请稍后再试", 0);
                jigsaw.setError(error);
                return jigsaw;
            }
            int i=RANDOM.nextInt(histories.size());
            JsonResult jr=  histories.get(i);
            cleanIfNecessary();
            session.setAttribute(KEY_CAPTCHA,jr.x);
            return jr.ret;
        }
    }

    private void ensureEnv() {
        if(SAVE_FILE_QUEUE==null){
            synchronized (LOCK){
                if(SAVE_FILE_QUEUE==null){
                    initEnv();
                }
            }
        }
    }

    private void cleanIfNecessary() {
        long now=System.currentTimeMillis();
        if(now-lastCleanTime>cleanInterval){
            //clean;
            String destDirPath = getImageRawDir() + FOLDER_TEMP + "/";
            File destDir = new File(destDirPath);
            //将最新的文件保留至少15份
            if(destDir.exists()&&destDir.isDirectory()){
                File[] files=destDir.listFiles();
                if(files!=null) {
                    Arrays.sort(files, (o1, o2) -> {
                        long lm1 = o1.lastModified();
                        long lm2 = o2.lastModified();
                        return Long.compare(lm1, lm2);
                    });
                    int i = 0;
                    int max = files.length - 2 * RESERVE_IMAGES;
                    for (File file : files) {
                        if (i++ >= max) {
                            break;
                        }
                        if (!file.isFile()) {
                            continue;
                        }
                        if (now - file.lastModified() > cleanInterval * 2) {
                            try {
                                file.delete();
                            } catch (Exception ex) {
                                LogUtil.error("Delete file " + file.getAbsolutePath() + " fail!");
                            }
                        }
                    }
                }
            }
            //更新文件
            File imageDir = new File(getImageRawDir());
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            if (!imageDir.isDirectory()) {
                initError="验证码拼图路径非目录";
                return;
            }
            File[] subFiles = imageDir.listFiles();
            imageFiles = new ArrayList<>();
            if(subFiles!=null) {
                for (File file : subFiles) {
                    if (file.isFile()) {
                        imageFiles.add(file);
                    }
                }
            }
            if (Utils.isEmpty(imageFiles)) {
                initError="验证码拼图缺少图片，请在该路径下补充图片[" + getImageRawDir() + "]";
                return;
            }
            FILE_CACHE.clear();
            lastCleanTime=now;

        }
    }


    private void initEnv() {
        SEMAPHORE=new Semaphore(WAIT_THREAD);
        IMAGE_QUEUE =new LinkedBlockingQueue<>(1);
        SAVE_FILE_QUEUE=new LinkedBlockingQueue<>(PREPARED);
        FILE_CACHE.setValueGetter(new BatchFunction<File, byte[]>() {

            @Override
            public Map<File, byte[]> apply(Set<File> set) {
                Map<File,byte[]> ret=new HashMap<>();
                byte[] buff=new byte[8192];
                int read=0;
                for(File f:set){
                    try(FileInputStream fis=new FileInputStream(f)){
                        ByteArrayOutputStream baos=new ByteArrayOutputStream();
                        while((read=fis.read(buff))>=0){
                            baos.write(buff,0,read);
                        }
                        ret.put(f,baos.toByteArray());
                    }catch (Exception ex){
                        LogUtil.error(null,ex);
                    }
                }
                return ret;
            }
        });
        //给三个都安排上任务。
        ExecutorService SAVE_FILE_ES=Executors.newSingleThreadExecutor();
        ExecutorService IMAGE_ES=Executors.newSingleThreadExecutor();
        File imageDir = new File(getImageRawDir());
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        if (!imageDir.isDirectory()) {
            initError="验证码拼图路径非目录";
            return;
        }
        File[] subFiles = imageDir.listFiles();
        imageFiles = new ArrayList<>();
        if(subFiles!=null) {
            for (File file : subFiles) {
                if (file.isFile()) {
                    imageFiles.add(file);
                }
            }
        }
        if (Utils.isEmpty(imageFiles)) {
            initError="验证码拼图缺少背景图片，请在该路径下补充图片[" + getImageRawDir() + "]";
            return;
        }
        try {
            templateData = getJigsawData(new File(getImageRawDir()+"block/mask.png"));
        } catch (Exception e) {
            initError="验证码拼图缺少形状图片，请在该路径下补充图片[" + getImageRawDir() + "block/mask.png]";
            return;
        }
        this.setSmallSize(templateData.getWidth());

        IMAGE_ES.submit(new Runnable (){

            public void run(){
                while(true){
                    //生成两个
                    int minWidthPosition = bigWidth >> 3;
                    int minHeightPosition = bigHeight >> 3;

                    int maxWidth = bigWidth - (minWidthPosition << 1) - smallSize;
                    int maxHeight = bigHeight - (minHeightPosition << 1) - smallSize;
                    // 横坐标位置
                    int x = minWidthPosition + RANDOM.nextInt(maxWidth);
                    int y = minHeightPosition + RANDOM.nextInt(maxHeight);


                    int fileIndex = RANDOM.nextInt(imageFiles.size());
                    File rawFile = imageFiles.get(fileIndex);
                    try {
                        InputStream input = new ByteArrayInputStream(FILE_CACHE.get(rawFile));
                        BufferedImage oriImage = ImageIO.read(input);
                        //背景图宽和高
                        BufferedImage newImage = new BufferedImage(templateData.getWidth(), bigHeight, BufferedImage.TYPE_INT_ARGB);//FIXME 模板图片的类型
                        Graphics2D graphics = newImage.createGraphics();
                        newImage = graphics.getDeviceConfiguration().createCompatibleImage(templateData.getWidth(), bigHeight, Transparency.TRANSLUCENT);
                        cutByTemplatePic(oriImage, templateData, newImage, x, y);
                        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics.setStroke(new BasicStroke(Font.BOLD, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
                        graphics.drawImage(newImage, 0, 0, null);
                        graphics.dispose();

                        ImgResult r = new ImgResult();
                        r.bImg=oriImage;
                        r.sImg=newImage;
                        r.rawFileName=rawFile.getName();
                        r.x=x;
                        IMAGE_QUEUE.put(r);
                    } catch (Exception e) {
                        LogUtil.error("can not generate img",e);
                    }
                }
            }});
        SAVE_FILE_ES.submit(new Runnable (){

            public void run(){
                while(true){
                    //生成两个
                    try {
                        ImgResult r=IMAGE_QUEUE.take();
                        JsonResult ret=new JsonResult();

                        ret.ret=new JigsawImgResult();
                        String fileExtName = FileUtil.getFileExtName(r.rawFileName);
                        String jigsawFileName= IdGenerator.getSortedId32();


                        String sFileName = jigsawFileName + "-S" + fileExtName;
                        File sDestFile = getTempDestFile(sFileName);
                        FileOutputStream outputS = new FileOutputStream(sDestFile);
                        ImageIO.write(r.sImg, getRealExtName(fileExtName), outputS);

                        String bFileName = jigsawFileName + "-B" + fileExtName;
                        File bDestFile = getTempDestFile(bFileName);
                        FileOutputStream outputB = new FileOutputStream(bDestFile);
                        ImageIO.write(r.bImg, getRealExtName(fileExtName), outputB);

                        ret.ret.setBig(new JigsawImgItem(imageFolder + FOLDER_TEMP + "/" + bFileName, bigWidth, bigHeight));
                        ret.ret.setSmall(new JigsawImgItem(imageFolder + FOLDER_TEMP + "/" + sFileName, smallSize, smallSize));
                        ret.x=r.x;
                        ret.ret.setMaxValue(bigWidth);


                        SAVE_FILE_QUEUE.put(ret);
                        histories.add(ret);
                        if(histories.size()>RESERVE_IMAGES){
                            histories.poll();
                        }
                    } catch (Exception e) {
                        LogUtil.error("can not put ret",e);
                    }
                }
            }});

    }


//    private static class GeneratorSmallImage implements Callable<BufferedImage> {
//        PreparedJigsawGenerator owner;
//        File rawFile;
//        int x;
//        int y;
//        int width;
//        int height;
//        public GeneratorSmallImage(PreparedJigsawGenerator owner, File rawFile, int x, int y, int width, int height) {
//            this.owner=owner;
//            this.rawFile=rawFile;
//            this.x=x;
//            this.y=y;
//            this.width=width;
//            this.height=height;
//        }
//
//        @Override
//        public BufferedImage call() throws Exception {
//
//            // 读取原始图片
//            try (InputStream input = new ByteArrayInputStream(owner.FILE_CACHE.get(rawFile))) {
//                BufferedImage rawImage = ImageIO.read(input);
//
//                BufferedImage subImage = rawImage.getSubimage(x, y, width, height);
////            BufferedImage destImage = new BufferedImage(width, rawImage.getHeight(), subImage.getType());
//                Graphics2D g = subImage.createGraphics();
//                BufferedImage destImage = g.getDeviceConfiguration().createCompatibleImage(width, rawImage.getHeight(), Transparency.TRANSLUCENT);
//                g.dispose();
//
////            Graphics g2 = destImage.createGraphics();
//                g = (Graphics2D) destImage.getGraphics();
//                g.setColor(new Color(255, 255, 255, 0));
//                // 必须调用这个方法将背景色填充到图片去
//                g.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
////            g2.setColor(new Color(0, 0, 0, 0));
////            g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
//                g.drawImage(subImage, 0, y, width, height, null);
//                // 小图片周边虚化
//                g.setColor(owner.getGapColor());
//                // 虚化大小
//                int blurSize = 1;
//                // 上
//                g.fillRect(0, y, width, blurSize);
//                // 右
//                g.fillRect(width - blurSize, y, blurSize, height);
//                // 下
//                g.fillRect(0, y + width - blurSize, width, blurSize);
//                // 左
//                g.fillRect(0, y, blurSize, height);
//
//                g.dispose();
//                return destImage;
//            }
//        }
//    }
//    private static class GeneratorBigImage implements Callable<BufferedImage> {
//        PreparedJigsawGenerator owner;
//        File rawFile;
//        int x;
//        int y;
//        int width;
//        int height;
//        public GeneratorBigImage(PreparedJigsawGenerator owner, File rawFile, int x, int y, int width, int height) {
//            this.owner=owner;
//            this.rawFile=rawFile;
//            this.x=x;
//            this.y=y;
//            this.width=width;
//            this.height=height;
//        }
//        @Override
//        public BufferedImage call() throws Exception {
//            try (InputStream input = new ByteArrayInputStream(owner.FILE_CACHE.get(rawFile))) {
//                BufferedImage rawImage = ImageIO.read(input);
//                Graphics g = rawImage.getGraphics();
//                g.setColor(owner.getGapColor());
//                // 必须调用这个方法将背景色填充到图片去
//                g.fillRect(x, y, width, height);
//                g.dispose();
//                return rawImage;
//            }
//        }
//    }



    private static class ImgResult{
        BufferedImage sImg;
        BufferedImage bImg;
        String rawFileName;
        int x;
    }
    private static class JsonResult{
        JigsawImgResult ret;
        int x;
    }
    private static void cutByTemplatePic(BufferedImage oriImage, TemplateData templateData, BufferedImage newImage, int x, int y){
        //临时数组遍历用于高斯模糊存周边像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];
        List<int[]> jigsawList = templateData.getJigsawList();//拼图框架坐标
        List<int[]> boundaryList = templateData.getBoundaryList();//拼图边界框架坐标
        for (int[] jc: jigsawList) {
            if(jc != null){
                int i = jc[0];
                int j = jc[1];
                try {

                    newImage.setRGB(i, y+j, oriImage.getRGB(x + i, y + j));
                }catch (Exception ex){
                    throw ex;
                }
                //抠图区域高斯模糊
                readPixel(oriImage, x + i, y + j, values);
                fillMatrix(matrix, values);
                oriImage.setRGB(x + i, y + j, avgMatrix(matrix));
            }
        }
        for (int[] bc: boundaryList) {
            if(bc != null){
                int i = bc[0];
                int j = bc[1];
                newImage.setRGB(i, y+j, Color.white.getRGB());
            }
        }
    }

    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - 1;
        int yStart = y - 1;
        int current = 0;
        for (int i = xStart; i < 3 + xStart; i++) {
            for (int j = yStart; j < 3 + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);
            }
        }
    }

    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int[] x: matrix) {
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / matrix.length, g / matrix.length, b / matrix.length).getRGB();
    }
    private static TemplateData getJigsawData(File file) throws Exception {
        //从缓存中取出


        List<int[]> blockList = new ArrayList<>();
        List<int[]> boundaryList = new ArrayList<>();
        FileInputStream fileinput = new FileInputStream(file);
        BufferedImage jigsawImage = ImageIO.read(fileinput);
        int xLength = jigsawImage.getWidth();
        int yLength = jigsawImage.getHeight();
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                int rgb = jigsawImage.getRGB(i, j);
                if (rgb < 0) {
                    blockList.add(new int[]{i,j});
                }
                if(i == (xLength - 1) || j == (yLength - 1)){
                    continue;
                }
                int rightRgb = jigsawImage.getRGB(i + 1, j);
                int downRgb = jigsawImage.getRGB(i, j + 1);
                if((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)){
                    boundaryList.add(new int[]{i,j});
                }
            }
        }
        return new TemplateData(xLength, yLength, jigsawImage.getType(), blockList, boundaryList);
    }


    private static class TemplateData {
        private int width;//宽
        private int height;//高
        private int type;//图片类型
        private List<int[]> jigsawList;//拼图图案坐标
        private List<int[]> boundaryList;//拼图边界坐标

        public TemplateData() {
        }

        public TemplateData(int width, int height, int type, List<int[]> jigsawList, List<int[]> boundaryList) {
            this.width = width;
            this.height = height;
            this.type = type;
            this.jigsawList = jigsawList;
            this.boundaryList = boundaryList;
        }

        public int getWidth() { return width; }

        public void setWidth(int width) { this.width = width; }

        public int getHeight() { return height; }

        public void setHeight(int height) { this.height = height; }

        public int getType() { return type; }

        public void setType(int type) { this.type = type; }

        public List<int[]> getJigsawList() {
            return jigsawList;
        }

        public void setJigsawList(List<int[]> jigsawList) {
            this.jigsawList = jigsawList;
        }

        public List<int[]> getBoundaryList() {
            return boundaryList;
        }

        public void setBoundaryList(List<int[]> boundaryList) {
            this.boundaryList = boundaryList;
        }
    }


}

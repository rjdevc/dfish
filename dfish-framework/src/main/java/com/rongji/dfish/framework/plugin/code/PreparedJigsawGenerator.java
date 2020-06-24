package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.base.util.LogUtil;
import com.rongji.dfish.framework.IdGenerator;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgItem;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResult;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgResultError;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class PreparedJigsawGenerator extends AbstractJigsawGenerator<PreparedJigsawGenerator> {
    Semaphore SEMAPHORE;
    BlockingQueue<ImgResult> IMAGE_QUEUE;
    BlockingQueue<JsonResult> SAVE_FILE_QUEUE;
    LinkedList<JsonResult> histories =new LinkedList<>();
    private String initError;
    long lastCleanTime;
    long cleanInterval=300000;
    private static final Object LOCK=new Object();

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
            JsonResult jr= SAVE_FILE_QUEUE.poll();
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
            if(destDir.exists()&&destDir.isDirectory()){
                for(File file:destDir.listFiles()){
                    if(!file.isFile()){
                        continue;
                    }
                    if(now-file.lastModified()>cleanInterval*2){
                        file.delete();
                    }
                }
            }

            lastCleanTime=now;
        }
    }


    private void initEnv() {
        SEMAPHORE=new Semaphore(3);
        IMAGE_QUEUE =new LinkedBlockingQueue<>(1);
        SAVE_FILE_QUEUE=new LinkedBlockingQueue<>(5);
        //给三个都安排上任务。
        final ExecutorService B_IMAGE_ES=Executors.newSingleThreadExecutor();
        final ExecutorService S_IMAGE_ES=Executors.newSingleThreadExecutor();
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
        final List<File> imageFiles = new ArrayList<>();
        for (File file : subFiles) {
            if (file.isFile()) {
                imageFiles.add(file);
            }
        }
        if (Utils.isEmpty(imageFiles)) {
            initError="验证码拼图缺少图片，请在该路径下补充图片[" + getImageRawDir() + "]";
            return;
        }
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
                    Future<BufferedImage> imgS = S_IMAGE_ES.submit(new GeneratorSmallImage(PreparedJigsawGenerator.this,rawFile, x, y, smallSize, smallSize));
                    Future<BufferedImage> imgB = B_IMAGE_ES.submit(new GeneratorBigImage(PreparedJigsawGenerator.this,rawFile, x, y, smallSize, smallSize));

                    ImgResult r = new ImgResult();
                    r.bImg=imgB.get();
                    r.sImg=imgS.get();
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
                ImgResult r=IMAGE_QUEUE.poll();
                JsonResult ret=new JsonResult();

                JigsawImgResult jir=new JigsawImgResult();
                ret.ret=jir;
                String fileExtName = FileUtil.getFileExtName(r.rawFileName);
                try {
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
                    ret.ret.setBig(new JigsawImgItem(imageFolder + FOLDER_TEMP + "/" + sFileName, smallSize, smallSize));
                    ret.x=r.x;
                    ret.ret.setMaxValue(bigWidth);


                    SAVE_FILE_QUEUE.put(ret);
                    histories.add(ret);
                    if(histories.size()>15){
                        histories.poll();
                    }
                } catch (Exception e) {
                    LogUtil.error("can not put ret",e);
                }
            }
        }});

    }


    private static class GeneratorSmallImage implements Callable<BufferedImage> {
        PreparedJigsawGenerator owner;
        File rawFile;
        int x;
        int y;
        int width;
        int height;
        public GeneratorSmallImage(PreparedJigsawGenerator owner, File rawFile, int x, int y, int width, int height) {
            this.owner=owner;
            this.rawFile=rawFile;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }

        @Override
        public BufferedImage call() throws Exception {

            // 读取原始图片
            try (FileInputStream input = new FileInputStream(rawFile)) {
                BufferedImage rawImage = ImageIO.read(input);

                BufferedImage subImage = rawImage.getSubimage(x, y, width, height);
//            BufferedImage destImage = new BufferedImage(width, rawImage.getHeight(), subImage.getType());
                Graphics2D g = subImage.createGraphics();
                BufferedImage destImage = g.getDeviceConfiguration().createCompatibleImage(width, rawImage.getHeight(), Transparency.TRANSLUCENT);
                g.dispose();

//            Graphics g2 = destImage.createGraphics();
                g = (Graphics2D) destImage.getGraphics();
                g.setColor(new Color(255, 255, 255, 0));
                // 必须调用这个方法将背景色填充到图片去
                g.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
//            g2.setColor(new Color(0, 0, 0, 0));
//            g2.fillRect(0, 0, destImage.getWidth(), destImage.getHeight());
                g.drawImage(subImage, 0, y, width, height, null);
                // 小图片周边虚化
                g.setColor(owner.getGapColor());
                // 虚化大小
                int blurSize = 1;
                // 上
                g.fillRect(0, y, width, blurSize);
                // 右
                g.fillRect(width - blurSize, y, blurSize, height);
                // 下
                g.fillRect(0, y + width - blurSize, width, blurSize);
                // 左
                g.fillRect(0, y, blurSize, height);

                g.dispose();
                return rawImage;
            }
        }
    }
    private static class GeneratorBigImage implements Callable<BufferedImage> {
        PreparedJigsawGenerator owner;
        File rawFile;
        int x;
        int y;
        int width;
        int height;
        public GeneratorBigImage(PreparedJigsawGenerator owner, File rawFile, int x, int y, int width, int height) {
            this.owner=owner;
            this.rawFile=rawFile;
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height;
        }
        @Override
        public BufferedImage call() throws Exception {
            try (FileInputStream input = new FileInputStream(rawFile)) {
                BufferedImage rawImage = ImageIO.read(input);
                Graphics g = rawImage.getGraphics();
                g.setColor(owner.getGapColor());
                // 必须调用这个方法将背景色填充到图片去
                g.fillRect(x, y, width, height);
                g.dispose();
                return rawImage;
            }
        }
    }

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
}

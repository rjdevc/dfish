package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgItem;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImg;
import com.rongji.dfish.framework.plugin.code.dto.JigsawImgError;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 拼图滑块验证工具
 *
 * @author lamontYu
 * @since DFish3.2
 */
public class SimpleJigsawGenerator extends AbstractJigsawGenerator<SimpleJigsawGenerator> {

    /**
     * 生成拼图
     *
     * @param request 请求
     * @return
     * @throws Exception
     */
    @Override
    public JigsawImg generatorJigsaw(HttpServletRequest request) throws Exception {
//        // 大图宽高都必须是小图的4倍
//        int bigMinSize = smallSize << 2;
//        if (smallSize <= 0 || bigWidth < bigMinSize || bigHeight < bigMinSize) {
//            throw new UnsupportedOperationException("图片切割大小图宽高设置不符合规范");
//        }
        HttpSession session = request.getSession();
        // 目前反暴力刷图策略暂时以session来判断,以后完善可以增加ip判断
        Integer generatorCount = (Integer) session.getAttribute(KEY_CAPTCHA_COUNT);
        JigsawImg jigsaw = new JigsawImg();

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
                JigsawImgError error = new JigsawImgError(errorMsg, leftTimeout);
                jigsaw.setError(error);
                return jigsaw;
            }
        }
        session.setAttribute(KEY_CAPTCHA_COUNT, ++generatorCount);

        // 图片周边预留1/8的位置
        int minWidthPosition = bigWidth >> 3;
        int minHeightPosition = bigHeight >> 3;

        int maxWidth = bigWidth - (minWidthPosition << 1) - smallSize;
        int maxHeight = bigHeight - (minHeightPosition << 1) - smallSize;
        // 横坐标位置
        int x = minWidthPosition + RANDOM.nextInt(maxWidth);
        int y = minHeightPosition + RANDOM.nextInt(maxHeight);

        File imageDir = new File(getImageRawDir());
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        if (!imageDir.isDirectory()) {
            throw new MarkedException("验证码拼图路径非目录");
        }
        File[] subFiles = imageDir.listFiles();
        List<File> imageFiles = new ArrayList<>();
        for (File file : subFiles) {
            if (file.isFile()) {
                imageFiles.add(file);
            }
        }
        if (Utils.isEmpty(imageFiles)) {
            throw new MarkedException("验证码拼图缺少图片，请在该路径下补充图片[" + getImageRawDir() + "]");
        }
        int fileIndex = RANDOM.nextInt(imageFiles.size());
        File rawFile = imageFiles.get(fileIndex);

        String jigsawFileName = session.getId() + "-" + System.currentTimeMillis();
        JigsawImgItem bigImg = generatorBigImage(jigsawFileName, rawFile, x, y, smallSize, smallSize);
        JigsawImgItem smallImg = generatorSmallImage(jigsawFileName, rawFile, x, y, smallSize, smallSize);
        // 将验证码放到session中
        session.setAttribute(KEY_CAPTCHA, x);

        jigsaw.setBig(bigImg);
        jigsaw.setSmall(smallImg);
        jigsaw.setMaxValue(bigWidth);
        return jigsaw;
    }





    /**
     * 生成大拼图
     *
     * @param jigsawFileName 会话编号
     * @param rawFile        原始文件
     * @param x              切块开始的横坐标
     * @param y              切块开始的纵坐标
     * @param width          切块宽度
     * @param height         切块高度
     * @return 大图片组件
     * @throws Exception
     */
    private JigsawImgItem generatorBigImage(String jigsawFileName, File rawFile, int x, int y, int width, int height) throws Exception {
        FileOutputStream output = null;
        try (FileInputStream input = new FileInputStream(rawFile)) {
            String fileExtName = FileUtil.getFileExtName(rawFile.getName());
            String destFileName = jigsawFileName + "-B" + fileExtName;
            File tempDestFile = getTempDestFile(destFileName);
            output = new FileOutputStream(tempDestFile);
            // 读取原始图片
            BufferedImage rawImage = ImageIO.read(input);

            Graphics g = rawImage.getGraphics();
//        Graphics2D g = rawImage.createGraphics();
//        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

            g.setColor(getGapColor());
            // 必须调用这个方法将背景色填充到图片去
            g.fillRect(x, y, width, height);

            ImageIO.write(rawImage, getRealExtName(fileExtName), output);
            g.dispose();

            return parseImg(destFileName, rawImage.getWidth(), rawImage.getHeight());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 生成小拼图
     *
     * @param jigsawFileName 会话编号
     * @param rawFile        原始文件
     * @param x              切块开始的横坐标
     * @param y              切块开始的纵坐标
     * @param width          切块宽度
     * @param height         切块高度
     * @return 小图片组件
     * @throws Exception
     */
    private JigsawImgItem generatorSmallImage(String jigsawFileName, File rawFile, int x, int y, int width, int height) throws Exception {
        FileOutputStream output = null;
        try (FileInputStream input = new FileInputStream(rawFile)) {
            String fileExtName = FileUtil.getFileExtName(rawFile.getName());
            String destFileName = jigsawFileName + "-S" + fileExtName;
            File tempDestFile = getTempDestFile(destFileName);
            output = new FileOutputStream(tempDestFile);
            // 读取原始图片
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
            g.setColor(getGapColor());
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
            // 输出图片
            ImageIO.write(destImage, getRealExtName(fileExtName), output);

            return parseImg(destFileName, destImage.getWidth(), destImage.getHeight());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }




}

package com.rongji.dfish.framework.plugin.code;

import com.rongji.dfish.base.exception.MarkedException;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.base.context.SystemContext;
import com.rongji.dfish.base.util.FileUtil;
import com.rongji.dfish.framework.info.ServletInfo;
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
import java.util.List;
import java.util.Random;

/**
 * 拼图滑块验证工具
 *
 * @author lamontYu
 * @date 2019-05-07
 * @since 3.2
 */
public class JigsawGenerator {

    public static final String KEY_CAPTCHA = "com.rongji.dfish.CAPTCHA.JIGSAW";
    @Deprecated
    public static final String KEY_CHECKCODE = KEY_CAPTCHA;
    private static final String KEY_CAPTCHA_COUNT = "com.rongji.dfish.CAPTCHA.COUNT";
    private static final String KEY_CAPTCHA_LOCK = "com.rongji.dfish.CAPTCHA.LOCK";

    /**
     * 大图宽度
     */
    private int bigWidth = 400;
    /**
     * 大图高度
     */
    private int bigHeight = 200;
    /**
     * 小图大小(正方形)
     */
    private int smallSize = 64;
    /**
     * 验证图片目录
     */
    private String imageFolder = "x/jigsaw/";
    /**
     * 误差范围
     */
    private double errorRange = 0.05;
    /**
     * 最大错误次数
     */
    private int maxErrorCount = 16;
    /**
     * 错误提示语
     */
    private String errorMsg = "次数过多,请稍后再试";
    /**
     * 锁定时间(单位:毫秒)
     */
    private long timeout = 60000L;


    /**
     * 大拼图缺口背景色
     */
    private Color gapColor = new Color(255, 255, 255, 220);

    /**
     * 大拼图宽度
     * @return int
     */
    public int getBigWidth() {
        return bigWidth;
    }

    /**
     * 大拼图宽度
     * @param bigWidth int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setBigWidth(int bigWidth) {
        this.bigWidth = bigWidth;
        return this;
    }

    /**
     * 大拼图高度
     * @return int
     */
    public int getBigHeight() {
        return bigHeight;
    }

    /**
     * 大拼图高度
     * @param bigHeight int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setBigHeight(int bigHeight) {
        this.bigHeight = bigHeight;
        return this;
    }

    /**
     * 小拼图大小(正方形)
     * @return int
     */
    public int getSmallSize() {
        return smallSize;
    }

    /**
     * 小拼图大小(正方形)
     * @param smallSize int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setSmallSize(int smallSize) {
        this.smallSize = smallSize;
        return this;
    }

    /**
     * 图片候选目录
     * @return String
     */
    public String getImageFolder() {
        return imageFolder;
    }

    /**
     * 图片候选目录
     * @param imageFolder String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
        return this;
    }

    /**
     * 空隙颜色
     * @return Color
     */
    public Color getGapColor() {
        return gapColor;
    }

    /**
     * 空隙颜色
     * @param gapColor Color
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setGapColor(Color gapColor) {
        this.gapColor = gapColor;
        return this;
    }

    /**
     * 容错精度范围
     * @return double
     */
    public double getErrorRange() {
        return errorRange;
    }

    /**
     * 容错精度范围
     * @param errorRange double
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setErrorRange(double errorRange) {
        this.errorRange = errorRange;
        return this;
    }

    /**
     * 最大错误次数
     * @return int
     */
    public int getMaxErrorCount() {
        return maxErrorCount;
    }

    /**
     * 最大错误次数
     * @param maxErrorCount int
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setMaxErrorCount(int maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
        return this;
    }

    /**
     * 错误信息
     * @return String
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 错误信息
     * @param errorMsg String
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    /**
     * 超限后需要等待的时长(毫秒)
     * @return long
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * 超限后需要等待的时长(毫秒)
     * @param timeout long
     * @return 本身，这样可以继续设置其他属性
     */
    public JigsawGenerator setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    private static final Random RANDOM = new Random();

    /**
     * 生成拼图
     *
     * @param request 请求
     * @return
     * @throws Exception
     */
    public JigsawImgResult generatorJigsaw(HttpServletRequest request) throws Exception {
//        // 大图宽高都必须是小图的4倍
//        int bigMinSize = smallSize << 2;
//        if (smallSize <= 0 || bigWidth < bigMinSize || bigHeight < bigMinSize) {
//            throw new UnsupportedOperationException("图片切割大小图宽高设置不符合规范");
//        }
        HttpSession session = request.getSession();
        // 目前反暴力刷图策略暂时以session来判断,以后完善可以增加ip判断
        Integer generatorCount = (Integer) session.getAttribute(KEY_CAPTCHA_COUNT);
        JigsawImgResult jigsaw = new JigsawImgResult();

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
                JigsawImgResultError error = new JigsawImgResultError(errorMsg, leftTimeout);
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
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offsetStr 偏移量
     * @return boolean 是否验证通过
     */
    public boolean checkJigsawOffset(HttpServletRequest request, String offsetStr) {
        if (Utils.isEmpty(offsetStr)) {
            return false;
        }
        try {
            Double offset = Double.parseDouble(offsetStr);
            return checkJigsawOffset(request, offset);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验拼图是否正确
     *
     * @param request 请求
     * @param offset 偏移量
     * @return boolean 是否验证通过
     */
    public boolean checkJigsawOffset(HttpServletRequest request, Number offset) {
        if (offset == null) {
            return false;
        }
        double customOffset = offset.doubleValue() * (bigWidth - smallSize) / bigWidth;
        HttpSession session = request.getSession();
        // KEY_CAPTCHA,需要二次验证
        Integer realOffset = (Integer) session.getAttribute(KEY_CAPTCHA);
        // 小于误差范围内都是校验成功
        boolean match = realOffset != null && Math.abs((customOffset - realOffset) / realOffset) <= errorRange;
        if (match) {
            // 匹配成功,清理错误计数数据
            session.removeAttribute(KEY_CAPTCHA_COUNT);
            session.removeAttribute(KEY_CAPTCHA_LOCK);
        } else {
            // 验证不通过,需要将验证码清理,防止暴力破解
            request.getSession().removeAttribute(JigsawGenerator.KEY_CAPTCHA);
        }
        return match;
    }

    /**
     * 获取原始图片目录
     *
     * @return Strnig
     */
    private String getImageRawDir() {
        String realServletPath = SystemContext.getInstance().get(ServletInfo.class).getServletRealPath();
        if (!realServletPath.endsWith("/")) {
            realServletPath += "/";
        }
        if (Utils.notEmpty(imageFolder) && !imageFolder.endsWith("/")) {
            imageFolder += "/";
        }
        return realServletPath + imageFolder;
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

    /**
     * 获取真实的扩展名,不含.
     *
     * @param fileExtName 真实的扩展名
     * @return String
     */
    private String getRealExtName(String fileExtName) {
        if (fileExtName != null && fileExtName.startsWith(".")) {
            return fileExtName.substring(1);
        }
        return fileExtName;
    }

    /**
     * 临时文件目录
     */
    private static final String FOLDER_TEMP = "temp";

    /**
     * 获取临时目标文件
     *
     * @param destFileName 临时目标文件名
     * @return 临时目标文件
     * @throws Exception
     */
    private File getTempDestFile(String destFileName) throws Exception {
        String destDirPath = getImageRawDir() + FOLDER_TEMP + "/";
        File destDir = new File(destDirPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File destFile = new File(destDirPath + destFileName);
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        return destFile;
    }

    private JigsawImgItem parseImg(String destFileName, int width, int height) {
        return new JigsawImgItem(imageFolder + FOLDER_TEMP + "/" + destFileName, width, height);
    }

}
